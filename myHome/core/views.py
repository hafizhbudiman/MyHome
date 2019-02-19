from django.db import transaction
from django.http import HttpResponse, Http404
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

    @action(methods=['get'], detail=False)
    def get_last_unauthorized(self, request):
        log = OpenDoorLog.objects.filter(authorized=False).first()
        
        if log is None:
            return Response("Not Found")
        else:
            log.authorized = True
            log.save()
            return Response(OpenDoorLogSerializer(log).data)

class ElectricityViewSet(viewsets.ModelViewSet):
    queryset = Electricity.objects.all()
    serializer_class = ElectricitySerializer


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

    @transaction.atomic
    @action(methods=['post'], detail=False)
    def use(self, request):
        account = Electricity.objects.filter(account_number=request.data['account_number']).first()
        token = Token.objects.filter(code=request.data['code']).first()
        
        if account is None or token is None:
            return Response('Electricity Account Or Token Not Found')
        else:
            account.balance += token.balance
            token.used = True
            account.save()
            token.save()

        return Response('YES')
