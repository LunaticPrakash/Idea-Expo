from config import DEVConfig, QAConfig, PRODConfig

def get_config(env:str):
    if env == "DEV":
        return DEVConfig
    elif env == "QA":
        return QAConfig
    elif env == "PROD":
        return PRODConfig
    return None

