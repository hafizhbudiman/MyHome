from django.http import HttpResponse
from django.shortcuts import get_object_or_404
from rest_framework import viewsets
from rest_framework.decorators import action
from rest_framework.response import Response

from core.decorators import validate_uuid
from core.models import Door, OpenDoorLog, Electricity, Lamp, Token
from core.serializers import (
    DoorSerializer,
    OpenDoorLogSerializer,
    ElectricitySerializer,
    LampSerializer,
    TokenSerializer,
)


class DoorViewSet(viewsets.ModelViewSet):
    queryset = Door.objects.all()
    serializer_class = DoorSerializer

    @action(methods=['post'], detail=True)
    def lock_unlock(self, request, pk):
        door = get_object_or_404(Door, pk=pk)
        door.locked = not(door.locked)
        door.save()

        return Response(DoorSerializer(door).data)

class OpenDoorLogViewSet(viewsets.ModelViewSet):
    queryset = OpenDoorLog.objects.all()
    serializer_class = OpenDoorLogSerializer


class ElectricityViewSet(viewsets.ModelViewSet):
    queryset = Electricity.objects.all()
    serializer_class = Electricity


class LampViewSet(viewsets.ModelViewSet):
    queryset = Lamp.objects.all()
    serializer_class = LampSerializer

    @action(methods=['post'], detail=True)
    def turn_on_off(self, request, pk):
        lamp = get_object_or_404(Lamp, pk=pk)
        lamp.on = not(lamp.on)
        lamp.save()

        return Response(LampSerializer(lamp).data)

class TokenViewSet(viewsets.ModelViewSet):
    queryset = Token.objects.all()
    serializer_class = TokenSerializer