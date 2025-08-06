import re
from flask import request
from flask_jwt_extended import verify_jwt_in_request
import logging

PUBLIC_URL_PATTERNS = [
    (re.compile(r'^/api/auth/login$'), 'POST'),
    (re.compile(r'^/api/auth/register$'), 'POST'),
    (re.compile(r'^/api/user/\d+$'), 'GET')
]

def register_security_matcher(app):
    @app.before_request
    def security_matcher():
        if request.endpoint is None:
            return

        path = request.path
        method = request.method
        
        for pattern, method_to_match in PUBLIC_URL_PATTERNS:
            if pattern.match(path) and method == method_to_match:
                return

        try:
            verify_jwt_in_request()
        except Exception as e:
            raise e