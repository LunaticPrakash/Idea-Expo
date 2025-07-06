from flask import Flask
from .config import get_config
from .db import db
from .db.models import *
from .routes import register_routes

def create_app():
    app = Flask(__name__)
    app.config.from_object(get_config(env="DEV"))
    db.init_app(app)
    register_routes(app)
    return app