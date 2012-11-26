MOBILE_AGENT = 'MobileEventd'

def is_mobile(request):
  return request.META['HTTP_USER_AGENT'] == MOBILE_AGENT

def is_valid_latlng(lat, lng):
  return (lat != '' and lat != u'' and lat != None) and \
         (lng != '' and lng != u'' and lng != None)
         
