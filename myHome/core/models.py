from django.db import models
from django.utils.timezone import now

# Create your models here.
class Lamp(models.Model):
    house_id = models.IntegerField()
    on = models.BooleanField(default=False)

    def __str__(self):
        return f'{self.id}'

class Door(models.Model):
    house_id = models.IntegerField()
    locked = models.BooleanField(default=False)

    def __str__(self):
        return f'{self.id}'

class Token(models.Model):
    code = models.CharField(max_length=200, unique=True)
    balance = models.IntegerField()
    used = models.BooleanField(default=False)

    def __str__(self):
        return f'{self.id}'

class Electricity(models.Model):
    balance = models.IntegerField(default=0)
    account_number = models.CharField(max_length=12)

    def __str__(self):
        return f'{self.id}'

class OpenDoorLog(models.Model):
    door = models.ForeignKey(
        'Door',
        on_delete=models.CASCADE,
        related_name='door')
    created_at = models.DateTimeField(default=now, editable=False)
    authorized = models.BooleanField(default=False)
    
    def __str__(self):
        return f'{self.id}'
