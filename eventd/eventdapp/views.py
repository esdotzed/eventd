from django import forms
from django.contrib.auth.models import User
from django.contrib.auth.forms import UserCreationForm
from django.http import HttpResponseRedirect
from django.shortcuts import render_to_response
from django.template import RequestContext
from eventdapp.models import UserProfile

class CustomUserCreationForm(UserCreationForm):
  GENDERS = (('',''), ("M","M"), ("F","F"))

  email = forms.EmailField(required=True)
  gender = forms.ChoiceField(required=False, choices=GENDERS)
 
  class Meta:
    model = User
    fields = ("username", "email", "password1", "password2", 'gender')
 
  def save(self, commit=True):
    user = super(CustomUserCreationForm, self).save(commit=False)
    user.email = self.cleaned_data["email"]

    user_profile = UserProfile()
    if self.cleaned_data["gender"] != '':
      user_profile.gender = self.cleaned_data["gender"]

    if commit:
      user.save()
      user_profile.user = user
      user_profile.save()
    return user

def test(request):
  username = "NoAuth" 
  if request.user.is_authenticated():
    username = request.user.get_profile().gender
  return render_to_response('eventdapp/test.html', {'username':username})

def register(request):
  if request.method == 'POST':
    form = CustomUserCreationForm(request.POST)
    if form.is_valid():
      new_user = form.save()
      return HttpResponseRedirect("/test/")
  else:
    form = CustomUserCreationForm()
  return render_to_response("eventdapp/register.html", {
    'form': form,
  }, context_instance=RequestContext(request))

