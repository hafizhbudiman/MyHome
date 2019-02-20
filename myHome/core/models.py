from django.db import models
from django.utils.timezone import now


class User(models.Model):
    email = models.CharField(max_length=50, unique=True)
    password = models.CharField(max_length=50)

    def __str__(self):
        return f'{self.id}'


class Lamp(models.Model):
    house_id = models.IntegerField()
    on = models.BooleanField(default=False)
    owner = models.ForeignKey('User', related_name='lamps', on_delete=models.CASCADE)

    def __str__(self):
        return f'{self.id}'


class Door(models.Model):
    house_id = models.IntegerField()
    locked = models.BooleanField(default=False)
    owner = models.ForeignKey('User', related_name='doors', on_delete=models.CASCADE)

    def __str__(self):
        return f'{self.id}'


class Token(models.Model):
    code = models.CharField(max_length=200, unique=True)
    balance = models.IntegerField()
    used = models.BooleanField(default=False)

    def __str__(self):
        return f'{self.id}'


class ElectricityAccount(models.Model):
    balance = models.IntegerField(default=0)
    account_number = models.CharField(max_length=12)
    owner = models.ForeignKey('User', related_name='e_account', on_delete=models.CASCADE)

    def __str__(self):
        return f'{self.id}'


class DoorLog(models.Model):
    door = models.ForeignKey(
        'Door',
        on_delete=models.CASCADE,
        related_name='door')
    created_at = models.DateTimeField(default=now, editable=False)
    
    def __str__(self):
        return f'{self.id}'
