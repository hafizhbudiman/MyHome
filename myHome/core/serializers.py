from rest_framework import serializers
from core.models import Door, OpenDoorLog, Electricity, Lamp, Token


class OpenDoorLogSerializer(serializers.ModelSerializer):
    class Meta:
        model = OpenDoorLog
        fields = ('id', 'door', 'created_at', 'authorized')


class DoorSerializer(serializers.ModelSerializer):
    logs = OpenDoorLogSerializer(source='door', many=True)

    class Meta:
        model = Door
        fields = ('id', 'house_id', 'logs')


class ElectricitySerializer(serializers.ModelSerializer):
    class Meta:
        model = Electricity
        fields = ('id', 'balance', 'account_number')


class LampSerializer(serializers.ModelSerializer):
    class Meta:
        model = Lamp
        fields = ('id', 'house_id')


class TokenSerializer(serializers.ModelSerializer):
    class Meta:
        model = Token
        fields = ('id', 'code', 'balance', 'used')