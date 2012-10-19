from django.conf.urls import patterns, include, url
from django.contrib.auth.views import login, logout

# Uncomment the next two lines to enable the admin:
from django.contrib import admin
admin.autodiscover()

urlpatterns = patterns('',
    url(r'^$', 'eventdapp.views.homepage'),
    url(r'login/$', login, {'template_name': 'eventdapp/login.html'}),
    url(r'logout/$', logout, {'next_page': '..'}),
    url(r'register/$', 'eventdapp.views.register'),
)
