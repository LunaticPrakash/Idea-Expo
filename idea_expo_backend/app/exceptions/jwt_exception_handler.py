from flask import jsonify
from http import HTTPStatus
from flask_jwt_extended.exceptions import (
    NoAuthorizationError, InvalidHeaderError, JWTDecodeError, RevokedTokenError,
    FreshTokenRequired, WrongTokenError
)

def register_jwt_error_handlers(app, jwt):
    @app.errorhandler(NoAuthorizationError)
    def handle_no_authorization_error(e):
        response = jsonify({
            "error": "Missing Authorization Header",
            "status_code": HTTPStatus.UNAUTHORIZED,
            "type": "NoAuthorizationError"
        })
        response.status_code = HTTPStatus.UNAUTHORIZED
        return response

    @app.errorhandler(InvalidHeaderError)
    def handle_invalid_header_error(e):
        response = jsonify({
            "error": "Invalid Authorization Header format",
            "status_code": HTTPStatus.UNPROCESSABLE_ENTITY,
            "type": "InvalidHeaderError"
        })
        response.status_code = HTTPStatus.UNPROCESSABLE_ENTITY
        return response

    @app.errorhandler(JWTDecodeError)
    def handle_decode_error(e):
        response = jsonify({
            "error": "Token could not be decoded (e.g., invalid signature or malformed)",
            "status_code": HTTPStatus.UNAUTHORIZED,
            "type": "DecodeError"
        })
        response.status_code = HTTPStatus.UNAUTHORIZED
        return response

    @app.errorhandler(RevokedTokenError)
    def handle_revoked_token_error(e):
        response = jsonify({
            "error": "Token has been revoked",
            "status_code": HTTPStatus.UNAUTHORIZED,
            "type": "RevokedTokenError"
        })
        response.status_code = HTTPStatus.UNAUTHORIZED
        return response

    @app.errorhandler(FreshTokenRequired)
    def handle_fresh_token_required_error(e):
        response = jsonify({
            "error": "Fresh token required for this action",
            "status_code": HTTPStatus.UNAUTHORIZED,
            "type": "FreshTokenRequired"
        })
        response.status_code = HTTPStatus.UNAUTHORIZED
        return response


    @app.errorhandler(WrongTokenError)
    def handle_wrong_token_error(e):
        response = jsonify({
            "error": "Wrong token type provided (e.g., refresh token where access token expected)",
            "status_code": HTTPStatus.UNAUTHORIZED,
            "type": "WrongTokenError"
        })
        response.status_code = HTTPStatus.UNAUTHORIZED
        return response


    @jwt.unauthorized_loader
    def unauthorized_response(callback):
        response = jsonify({
            "error": "Authorization required: Missing or invalid token",
            "status_code": HTTPStatus.UNAUTHORIZED,
            "type": "UnauthorizedError"
        })
        response.status_code = HTTPStatus.UNAUTHORIZED
        return response

    @jwt.invalid_token_loader
    def invalid_token_response(callback):
        response = jsonify({
            "error": "Invalid token: Signature verification failed or malformed",
            "status_code": HTTPStatus.UNAUTHORIZED,
            "type": "InvalidTokenError"
        })
        response.status_code = HTTPStatus.UNAUTHORIZED
        return response

    @jwt.expired_token_loader
    def expired_token_response(callback):
        response = jsonify({
            "error": "Token has expired",
            "status_code": HTTPStatus.UNAUTHORIZED,
            "type": "ExpiredTokenError"
        })
        response.status_code = HTTPStatus.UNAUTHORIZED
        return response

    @jwt.revoked_token_loader
    def revoked_token_response(callback):
        response = jsonify({
            "error": "Token has been revoked",
            "status_code": HTTPStatus.UNAUTHORIZED,
            "type": "RevokedTokenError"
        })
        response.status_code = HTTPStatus.UNAUTHORIZED
        return response

    @jwt.needs_fresh_token_loader
    def needs_fresh_token_response(callback):
        response = jsonify({
            "error": "Fresh token required for this action",
            "status_code": HTTPStatus.UNAUTHORIZED,
            "type": "FreshTokenRequiredError"
        })
        response.status_code = HTTPStatus.UNAUTHORIZED
        return response