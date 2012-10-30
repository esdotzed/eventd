from django.contrib.auth.models import User
from django.http import Http404, HttpResponseRedirect
from django.shortcuts import render, render_to_response
from django.template import RequestContext

from eventdapp.models import Event
from eventdapp.models import UserProfile
from eventdapp.models import Attendence
from eventdapp.forms import CustomUserCreationForm, EventForm
from eventdapp.models import AddFriendRequest

def register(request):
  if request.method == 'POST':
    form = CustomUserCreationForm(request.POST, request.FILES)
    if form.is_valid():
      new_user = form.save()
      return HttpResponseRedirect("/")
  else:
    form = CustomUserCreationForm()
  return render_to_response("eventdapp/register.html", {
    'form': form,
  }, context_instance=RequestContext(request))

def view_event(request, event_id):
  event = Event.objects.get(pk=event_id)
  owner_id = event.owner.id
  
  #determine whether the user has activated the participation status
  attendence = Attendence.objects.filter(event__pk = event.id, participant__pk = request.user.id)
  
  attendence_choices = Attendence.get_remaining_choices(
                          attendence[0].participation if attendence.exists() else None)
  template_vars = {
    'event': event,
    'attendence_choices':attendence_choices,
    'is_own':(request.user.id == owner_id),
    'is_not_own': (request.user.id != owner_id),
    }
  if attendence.exists():
    template_vars['status'] = attendence[0].get_participation_display()

  return render(request, 'eventdapp/event.html', template_vars)

def create_event(request):
  return display_event_form(request, redirect="/")

def delete_event(request, event_id):
  if not request.user.is_authenticated():
    return HttpResponseRedirect("/login/")
  user_id = request.user.id 
  event = Event.objects.get(pk=event_id)
  owner_id = event.owner.id
  if user_id == owner_id:
    event.delete()
    return HttpResponseRedirect("/")
  else:
    return render(request, 'eventdapp/nopermit.html',{
      'type' : "delete",
    })
  
def edit_event(request, event_id):
  if not request.user.is_authenticated():
    return HttpResponseRedirect("/login/")
  user_id = request.user.id 
  event = Event.objects.get(pk=event_id)
  owner_id = event.owner.id
  if user_id == owner_id:
    return display_event_form(request, redirect="../../{}".format(event.id), instance=event)
  else:
    return render(request, 'eventdapp/nopermit.html',{
      'type' : "edit",
    })
    
def display_event_form(request, **kwargs):
  if not request.user.is_authenticated():
    return HttpResponseRedirect("/login/")
  redirect_addr = kwargs.pop("redirect")
  if request.method == 'POST':
    form = EventForm(request.POST, request.FILES, request=request, **kwargs)
    if form.is_valid():
      new_event = form.save()
      return HttpResponseRedirect(redirect_addr)
  else:
    form = EventForm(request=request, **kwargs)

  return render(request, 'eventdapp/event_form.html', {
    'form': form,
  })

def view_own_homepage(request):
  if not request.user.is_authenticated():
    return HttpResponseRedirect("/login/")
  return view_user(request, request.user.id)

def view_user(request, user_id):
  user = User.objects.get(pk=user_id)
  username = user.username
  user_profile_id = user.get_profile().id

  own_events = Event.objects.filter(owner=user)
  participation_event_ids = Attendence.objects.filter(participant=user).values_list('event_id')
  participation_events = Event.objects.filter(id__in=participation_event_ids)
  events = own_events | participation_events

  addFriendRequests = AddFriendRequest.objects.filter(requester=request.user, requestee=user)
  is_added = addFriendRequests.exists()
  
  #check whether has friendship
  is_friend = False
  if len(request.user.get_profile().friends.filter(id=user_profile_id))!= 0:
    is_friend = True

  allAddFriendRequests = AddFriendRequest.objects.filter(requestee=user)
  return render(request, 'eventdapp/user.html', {
    'username': username,
    'events': events,
    "user_id":user_id,
    'is_own': (request.user.id == int(user_id)), 
    'is_not_own_not_friend': ((request.user.id != int(user_id)) and (is_friend == False) and (is_added == False)),
    'allAddFriendRequests': allAddFriendRequests,
  })  

def add_friend(request, user_id):
  user = User.objects.get(pk=user_id)
  
  addFriendRequest = AddFriendRequest()
  addFriendRequest.requester = request.user
  addFriendRequest.requestee = user
  addFriendRequest.save()
  return view_user(request, user.id)

def invite_friend(request, event_id):
  request_event_id = event_id
  user = request.user
  
  friendProfileList = user.get_profile().friends.all()
  friendList = []
  
  if friendProfileList.exists():
    for friendProfile in friendProfileList:    
      attendence = Attendence.objects.filter(event_id=event_id,participant=friendProfile.user)[:1]  
      if len(attendence)!=0:
        pass
      else:
        friendList.append(friendProfile.user)
        # if friendList == []:
          # friendList = friendProfile.user
          # import pdb;pdb.set_trace()
        # else:
          # friendList = friendList | friendProfile.user   
  return render(request, 'eventdapp/friend_list.html',{
    'friendList' : friendList,
    'event_id' : request_event_id,
  })


def select_friend(request, event_id):
  event = Event.objects.get(pk=event_id)
  
  if request.method == "POST":
    selected_friendList = request.POST.getlist('friendList')
    for selected_friend_id in selected_friendList:
      selected_friend = User.objects.get(pk=selected_friend_id)     
      attendence = Attendence()
      attendence.participant = selected_friend
      attendence.event = event
      isInvited = True
      attendence.save()
      
  return HttpResponseRedirect(("../../{}").format(event_id))

def respond_addFriendRequest(request, result, request_id):
  addFriendRequest = AddFriendRequest.objects.get(pk=request_id)
  if result == 'confirm':
    requester = addFriendRequest.requester
    requestee = addFriendRequest.requestee
    requester.get_profile().friends.add(requestee.get_profile())
    requestee.get_profile().friends.add(requester.get_profile())  
  elif result == 'ignore':
    pass
  addFriendRequest.delete()
  return view_user(request, request.user.id)
  
#three participation views: attend, maybe attend, not attend
def attend_event(request, event_id, is_going):
  event = Event.objects.get(pk=event_id)
  attendence = Attendence.objects.filter(event__pk = event.id, participant__pk = request.user.id)
  user = request.user

  is_going = is_going.upper()
  if not Attendence.is_valid_participation(is_going):
    raise Http404

  if not attendence.exists():
    new_attendence = Attendence()
    new_attendence.participant = user
    new_attendence.event = event
    new_attendence.participation = is_going
    new_attendence.save()    
  elif attendence.exists():
    att = attendence[0]
    att.participation = is_going
    att.save()
    
  return HttpResponseRedirect(("../../../{}").format(event.id))

def search_event(request):  
  if request.method == "POST":
    what = request.POST.get("what")
    lng = request.POST.get("lng")
    lat = request.POST.get("lat")
    tempEvent = None
    
    if what == 'what'and (lng == 'lng') and (lat == 'lat'):
      pass
    if what == 'what'and (lng != 'lng') and (lat != 'lat'):
      # where != None --> filter out locations
      lng = float(lng)
      lat = float(lat)
      tempRawEvent = Event.objects.raw('SELECT id,(3959 * acos( cos( radians(%s) ) * cos( radians( place_latitude ) ) * cos( radians( place_longitude ) - radians(%s) ) + sin( radians(%s) ) * sin( radians( place_latitude ) ) ) ) AS distance FROM eventdapp_event HAVING distance < 25 ORDER BY distance LIMIT 0 , 20',[lat,lng,lat])
      for event in tempRawEvent:
        if tempEvent == None:
          tempEvent = Event.objects.filter(pk=event.id)
        else:
          tempEvent = tempEvent | Event.objects.filter(pk=event.id)
    if (what != 'what') and (lng == 'lng') and (lat == 'lat'):
      # filter what; filter from event title or description
      what_tokens = what.split()
      # filter "what" from event title
      tempEvent1 = Event.objects.all()
      for token in what_tokens:
        tempEvent1 = tempEvent1.filter(title__icontains=token)
      # filter "what" from event desription
      tempEvent2 = Event.objects.all()
      for token in what_tokens:
        tempEvent2 = tempEvent2.filter(description__icontains=token)
      # all candidate events
      tempEvent = tempEvent1 | tempEvent2      
    if (what != 'what') and (lng != 'lng') and (lat != 'lat'):
      # where != None --> filter out locations
      lng = float(lng)
      lat = float(lat)
      tempRawEvent = Event.objects.raw('SELECT id,(3959 * acos( cos( radians(%s) ) * cos( radians( place_latitude ) ) * cos( radians( place_longitude ) - radians(%s) ) + sin( radians(%s) ) * sin( radians( place_latitude ) ) ) ) AS distance FROM eventdapp_event HAVING distance < 25 ORDER BY distance LIMIT 0 , 20',[lat,lng,lat])
      for event in tempRawEvent:
        if tempEvent == None:
          tempEvent = Event.objects.get(pk=event.id)
        else:
          tempEvent = tempEvent | Event.objects.get(pk=event.id)
      # filter what; filter from event title or description
      what_tokens = what.split()
      # filter "what" from event title
      tempEvent1 = tempEvent
      for token in what_tokens:
        tempEvent1 = tempEvent1.filter(title__icontains=token)
      # filter "what" from event desription
      tempEvent2 = tempEvent
      for token in what_tokens:
        tempEvent2 = tempEvent2.filter(description__icontains=token)
      # all candidate events
      tempEvent = tempEvent1 | tempEvent2  
      
    return render(request, 'eventdapp/search_result_event.html',{
      'tempEvent' : tempEvent,
    })
   
    
  else:
    return render(request, 'eventdapp/search_result_event.html',{
    })


