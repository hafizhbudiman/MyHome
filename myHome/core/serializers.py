from rest_framework import serializers
from django.contrib.auth.models import User
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


class OwnerSerializer(serializers.ModelSerializer):
    class Meta:
        model = User
        fields = ('username',)


class DoorLogSerializer(serializers.ModelSerializer):
    class Meta:
        model = DoorLog
        fields = ('id', 'door', 'created_at')


class DoorSerializer(serializers.ModelSerializer):
    owner = OwnerSerializer()

    class Meta:
        model = Door
        fields = ('id', 'owner', 'house_id', 'locked')


class ElectricityAccountSerializer(serializers.ModelSerializer):
    owner = OwnerSerializer()

    class Meta:
        model = ElectricityAccount
        fields = ('id', 'owner', 'balance', 'account_number')


class LampSerializer(serializers.ModelSerializer):
    owner = OwnerSerializer()

    class Meta:
        model = Lamp
        fields = ('id', 'owner', 'house_id', 'on')


class LoginSerializer(serializers.ModelSerializer):
    class Meta:
        model = User
        fields = ('username', 'password')
        

class TokenSerializer(serializers.ModelSerializer):
    class Meta:
        model = Token
        fields = ('id', 'code', 'balance', 'used')


class UserSerializer(serializers.ModelSerializer):
    class Meta:
        model = User
        fields = ('id', 'username', 'email')


class NotificationSerializer(serializers.ModelSerializer):
    class Meta:
        model = Notification
        fields = ('id', 'tipe', 'nominal', 'owner')


class UserTokenSerializer(serializers.ModelSerializer):
    class Meta:
        model = UserToken
        fields = '__all__'


class UserDetailSerializer(serializers.ModelSerializer):
    username = serializers.CharField(source='user.username')
    email = serializers.CharField(source='user.email')    

    class Meta:
        model = UserProfile
        fields = ('email', 'username', 'name', 'phone', 'address')
