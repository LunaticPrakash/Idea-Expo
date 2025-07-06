from .auth_routes import register_auth_routes

def register_routes(app):
    register_auth_routes(app)