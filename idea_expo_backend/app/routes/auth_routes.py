from flask import request, jsonify
from ..services.auth_services import register_user_service, login_user_service
from flask_jwt_extended import jwt_required, get_jwt_identity, create_access_token

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

    @app.route('/api/auth/refresh', methods=['POST'])
    @jwt_required(refresh=True)
    def refresh_token():
        current_user_id = get_jwt_identity()
        new_access_token = create_access_token(identity=current_user_id)
        
        return jsonify(access_token=new_access_token)


    @app.route('/api/user/', methods=['GET'])
    def hello():
        
        return jsonify({"message":"Hi"})