from http import HTTPStatus 

class BaseException(Exception):

    def __init__(self, status_code: int=HTTPStatus.INTERNAL_SERVER_ERROR, message:str="Internal Server Error"):
        super().__init__(message)
        self.message = message
        self.status_code = status_code
    
    def to_dict(self):
        return {
                "error": self.message,
                "status_code": self.status_code,
                "type": self.__class__.__name__
            }
