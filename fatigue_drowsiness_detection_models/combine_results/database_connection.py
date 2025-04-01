__author__ = 'mpages1'

import sqlalchemy


def get_engine():
    engine = sqlalchemy.create_engine('postgresql://postgres:admin123@host.docker.internal:33333/FLEET_GUARD')
    print("Connecting to:", engine.url)
    return engine

