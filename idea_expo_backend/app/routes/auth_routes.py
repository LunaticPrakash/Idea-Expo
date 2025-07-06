from flask import request, jsonify
from ..db import db
from ..db.models import User
def register_auth_routes(app):
    
    @app.route(rule="/api/auth/register",methods=["POST"])
    def register_user():
        req_body = request.json
        first_name = req_body.get('first_name')
        last_name = req_body.get('last_name')
        email = req_body.get('email')
        password = req_body.get('password')

        if User.query.filter_by(email=email).first():
            return jsonify({"error": "Email already registered"}), 409
        user = User(first_name, last_name, email, password)
        db.session.add(user)
        db.session.commit()

        return jsonify({"message": "User registered successfully"}), 201

    @app.route(rule="/api/auth/login",methods=["POST"])
    def login_user():
        req_body = request.json
        return jsonify(req_body)
