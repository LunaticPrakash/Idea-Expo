import re
from flask import request, current_app
from flask_jwt_extended import verify_jwt_in_request
import logging

def register_security_matcher(app):
    @app.before_request
    def security_matcher():
        if request.endpoint is None:
            return

        path = request.path
        method = request.method
        
        # 1. Check if the path is explicitly public
        for pattern, method_to_match, _ in current_app.config["PUBLIC_URL_PATTERNS"]:
            if pattern.match(path) and method == method_to_match:
                return # It's public, no JWT required

        # 2. Check if the path requires a refresh token
        for pattern, method_to_match in current_app.config["REFRESH_TOKEN_ENDPOINTS"]:
            if pattern.match(path) and method == method_to_match:
                try:
                    verify_jwt_in_request(refresh=True) # Expect a refresh token
                    return # Refresh token verified, proceed
                except Exception as e:
                    raise e

        # 3. If not public and not a refresh token endpoint, it must be a protected access token endpoint
        try:
            verify_jwt_in_request() # Expect a standard access token
        except Exception as e:
            raise e
