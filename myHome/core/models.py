from django.db import models
from django.utils.timezone import now


class Lamp(models.Model):
    house_id = models.IntegerField()
    on = models.BooleanField(default=False)
    owner = models.ForeignKey('auth.User', related_name='lamps', on_delete=models.CASCADE)

    def __str__(self):
        return f'{self.id}'


class Door(models.Model):
    house_id = models.IntegerField()
    locked = models.BooleanField(default=False)
    owner = models.ForeignKey('auth.User', related_name='doors', on_delete=models.CASCADE)

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
    owner = models.ForeignKey('auth.User', related_name='e_account', on_delete=models.CASCADE)

    def __str__(self):
        return f'{self.id}'


class DoorLog(models.Model):
    door = models.ForeignKey(
        'Door',
        on_delete=models.CASCADE,
        related_name='logs')
    created_at = models.DateTimeField(default=now, editable=False)
    
    def __str__(self):
        return f'{self.id}'


class UserToken(models.Model):
    user = models.ForeignKey("auth.User", on_delete=models.CASCADE, related_name="tokens")
    token = models.CharField(max_length=128)

    def __str__(self):
        return f'{self.token}'


class Notification(models.Model):
    owner = models.ForeignKey("auth.User", on_delete=models.CASCADE, related_name="notifications")
    tipe = models.IntegerField(default=0)   # 1 = door, 2 = reward
    nominal = models.IntegerField(default=0)


class UserProfile(models.Model):
    user = models.OneToOneField("auth.User", on_delete=models.CASCADE)
    name = models.CharField(max_length=64)
    google_id = models.CharField(max_length=1000)
    address = models.CharField(max_length=128)
    phone = models.CharField(max_length=128)