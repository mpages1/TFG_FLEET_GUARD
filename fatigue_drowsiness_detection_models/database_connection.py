__author__ = 'mpages1'

import pandas as pd
import sqlalchemy


def get_engine():
    engine = sqlalchemy.create_engine('postgresql+psycopg2://postgres:admin123@host.docker.internal:5432/TFG')
    return engine


def get_tachograph_data():
    engine = get_engine()
    query = """
    SELECT *
    FROM tachograph_data
    """
    try:
        with engine.connect() as connection:
            return pd.read_sql(query, connection)
    except Exception as e:
        print(f"Error getting tachograph data: {e}")
        return pd.DataFrame()


def get_camera_data():
    engine = get_engine()
    query = "SELECT * FROM camera_data"
    data = pd.read_sql(query, engine, parse_dates=['data'], index_col='data')
    return data
