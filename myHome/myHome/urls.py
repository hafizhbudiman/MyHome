from django.contrib import admin
from django.urls import path
from django.conf.urls import include


urlpatterns = [
    path('api-auth/', include('rest_framework.urls')),
    path('', include('core.urls')),
]