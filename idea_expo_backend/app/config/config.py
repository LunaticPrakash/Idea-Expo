import re

class BaseConfig:

    # Database
    SQLALCHEMY_DATABASE_URI = "sqlite:///idea_expo_db.sqlite3"
    SQLALCHEMY_TRACK_MODIFICATIONS = False

    # jwt
    JWT_ALGORITHM = "HS256"
    JWT_SECRET_KEY = "65b8c66deb33f9ae118f849353a1304be190874ecc55ab0cb20ea6bcc4c14ed6"
    JWT_ACCESS_TOKEN_EXPIRES = 3600  # 1 hour in seconds
    JWT_REFRESH_TOKEN_EXPIRES = 2592000 # 30 days in seconds

    PUBLIC_URL_PATTERNS = [
        (re.compile(r'^/api/auth/login$'), 'POST', None),       # Public (no token required)
        (re.compile(r'^/api/auth/register$'), 'POST', None),    # Public (no token required)
        (re.compile(r'^/api/user/\d+$'), 'GET', None)           # Public (no token required)
    ]

    # Explicitly list protected endpoints that require a REFRESH token
    REFRESH_TOKEN_ENDPOINTS = [
        (re.compile(r'^/api/auth/refresh$'), 'POST')
    ]


class DevConfig(BaseConfig):
    pass

class QaConfig(BaseConfig):
    pass

class ProdConfig(BaseConfig):
    pass
