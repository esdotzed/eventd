from eventdapp.models import UserProfile, Event, Attendence
from django.contrib import admin
from django.contrib.auth.admin import UserAdmin
from django.contrib.auth.models import User

class UserProfileInline(admin.StackedInline):
  model = UserProfile
  can_delete = False
  verbose_name_plural = 'profile'

class UserAdmin(UserAdmin):
  inlines = (UserProfileInline,)

admin.site.unregister(User)
admin.site.register(User, UserAdmin)
admin.site.register(UserProfile)
admin.site.register(Event)
admin.site.register(Attendence)
