from django.db import models
from django.contrib.auth.models import User

class UserProfile(models.Model):
  GENDERS = (("M","M"), ("F","F"))

  phone_number = models.CharField(max_length=20, blank=True)
  location = models.CharField(max_length=100, blank=True)
  dob = models.DateField(null=True, blank=True)
  gender = models.CharField(max_length=8, blank=True, choices=GENDERS)
  photo = models.ImageField(upload_to='user_photos', null=True, blank=True)
  friends = models.ManyToManyField('self', null=True, blank=True)

  user = models.ForeignKey(User, unique=True, related_name='profile')

  def __unicode__(self):
    return self.user.username

class Event(models.Model):
  CATEGORY_CHOICES = (("Sports","Sports"),("Party","Party"))
  
  title = models.CharField(max_length=100)
  description = models.CharField(max_length=2000)
  photo = models.ImageField(upload_to='event_photos',null=True, blank=True)
  start_time = models.DateTimeField()
  end_time = models.DateTimeField()
  place_text = models.CharField(max_length=200)
  place_longitude = models.FloatField()
  place_latitude = models.FloatField()
  category = models.CharField(max_length=20,choices=CATEGORY_CHOICES)
  owner=models.ForeignKey("UserProfile")
  
  def __unicode__(self):
    return self.title
    
class Attendence(models.Model):
  PARTICIPATION_CHOICES = (("Going","Going"),("Maybe Going","Maybe Going"),("Not Going","Not Going"))
  
  participant = models.ForeignKey("UserProfile")
  event = models.ForeignKey("Event")
  participation = models.CharField(max_length=15, choices=PARTICIPATION_CHOICES)
  isInvited = models.BooleanField()
  
  def __unicode__(self):
    return self.event.title+"attendence"

   