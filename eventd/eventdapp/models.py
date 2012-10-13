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

