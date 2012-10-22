from django import forms
from django.contrib.auth.models import User
from django.contrib.auth.forms import UserCreationForm
from django.forms import ModelForm
from django.http import HttpResponseRedirect
from django.shortcuts import render, render_to_response
from django.template import RequestContext
from eventdapp.models import Event
from eventdapp.models import UserProfile

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
    event.owner = self.request.user.get_profile()

    if commit:
      event.save()
    return event

def homepage(request):
  if not request.user.is_authenticated():
    return HttpResponseRedirect("/login/")
  username = request.user.username
  return render_to_response('eventdapp/homepage.html', {'username':username})

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

def create_event(request):
  if not request.user.is_authenticated():
    return HttpResponseRedirect("/login/")
  if request.method == 'POST':
    form = EventForm(request.POST, request.FILES, request=request)
    if form.is_valid():
      new_event = form.save()
      return HttpResponseRedirect("/")
  else:
    form = EventForm(request=request)

  return render(request, 'eventdapp/event_form.html', {
    'form': form,
  })

