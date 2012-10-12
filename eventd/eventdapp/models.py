from django.db import models

class User(models.Model):
  GENDERS = (("M","M"), ("F","F"))

  username = models.CharField(max_length=45, unique=True)
  email = models.CharField(max_length=60, unique=True)
  password = models.CharField(max_length=45)
  phone_number = models.CharField(max_length=20, blank=True)
  location = models.CharField(max_length=100, blank=True)
  dob = models.DateField(null=True, blank=True)
  gender = models.CharField(max_length=8, blank=True)
  photo = models.ImageField(upload_to='user_photos', null=True, blank=True)
  first_name = models.CharField(max_length=45, blank=True)
  last_name = models.CharField(max_length=45, blank=True)
  friends = models.ManyToManyField('self', null=True, blank=True)

  def __unicode__(self):
    return self.username

