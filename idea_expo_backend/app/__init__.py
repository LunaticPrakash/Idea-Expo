from flask import Flask
from .db import db
from .routes import register_routes
from .config import get_config
import logging
from .exceptions import register_error_handlers
from flask_migrate import Migrate
from flask_jwt_extended import JWTManager
from .config.auth_config import register_security_matcher

env = "DEV"
jwt = JWTManager()

def create_app():
    app = Flask(__name__)
    app.config.from_object(get_config(env=env))
    logging.info(f"Loaded the {env} config")
    db.init_app(app)
    migrate = Migrate(app, db)
    logging.info(f"Flask Migrate has been setup successfully")
    jwt.init_app(app)
    logging.info(f"Flask JWT Extended has been setup successfully")
    register_error_handlers(app, jwt)
    logging.info(f"Error handlers are registered successfully")
    register_routes(app)
    logging.info(f"Routes are registered successfully")
    register_security_matcher(app)
    logging.info(f"Security matcher is registered successfully")
    return app