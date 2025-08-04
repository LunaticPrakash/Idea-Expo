from .base_exception import BaseException
from http import HTTPStatus 

class UserAlreadyExistException(BaseException):
    def __init__(self, status_code:int=HTTPStatus.CONFLICT, message:str="User already exists."):
        super().__init__(status_code, message)

class UserNotFoundException(BaseException):
    def __init__(self, status_code:int=HTTPStatus.NOT_FOUND, message:str="User doesn't exists."):
        super().__init__(status_code, message)

class InvalidCredentialException(BaseException):
    def __init__(self, status_code:int=HTTPStatus.UNAUTHORIZED, message:str="Invalid Username/Password"):
        super().__init__(status_code, message)