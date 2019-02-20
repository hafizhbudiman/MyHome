from rest_framework import serializers

from core.models import Door, DoorLog, ElectricityAccount, Lamp, Token


class DoorLogSerializer(serializers.ModelSerializer):
    class Meta:
        model = DoorLog
        fields = ('id', 'door', 'created_at')


class DoorSerializer(serializers.ModelSerializer):
    # logs = OpenDoorLogSerializer(source='door', many=True)

    class Meta:
        model = Door
        fields = ('id', 'house_id', 'locked')


class ElectricityAccountSerializer(serializers.ModelSerializer):
    class Meta:
        model = ElectricityAccount
        fields = ('id', 'balance', 'account_number')


class LampSerializer(serializers.ModelSerializer):
    class Meta:
        model = Lamp
        fields = ('id', 'house_id', 'on')


class TokenSerializer(serializers.ModelSerializer):
    class Meta:
        model = Token
        fields = ('id', 'code', 'balance', 'used')