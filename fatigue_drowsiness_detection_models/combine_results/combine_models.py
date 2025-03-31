__author__ = 'mpages1'

import os
import threading
import time
from datetime import datetime

import joblib
import numpy as np
from sklearn.ensemble import AdaBoostClassifier
from sklearn.preprocessing import MinMaxScaler
from sklearn.tree import DecisionTreeClassifier
from sqlalchemy import exc

MODEL_PATH = 'ada_boost_model.pkl'
ada_boost = None
scaler = MinMaxScaler()
active_threads = {}


def load_model():
    global ada_boost

    if os.path.exists('ada_boost_model.pkl'):
        ada_boost = joblib.load('ada_boost_model.pkl')
        print("AdaBoost model loaded successfully.")
    else:
        print("AdaBoost model not found. Training a new model...")
        combined_data = fetch_training_data()
        if combined_data is None or combined_data.empty:
            print("ERROR: No data available for training. Cannot train model.")
            return
        true_labels = generate_labels_for_training(combined_data)
        train_ada_boost(combined_data, true_labels)


import pandas as pd
from database_connection import get_engine


def fetch_training_data():
    engine = get_engine()

    query_camera = """
    SELECT driver_id, eye_aspect_ratio, mouth_aspect_ratio
    FROM camera_data
    """
    query_tachograph = """
    SELECT driver_id, predictions, driving_time, break_time
    FROM tachograph_data
    """

    try:
        camera_data = pd.read_sql(query_camera, engine)
        tachograph_data = pd.read_sql(query_tachograph, engine)
    except Exception as e:
        print(f"Error getting training data: {e}")
        return pd.DataFrame()

    if camera_data.empty or tachograph_data.empty:
        print("No training data found in database.")
        return pd.DataFrame()

    combined_data = pd.merge(camera_data, tachograph_data, on='driver_id', how='inner')
    combined_data.fillna(0, inplace=True)

    print(f"Training data fetched successfully: {len(combined_data)} samples")
    return combined_data


def save_model():
    global ada_boost
    joblib.dump(ada_boost, MODEL_PATH)
    print("AdaBoost model saved successfully.")


def fetch_latest_camera_predictions(driver_id):
    engine = get_engine()
    query = """
    SELECT driver_id, MAX(timestamp) AS latest_timestamp_camera, 
           eye_aspect_ratio, mouth_aspect_ratio
    FROM camera_data
    WHERE driver_id = %s
    GROUP BY driver_id, eye_aspect_ratio, mouth_aspect_ratio
    """
    try:
        return pd.read_sql(query, engine, params=(driver_id,))
    except Exception as e:
        print(f"Error getting camera data: {e}")
        return pd.DataFrame()


def fetch_latest_tachograph_predictions(driver_id):
    engine = get_engine()
    query = """
    SELECT driver_id, MAX(timestamp) AS latest_timestamp_tachograph, 
           predictions, driving_time, break_time
    FROM tachograph_data
    WHERE driver_id = %s
    GROUP BY driver_id, predictions, driving_time, break_time
    """
    try:
        return pd.read_sql(query, engine, params=(driver_id,))
    except Exception as e:
        print(f"Error getting tachograph data: {e}")
        return pd.DataFrame()


def normalize_features(df, columns):
    global scaler
    if df.empty:
        return df
    df[columns] = scaler.fit_transform(df[columns])
    return df


def round_timestamp_to_millis(ts):
    if pd.isnull(ts):
        return ts
    if isinstance(ts, datetime):
        return ts.replace(microsecond=int(ts.microsecond / 1000) * 1000)
    # Si només és una `date`, converteix-la a `datetime`
    return datetime.combine(ts, datetime.min.time()).replace(microsecond=0)


### Combina les prediccions de la càmera i el tacògraf per al conductor
def combine_predictions_for_driver(driver_id):
    camera_data = fetch_latest_camera_predictions(driver_id)
    tachograph_data = fetch_latest_tachograph_predictions(driver_id)

    if camera_data.empty or tachograph_data.empty:
        print(f"[Driver {driver_id}] Missing camera or tachograph data. Skipping combination.")
        return None

    if camera_data.empty or tachograph_data.empty:
        print(f"No data available for driver_id: {driver_id}.")
        return None

    tachograph_data = normalize_features(tachograph_data, ['predictions', 'driving_time', 'break_time'])
    camera_data = normalize_features(camera_data, ['eye_aspect_ratio', 'mouth_aspect_ratio'])

    combined_data = pd.merge(camera_data, tachograph_data, on='driver_id', suffixes=('_camera', '_tachograph'))

    combined_data.fillna(0, inplace=True)

    # combined_data['latest_timestamp_camera'] = combined_data['latest_timestamp_camera'].apply(round_timestamp_to_millis)
    # combined_data['latest_timestamp_tachograph'] = combined_data['latest_timestamp_tachograph'].apply(
    #     round_timestamp_to_millis)

    print(">>> Timestamps amb mil·lisegons:")
    print(combined_data['latest_timestamp_camera'].head())
    print(combined_data['latest_timestamp_tachograph'].head())

    combined_data['combined_prediction'] = (
            combined_data['eye_aspect_ratio'] * 0.25 +
            combined_data['mouth_aspect_ratio'] * 0.15 +
            combined_data['predictions'] * 0.3 +
            combined_data.get('driving_time', 0) * 0.15 -
            combined_data.get('break_time', 0) * 0.2
    )

    combined_data['fatigue_score'] = combined_data['combined_prediction'] * 0.8
    combined_data['drowsiness_score'] = combined_data['combined_prediction'] * 0.6

    combined_data['fatigue_detected'] = combined_data['fatigue_score'] > 0.7
    combined_data['drowsiness_detected'] = combined_data['drowsiness_score'] < 0.3

    combined_data['predictions'] = combined_data[['fatigue_detected', 'drowsiness_detected']].any(axis=1).astype(int)

    print(f"Combined predictions for driver {driver_id}:")
    print(combined_data[['driver_id', 'fatigue_score', 'drowsiness_score', 'predictions']].head())

    return combined_data


def combine_predictions_continuous(driver_id):
    if driver_id in active_threads:
        print(f"Detection already running for driver_id: {driver_id}.")
        return

    def detection_loop():
        try:
            while driver_id in active_threads:
                print(f"Checking for new data for driver_id: {driver_id}...")

                combined_data = combine_predictions_for_driver(driver_id)

                if combined_data is not None:
                    combined_data = predict_with_ada_boost(combined_data)
                    save_combined_predictions_to_db(combined_data)
                else:
                    print(f"No combined data available for driver_id: {driver_id}.")

                time.sleep(2)
        except KeyboardInterrupt:
            print("Exiting detection loop...")

    thread = threading.Thread(target=detection_loop, daemon=True)
    active_threads[driver_id] = thread
    thread.start()


def predict_with_ada_boost(combined_data, max_samples=5000):
    global ada_boost

    if ada_boost is None:
        print("ERROR: AdaBoost model not found. Please train a new model.")
        return None

    if not hasattr(ada_boost, "n_classes_"):
        print("ERROR: AdaBoost model has not been trained. Please train it first.")
        return None

    X = combined_data[['eye_aspect_ratio', 'mouth_aspect_ratio', 'driving_time', 'combined_prediction']].values

    if X.shape[0] > max_samples:
        combined_data = combined_data.sample(n=max_samples, random_state=42)
        X = combined_data[['eye_aspect_ratio', 'mouth_aspect_ratio', 'driving_time', 'combined_prediction']].values

    predictions = ada_boost.predict(X)
    combined_data = combined_data.copy()
    combined_data.loc[:, 'predictions'] = predictions
    combined_data.loc[:, 'ada_fatigue_detected'] = combined_data['predictions'] > 0.8

    return combined_data


def train_ada_boost(combined_data, true_labels):
    global ada_boost
    X = combined_data[['eye_aspect_ratio', 'combined_prediction', 'fatigue_score', 'drowsiness_score']].values
    y = true_labels

    base_estimator = DecisionTreeClassifier(max_depth=3)
    ada_boost = AdaBoostClassifier(base_estimator, n_estimators=100)
    ada_boost.fit(X, y)

    print("AdaBoost model trained successfully.")
    save_model()


def save_combined_predictions_to_db(combined_data):
    if combined_data is None or combined_data.empty:
        print("No combined data available to save.")
        return

    columns_to_store = [
        'driver_id', 'latest_timestamp_camera', 'latest_timestamp_tachograph',
        'eye_aspect_ratio', 'mouth_aspect_ratio', 'combined_prediction',
        'fatigue_score', 'drowsiness_score', 'fatigue_detected', 'drowsiness_detected', 'predictions',
        'ada_fatigue_detected'
    ]

    combined_data = combined_data[[col for col in columns_to_store if col in combined_data.columns]]

    try:
        engine = get_engine()
        with engine.begin() as connection:
            combined_data.to_sql('combined_predictions', connection, if_exists='append', index=False)
            print(f"Combined predictions saved successfully for driver_id: {combined_data['driver_id'].values[0]}")

    except exc.SQLAlchemyError as e:
        print(f"Error saving combined predictions: {e}")


def generate_labels_for_training(combined_data):
    num_samples = len(combined_data)
    labels = np.random.choice([0, 1], size=num_samples, p=[0.5, 0.5])
    return labels


if __name__ == "__main__":
    load_model()
