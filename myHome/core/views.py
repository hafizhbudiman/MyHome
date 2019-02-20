from django.db import transaction
from django.shortcuts import get_object_or_404

from rest_framework import generics, mixins, viewsets
from rest_framework.decorators import action
from rest_framework.response import Response

from core.models import Door, DoorLog, ElectricityAccount, Lamp, Token
from core.serializers import (
    DoorSerializer,
    DoorLogSerializer,
    ElectricityAccountSerializer,
    LampSerializer,
    TokenSerializer,
)


class DoorLogViewSet(mixins.ListModelMixin, viewsets.GenericViewSet):
    queryset = DoorLog.objects.all()
    serializer_class = DoorLogSerializer

    @transaction.atomic
    def create(self, request, *args, **kwargs):
        door = get_object_or_404(Door, pk=request.data['id'])
        log = DoorLog.objects.create(door=door)
        
        return Response(DoorLogSerializer(log).data)

class DoorViewSet(viewsets.ModelViewSet):
    queryset = Door.objects.all()
    serializer_class = DoorSerializer

    @action(methods=['post'], detail=True)
    def lock_unlock(self, request, pk):
        door = get_object_or_404(Door, pk=pk)
        door.locked = not(door.locked)
        door.save()

        return Response(DoorSerializer(door).data)


class ElectricityAccountViewSet(viewsets.ModelViewSet):
    queryset = ElectricityAccount.objects.all()
    serializer_class = ElectricityAccountSerializer


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
    def use_token(self, request):
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
