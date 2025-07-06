class BaseConfig:

    # Database
    SQLALCHEMY_DATABASE_URI = "sqlite:///db.sqlite3"
    SQLALCHEMY_TRACK_MODIFICATIONS = False

    # Role based authorization
    RBAC_RULES = {
    '/api/admin/*': ['admin'],
    '/api/user/': ['user', 'admin'],
}

class DEVConfig(BaseConfig):

    # Database
    SQLALCHEMY_DATABASE_URI = "sqlite:///db.sqlite3"
    SQLALCHEMY_TRACK_MODIFICATIONS = False

class QAConfig(BaseConfig):

    # Database
    SQLALCHEMY_DATABASE_URI = "sqlite:///db.sqlite3"
    SQLALCHEMY_TRACK_MODIFICATIONS = False

class PRODConfig(BaseConfig):

    # Database
    SQLALCHEMY_DATABASE_URI = "sqlite:///db.sqlite3"
    SQLALCHEMY_TRACK_MODIFICATIONS = False
