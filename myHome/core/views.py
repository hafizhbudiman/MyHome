from django.contrib.auth.models import User
from django.db import transaction
from django.http import HttpResponse
from django.shortcuts import get_object_or_404

from rest_framework import generics, mixins, viewsets, permissions
from rest_framework.decorators import action
from rest_framework.response import Response

from core.models import Door, DoorLog, ElectricityAccount, Lamp, Token
from core.serializers import (
    DoorLogSerializer,
    DoorSerializer,
    ElectricityAccountSerializer,
    LampSerializer,
    TokenSerializer,
    UserSerializer,
)


class DoorLogViewSet(mixins.ListModelMixin, mixins.RetrieveModelMixin, viewsets.GenericViewSet):
    queryset = DoorLog.objects.all()
    serializer_class = DoorLogSerializer

    @transaction.atomic
    def create(self, request, *args, **kwargs):
        user = User.objects.filter(username=request.data['username']).first()
        if user is None:
            return Response('User With Username ' + request.data['username'] + ' Not Found')
        door = Door.objects.filter(house_id=request.data['id'], owner=user).first()
        log = DoorLog.objects.create(door=door)
        
        return Response(DoorLogSerializer(log).data)


class DoorViewSet(mixins.ListModelMixin, mixins.RetrieveModelMixin, viewsets.GenericViewSet):
    queryset = Door.objects.all()
    serializer_class = DoorSerializer
    permission_classes = (permissions.IsAuthenticated,)

    @action(methods=['post'], detail=True)
    def lock_unlock(self, request, pk):
        door = get_object_or_404(Door, pk=pk)
        door.locked = not(door.locked)
        door.save()

        return Response(DoorSerializer(door).data)


class ElectricityAccountViewSet(viewsets.ModelViewSet):
    queryset = ElectricityAccount.objects.all()
    serializer_class = ElectricityAccountSerializer
    permission_classes = (permissions.IsAuthenticated,)


class LampViewSet(mixins.ListModelMixin, mixins.RetrieveModelMixin, viewsets.GenericViewSet):
    queryset = Lamp.objects.all()
    serializer_class = LampSerializer
    permission_classes = (permissions.IsAuthenticated,)

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
        account = ElectricityAccount.objects.filter(account_number=request.data['account_number']).first()
        token = Token.objects.filter(code=request.data['code']).first()
        
        if account is None or token is None:
            return Response('Electricity Account Or Token Not Found')
        else:
            account.balance += token.balance
            token.used = True
            account.save()
            token.save()

        return Response(ElectricityAccountSerializer(account).data)


class UserViewSet(mixins.ListModelMixin, mixins.RetrieveModelMixin, viewsets.GenericViewSet):
    queryset = User.objects.all()
    serializer_class = UserSerializer

    @transaction.atomic
    def create(self, request, *args, **kwargs):
        user = User.objects.create_user(username=request.data['username'], email=request.data['email'], password=request.data['password'])
        door = Door.objects.create(owner=user, house_id=1)

        for i in range(3):
            lamp = Lamp.objects.create(owner=user, house_id=i+1)

        return Response(UserSerializer(user).data)
