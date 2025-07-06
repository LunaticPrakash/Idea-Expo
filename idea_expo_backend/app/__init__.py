from flask import Flask
from .config import get_config
from .db import db
from .db.models import *

def create_app():
    app = Flask(__name__)
    app.config.from_object(get_config(env="DEV"))
    db.init_app(app)
    return app