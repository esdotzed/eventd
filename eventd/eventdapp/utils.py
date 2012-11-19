MOBILE_AGENT = 'MobileEventd'

def is_mobile(request):
  return request.META['HTTP_USER_AGENT'] == MOBILE_AGENT

