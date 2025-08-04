from .config import DevConfig, QaConfig, ProdConfig


def get_config(env:str):
    if env == "DEV":
        return DevConfig
    elif env == "QA":
        return QaConfig
    elif env == "PROD":
        return ProdConfig