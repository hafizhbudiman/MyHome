from django.contrib import admin
from django.urls import include, path

urlpatterns = [
    path('api-auth/', include('rest_framework.urls')),
    path('', include('core.urls')),
    path('admin/', admin.site.urls),
]