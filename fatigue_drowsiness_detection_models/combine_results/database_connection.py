__author__ = 'mpages1'

import sqlalchemy


def get_engine():
    engine = sqlalchemy.create_engine('postgresql+psycopg2://postgres:admin123@host.docker.internal:5432/TFG')
    return engine
