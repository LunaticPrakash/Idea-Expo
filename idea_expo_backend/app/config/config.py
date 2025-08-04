class BaseConfig:

    # Database
    SQLALCHEMY_DATABASE_URI = "sqlite:///idea_expo_db.sqlite3"
    SQLALCHEMY_TRACK_MODIFICATIONS = False

    # jwt
    JWT_SECRET_KEY = "IdeaExpo123"
    JWT_ACCESS_TOKEN_EXPIRES = 3600  # 1 hour in seconds
    JWT_REFRESH_TOKEN_EXPIRES = 2592000 # 30 days in seconds


class DevConfig(BaseConfig):
    pass

class QaConfig(BaseConfig):
    pass

class ProdConfig(BaseConfig):
    pass
