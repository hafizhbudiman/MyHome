from django.conf import settings
from django.contrib.auth.models import User
from django.contrib.auth import authenticate
from django.db import transaction
from django.http import JsonResponse
from django.shortcuts import get_object_or_404

from rest_framework import generics, mixins, viewsets, permissions
from rest_framework.decorators import action
from rest_framework.response import Response

from core.models import (
    Door,
    DoorLog,
    ElectricityAccount,
    Lamp,
    Notification,
    Token,
    UserProfile,
    UserToken,
)
from core.serializers import (
    DoorLogSerializer,
    DoorSerializer,
    ElectricityAccountSerializer,
    LampSerializer,
    LoginSerializer,
    NotificationSerializer,
    OwnerSerializer,
    TokenSerializer,
    UserDetailSerializer,
    UserSerializer,
    UserTokenSerializer,
)

from pyfcm import FCMNotification


class DoorLogViewSet(mixins.ListModelMixin, mixins.RetrieveModelMixin, viewsets.GenericViewSet):
    queryset = DoorLog.objects.all()
    serializer_class = DoorLogSerializer

    @transaction.atomic
    def create(self, request, *args, **kwargs):
        user = User.objects.filter(username=request.data['username']).first()
        if user is None:
            return Response('User With Username ' + request.data['username'] + ' Not Found')
            
        door = Door.objects.filter(house_id=request.data['id'], owner=user).first()
        if door is None:
            return Response({'success': False})
        log = DoorLog.objects.create(door=door)
        
        return Response({'success': True})


class DoorViewSet(mixins.ListModelMixin, viewsets.GenericViewSet):
    serializer_class = DoorSerializer
    # permission_classes = (permissions.IsAuthenticated,)

    def get_queryset(self):
        queryset = Door.objects.all()
        username = self.request.query_params.get('username', None)
        if username:
            queryset = queryset.filter(owner__username=username)
        return queryset

    @action(methods=['post'], detail=False)
    def lock_unlock(self, request):
        user = User.objects.filter(username=request.data['username']).first()
        if user is None:
            return Response({'success': False})
        
        door = Door.objects.filter(owner__username=user.username, house_id=request.data['id']).first()
        if door is None:
            return Response({'success': False})
        else:
            print(door.locked)
            door.locked = not(door.locked)
            print(door.locked)
            door.save()

        return Response({'success': True})


class ElectricityAccountViewSet(viewsets.ModelViewSet):
    queryset = ElectricityAccount.objects.all()
    serializer_class = ElectricityAccountSerializer
    # permission_classes = (permissions.IsAuthenticated,)


class LampViewSet(mixins.ListModelMixin, viewsets.GenericViewSet):
    serializer_class = LampSerializer
    # permission_classes = (permissions.IsAuthenticated,)

    def get_queryset(self):
        queryset = Lamp.objects.all()
        username = self.request.query_params.get('username', None)
        if username:
            queryset = queryset.filter(owner__username=username)
        return queryset

    @action(methods=['post'], detail=False)
    def turn_on_off(self, request):
        user = User.objects.filter(username=request.data['username']).first()
        if user is None:
            return Response({'success': False})
        
        lamp = Lamp.objects.filter(owner__username=user.username, house_id=request.data['id']).first()
        if lamp is None:
            return Response({'success': False})
        else:
            lamp.on = not(lamp.on)
            lamp.save()

        return Response({'success': True})

    @action(methods=['post'], detail=False)
    def turn_off_all(self, request):
        user = User.objects.filter(username=request.data['username']).first()
        if user is None:
            return Response({'success': False})
        
        lamps = Lamp.objects.filter(owner__username=user.username)
        if lamps is None:
            return Response({'success': False})
        
        for lamp in lamps:
            lamp.on = False
            lamp.save()
        
        doors = Door.objects.filter(owner__username=user.username)
        if doors is None:
            return Response({'success': False})
        
        for door in doors:
            door.locked = True
            door.save()
        
        return Response({'success': True})


class TokenViewSet(viewsets.ModelViewSet):
    queryset = Token.objects.all()
    serializer_class = TokenSerializer

    @transaction.atomic
    @action(methods=['post'], detail=False)
    def use_token(self, request):
        account = ElectricityAccount.objects.filter(account_number=request.data['account_number']).first()
        token = Token.objects.filter(code=request.data['code']).first()
        
        if account is None or token is None:
            return Response({'success': False})
        else:
            account.balance += token.balance
            token.used = True
            account.save()
            token.save()

        return Response({'success': True})


class UserViewSet(mixins.ListModelMixin, mixins.RetrieveModelMixin, viewsets.GenericViewSet):
    queryset = User.objects.all()
    serializer_class = UserSerializer

    @transaction.atomic
    def create(self, request, *args, **kwargs):
        user = User.objects.create_user(username=request.data['username'], email=request.data['email'], password=request.data['password'])
        door = Door.objects.create(owner=user, house_id=1)

        for i in range(3):
            lamp = Lamp.objects.create(owner=user, house_id=i+1)

        userProfile = UserProfile.objects.create(
            user=user,
            name=request.data['name'],
            address=request.data['address'],
            phone=request.data['phone']   
        )

        if request.data['google_id'] is not 'False':
            userProfile.google_id = request.data['google_id']
        userProfile.save()

        return Response({'success': True})

    @action(methods=['post'], detail=False)
    def google_detail(self, request):
        userProfile = UserProfile.objects.filter(google_id=request.data['google_id']).first()

        return Response(OwnerSerializer(userProfile.user).data)

    @action(methods=['post'], detail=False)
    def username_detail(self, request):
        userProfile = UserProfile.objects.filter(user__username=request.data['username']).first()
# 
        return Response(UserDetailSerializer(userProfile).data)


class LoginViewSet(viewsets.GenericViewSet):
    serializer_class = LoginSerializer

    def create(self, request, *arg, **kwargs):
        if 'google_id' in request.data:
            user = UserProfile.objects.filter(google_id=request.data['google_id']).first()
            if user is not None:
                return Response({'success': True})

        user = authenticate(username=request.data['username'], password=request.data['password'])        
        if not user:
            return Response({'success': False})
        else:
            return Response({'success': True})

    @action(methods=['post'], detail=False)
    def google(self, request):
        user = UserProfile.objects.filter(google_id=request.data['google_id']).first()
        if user is not None:
            return Response({'success': True})
        else:
            return Response({'success': False})


class NotificationViewSet(mixins.ListModelMixin, viewsets.GenericViewSet):
    serializer_class = NotificationSerializer

    @transaction.atomic
    def create(self, request, *arg, **kwargs):
        user = User.objects.filter(username='rwk').first() # Change username later
        if user is None:
            return Response({'success': False})

        notification = Notification.objects.create(owner=user, tipe=request.data['tipe'])
        if 'nominal' in request.data:
            notification.nominal = request.data['nominal']
        notification.save()
        
        userTokens = UserToken.objects.filter(user=user)
        push_service = FCMNotification(api_key=getattr(settings, 'API_KEY', None))
        for userToken in userTokens:
            message_title = ''
            message_body = ''
            
            if 'nominal' in request.data:
                message_title = 'Token Reward'
                message_body = 'You have received IDR ' + str(request.data['nominal']) + ',- token reward.'
            else:
                message_title = 'Door Alert'
                message_body = 'Unauthorized door activity detected.'

            result = push_service.notify_single_device(
                registration_id=userToken.token,
                message_title=message_title,
                message_body=message_body
                )

        return Response({'success': True})

    def get_queryset(self):
        queryset = Notification.objects.all()
        username = self.request.query_params.get('username', None)
        if username:
            queryset = queryset.filter(owner__username=username)
        return queryset


class UserTokenViewSet(mixins.ListModelMixin, viewsets.GenericViewSet):
    queryset = UserToken.objects.all()
    serializer_class = UserTokenSerializer

    @transaction.atomic
    def create(self, request, *args, **kwargs):
        user = User.objects.filter(username=request.data['username']).first()
        if user is None:
            return Response({'success': False})
            
        userToken = UserToken.objects.create(user=user, token=request.data['token'])

        return Response(UserTokenSerializer(userToken).data)