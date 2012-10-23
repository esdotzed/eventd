from django.conf.urls import patterns, include, url
from django.contrib.auth.views import login, logout

# Uncomment the next two lines to enable the admin:
from django.contrib import admin
admin.autodiscover()

urlpatterns = patterns('',
    url(r'^$', 'eventdapp.views.view_own_homepage'),
    url(r'login/$', login, {'template_name': 'eventdapp/login.html'}),
    url(r'logout/$', logout, {'next_page': '..'}),
    url(r'register/$', 'eventdapp.views.register'),
    url(r'event/(?P<event_id>\d+)/$', 'eventdapp.views.view_event'),
    url(r'event/create/$', 'eventdapp.views.create_event'),
    url(r'event/delete/(?P<event_id>\d+)/$', 'eventdapp.views.delete_event'),
    url(r'event/edit/(?P<event_id>\d+)/$', 'eventdapp.views.edit_event'),
    url(r'user/(?P<user_id>\d+)/$','eventdapp.views.view_user'),
)
