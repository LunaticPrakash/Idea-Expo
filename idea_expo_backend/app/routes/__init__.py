from flask import jsonify

def register_routes(app):
    @app.route('/')
    def hello():
        return jsonify({"message": "Hello from Idea Expo backend!"})