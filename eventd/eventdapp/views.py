from django import forms
from django.contrib.auth.models import User
from django.contrib.auth.forms import UserCreationForm
from django.forms import ModelForm
from django.http import Http404, HttpResponseRedirect
from django.shortcuts import render, render_to_response
from django.template import RequestContext
from eventdapp.models import Event
from eventdapp.models import UserProfile
from eventdapp.models import Attendence

class CustomUserCreationForm(UserCreationForm):
  GENDERS = (('',''), ("M","M"), ("F","F"))

  email = forms.EmailField(required=True)
  gender = forms.ChoiceField(required=False, choices=GENDERS)
  phone_number = forms.CharField(required=False)
  location = forms.CharField(required=False)
  dob = forms.DateField(required=False)
  photo = forms.ImageField(required=False)
 
  class Meta:
    model = User
    fields = ("username", "email", "password1", "password2", 'gender',
              'phone_number', 'location', 'dob', 'photo')
 
  def save(self, commit=True):
    user = super(CustomUserCreationForm, self).save(commit=False)
    user.email = self.cleaned_data["email"]

    user_profile = UserProfile()
    user_profile.gender = self.cleaned_data["gender"]
    user_profile.phone_number = self.cleaned_data["phone_number"]
    user_profile.location = self.cleaned_data["location"]
    user_profile.dob = self.cleaned_data["dob"]
    user_profile.photo = self.cleaned_data["photo"]

    if commit:
      user.save()
      user_profile.user = user
      user_profile.save()
    return user

class EventForm(ModelForm):
  class Meta:
    model = Event
    exclude = ('owner',)

  def __init__(self, *args, **kwargs):
    self.request = kwargs.pop("request")
    super(ModelForm, self).__init__(*args, **kwargs)

  def save(self, commit=True):
    event = super(ModelForm, self).save(commit=False)
    event.owner = self.request.user

    if commit:
      event.save()
    return event

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

  own_events = Event.objects.filter(owner=user)
  participation_event_ids = Attendence.objects.filter(participant=user).values_list('event_id')
  participation_events = Event.objects.filter(id__in=participation_event_ids)
  events = own_events | participation_events

  return render(request, 'eventdapp/user.html', {
    'username': username,
    'events': events,
    'is_own': (request.user.id == int(user_id)),
  })  

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

