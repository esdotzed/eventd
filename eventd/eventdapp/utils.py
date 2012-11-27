MOBILE_AGENT = 'MobileEventd'

def is_mobile(request):
  return request.META['HTTP_USER_AGENT'] == MOBILE_AGENT

def is_valid_latlng(lat, lng):
  return (lat != '' and lat != u'' and lat != None) and \
         (lng != '' and lng != u'' and lng != None)

def ext_from(request):
  return 'xml' if is_mobile(request) else 'html'

def content_type_from(request):
  return 'text/%s' % ext_from(request)

