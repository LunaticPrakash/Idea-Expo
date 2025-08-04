from flask import request, jsonify
from ..services.auth_services import register_user_service, login_user_service

def register_auth_routes(app):

    @app.route(rule="/api/auth/register", methods=["POST"])
    def register_user():
        req_body = request.json
        first_name = req_body.get('first_name', None)
        last_name = req_body.get('last_name', None)
        email = req_body.get('email', None)
        password = req_body.get('password', None)

        user = register_user_service(first_name, last_name, email, password)
        response = jsonify({
            "data": user.to_dict(),
            "message": "User has been registered successfully"
        })
        response.status_code = 201
        return response

    @app.route(rule="/api/auth/login", methods=["POST"])
    def login_user():
        req_body = request.json
        email = req_body.get('email', None)
        password = req_body.get('password', None)
        data = login_user_service(email, password)
        return jsonify(data)
