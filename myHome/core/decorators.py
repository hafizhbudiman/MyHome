from uuid import UUID

from django.http import HttpResponse
from django.utils.decorators import decorator_from_middleware
from rest_framework import status


class ValidateUUID():
    def process_view(self, request, view_func, view_args, view_kwargs):
        try:
            UUID(view_kwargs['pk'], version=4)
        except ValueError:
            return HttpResponse(status=status.HTTP_404_NOT_FOUND)
        return None


validate_uuid = decorator_from_middleware(ValidateUUID)
