from flask import jsonify
from .base_exception import BaseException
from http import HTTPStatus
from .jwt_exception_handler import register_jwt_error_handlers

def register_error_handlers(app, jwt):

    register_jwt_error_handlers(app, jwt)

    @app.errorhandler(BaseException)
    def handle_app_exception(error):
        response = jsonify(error.to_dict())
        response.status_code = error.status_code
        return response

    @app.errorhandler(400)
    def bad_request(error):
        response =  jsonify({
                "error": "The request could not be understood",
                "status_code": HTTPStatus.BAD_REQUEST,
                "type": "BadRequest"
            })
        response.status_code = HTTPStatus.BAD_REQUEST
        return response

    @app.errorhandler(405)
    def method_not_allowed(error):
        response =  jsonify({
                "error": "The method is not allowed for the requested URL",
                "status_code": HTTPStatus.METHOD_NOT_ALLOWED,
                "type": "MethodNotAllowed"
            })
        response.status_code = HTTPStatus.METHOD_NOT_ALLOWED
        return response
    
    @app.errorhandler(500)
    def internal_server_error(error):
        response =  jsonify({
                "error": "An unexpected server error occurred. Please try again later.",
                "status_code": HTTPStatus.INTERNAL_SERVER_ERROR,
                "type": "InternalServerError"
            })
        response.status_code = HTTPStatus.INTERNAL_SERVER_ERROR
        return response