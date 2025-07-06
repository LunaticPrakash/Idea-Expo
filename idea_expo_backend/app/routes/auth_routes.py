from flask import request, jsonify

def register_auth_routes(app):
    
    @app.route(rule="/api/auth/register",methods=["POST"])
    def register_user():
        req_body = request.json
        return jsonify(req_body)

    @app.route(rule="/api/auth/login",methods=["POST"])
    def login_user():
        req_body = request.json
        return jsonify(req_body)
