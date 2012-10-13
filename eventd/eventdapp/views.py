from django.shortcuts import render_to_response

def test(request):
  username = "NoAuth" 
  if request.user.is_authenticated():
    username = request.user.get_profile().gender
  return render_to_response('eventdapp/test.html', {'username':username})

