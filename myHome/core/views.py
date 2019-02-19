from rest_framework import viewsets
from core.models import Door, OpenDoorLog, Electricity, Lamp, Token
from core.serializers import (
    DoorSerializer,
    OpenDoorLogSerializer,
    ElectricitySerializer,
    LampSerializer,
    TokenSerializer,
)
from django.http import HttpResponse

class DoorViewSet(viewsets.ModelViewSet):
    queryset = Door.objects.all()
    serializer_class = DoorSerializer


class OpenDoorLogViewSet(viewsets.ModelViewSet):
    queryset = OpenDoorLog.objects.all()
    serializer_class = OpenDoorLogSerializer


class ElectricityViewSet(viewsets.ModelViewSet):
    queryset = Electricity.objects.all()
    serializer_class = Electricity


class LampViewSet(viewsets.ModelViewSet):
    queryset = Lamp.objects.all()
    serializer_class = LampSerializer


class TokenViewSet(viewsets.ModelViewSet):
    queryset = Token.objects.all()
    serializer_class = TokenSerializer