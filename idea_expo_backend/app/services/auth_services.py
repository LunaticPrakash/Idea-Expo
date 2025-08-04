from ..db.models import User
from ..exceptions.auth_exception import UserAlreadyExistException, UserNotFoundException, InvalidCredentialException
from ..exceptions.base_exception import BaseException
from ..db import db
import logging
from flask_jwt_extended import create_access_token, create_refresh_token
from datetime import timedelta
from ..config import get_config
from flask import current_app

def register_user_service(first_name, last_name, email, password):
    if User.query.filter_by(email=email).first():
        raise UserAlreadyExistException()
    try:
        user = User(first_name, last_name, email, password)
        logging.info(f"Trying to register the user in database.")
        db.session.add(user)
        db.session.commit()
        logging.info(
            f"User with email - {email} has been registered successfully")
        return user
    except Exception as error:
        raise BaseException(message=str(error))


def login_user_service(email, password):
    user = User.query.filter_by(email=email).first()
    if not user:
        logging.warning(f"Login attempt failed for email: {email} - User not found.")
        raise UserNotFoundException()

    if not user.check_password(password):
        logging.warning(f"Login attempt failed for email: {email} - Invalid credentials.")
        raise InvalidCredentialException()

    identity_claims = {
        "user_id": user.user_id,
        "email": user.email,
    }

    access_token = create_access_token(
        identity=user.user_id,
        expires_delta=timedelta(seconds=current_app.config["JWT_ACCESS_TOKEN_EXPIRES"]),
        additional_claims=identity_claims
    )
    refresh_token = create_refresh_token(
        identity=user.user_id,
        expires_delta=timedelta(seconds=current_app.config["JWT_REFRESH_TOKEN_EXPIRES"]),
        additional_claims=identity_claims
    )

    logging.info(f"User {user.email} logged in successfully.")

    return {
        "user_info": user.to_dict(),
        "access_token": access_token,
        "refresh_token": refresh_token,
        "token_type": "Bearer"
    }
