from django.conf.urls import url
from django.urls import path
from rest_framework_nested import routers

from core import views

door_router = routers.SimpleRouter()
door_router.register('doors', views.DoorViewSet, base_name='doors')

electricity_router = routers.SimpleRouter()
electricity_router.register('electricities', views.ElectricityAccountViewSet, base_name='electricities')

lamp_router = routers.SimpleRouter()
lamp_router.register('lamps', views.LampViewSet, base_name='lamps')

token_router = routers.SimpleRouter()
token_router.register('tokens', views.TokenViewSet, base_name='tokens')

door_log_router = routers.SimpleRouter()
door_log_router.register('logs', views.DoorLogViewSet, base_name='logs')

urlpatterns = []
urlpatterns += door_router.urls
urlpatterns += electricity_router.urls
urlpatterns += lamp_router.urls
urlpatterns += token_router.urls
urlpatterns += door_log_router.urls
