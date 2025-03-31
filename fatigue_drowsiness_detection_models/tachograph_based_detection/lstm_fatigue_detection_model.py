__author__ = 'mpages1'

import logging
import os
import threading

import numpy as np
import pandas as pd
from keras.layers import LSTM, Dense, Input
from keras.models import Sequential, load_model
from sklearn.preprocessing import MinMaxScaler

logging.basicConfig(level=logging.INFO)
logger = logging.getLogger(__name__)

MODEL_PATH = 'tachograph_fatigue_model.keras'
running = False  # Control of continuous monitoring
SEQ_LENGTH = 50  # Number of samples for LSTM


def preprocess_data(data):
    required_columns = [
        'driving_time', 'driving_distance', 'speed', 'break_time', 'engine_hours',
        'fuel_consumption', 'rpm', 'gear_position', 'brake_pedal',
        'accelerator_pedal', 'steering_wheel_angle', 'driver_active'
    ]

    if not all(col in data.columns for col in required_columns):
        missing = set(required_columns) - set(data.columns)
        raise ValueError(f"❌ Columns missing in data: {missing}")

    data = data[required_columns].copy()

    for col in ['brake_pedal', 'accelerator_pedal', 'driver_active']:
        data[col] = data[col].astype(int)

    scaler = MinMaxScaler(feature_range=(0, 1))
    scaled_data = scaler.fit_transform(data)

    return scaled_data, scaler


def create_sequences(data, seq_length=SEQ_LENGTH):
    """ Generate sequences for LSTM """
    X, y = [], []
    for i in range(len(data) - seq_length):
        X.append(data[i:i + seq_length])
        y.append(data[i + seq_length][0])  # Predict fatigue score

    X, y = np.array(X), np.array(y)

    if X.shape[0] == 0:  # Check if there is enough data
        raise ValueError("No data available for training.")

    return X, y


def load_and_clean_csv(csv_path):
    logger.info(f"Llegint fitxer CSV: {csv_path}")
    df = pd.read_csv(csv_path)

    if 'timestamp' in df.columns:
        df['timestamp'] = pd.to_datetime(df['timestamp'], errors='coerce')

    correct_columns = [
        'driver_id', 'timestamp', 'driving_time', 'driving_distance', 'speed',
        'break_time', 'engine_hours', 'fuel_consumption', 'rpm', 'gear_position',
        'brake_pedal', 'accelerator_pedal', 'steering_wheel_angle', 'driver_active', 'predictions'
    ]

    # Check and reorder columns
    missing_columns = set(correct_columns) - set(df.columns)
    if missing_columns:
        raise ValueError(f"Missing columns in CSV: {missing_columns}")

    df = df[correct_columns]

    logger.info("Cleaning data...")

    return df


def train_tachograph_model(csv_path=None):
    if os.path.exists(MODEL_PATH):
        logger.info("Model already exists, skipping training.")
        return

    logger.info("Loading tachograph data for training...")

    if csv_path:
        data = load_and_clean_csv(csv_path)
    else:
        from database_connection import get_tachograph_data
        data = get_tachograph_data()

    if data is None or data.empty:
        logger.error("Error: No data available for training.")
        return

    scaled_data, scaler = preprocess_data(data)
    X, y = create_sequences(scaled_data)

    split = int(0.8 * len(X))
    X_train, X_test = X[:split], X[split:]
    y_train, y_test = y[:split], y[split:]

    model = Sequential([
        Input(shape=(SEQ_LENGTH, X.shape[2])),
        LSTM(units=50, return_sequences=True),
        LSTM(units=50),
        Dense(units=1, activation='sigmoid')  # Output of the model is a probability [0, 1] for fatigue and drowsiness
    ])

    model.compile(optimizer='adam', loss='binary_crossentropy')

    logger.info("🚀 Entrenant el model...")
    model.fit(X_train, y_train, epochs=30, batch_size=32, validation_data=(X_test, y_test))

    model.save(MODEL_PATH)
    logger.info(f"💾 Model guardat com '{MODEL_PATH}'")


import time


def predict_fatigue_tachograph():
    if not os.path.exists(MODEL_PATH):
        logger.warning("Model not found. Train it first!")
        return None

    logger.info("🔍 Carregant model...")
    model = load_model(MODEL_PATH)

    from database_connection import get_tachograph_data
    logger.info("Loading data for prediction...")

    attempts = 0
    while True:
        data = get_tachograph_data()

        if data is not None and len(data) >= SEQ_LENGTH:
            break

        if attempts >= 10:
            logger.warning("No more data available after 10 attempts.")
            return None

        logger.warning(f"Wainting for more data... ({attempts})")
        time.sleep(5)
        attempts += 1

    # Processa les dades
    scaled_data, scaler = preprocess_data(data)
    X, _ = create_sequences(scaled_data)

    logger.info("Generating predictions...")
    predictions = model.predict(X)

    driver_ids = data['driver_id'].values[-len(predictions):]

    # Convertir prediccions escalades a valors reals
    predictions_full = np.zeros((predictions.shape[0], scaled_data.shape[1]))
    predictions_full[:, 0] = predictions.flatten()

    try:
        predictions_full = scaler.inverse_transform(predictions_full)
    except ValueError as e:
        logger.error(f"Error: {e}")
        return None

    predictions = predictions_full[:, 0]

    if len(driver_ids) != len(predictions):
        logger.error("The number of driver IDs does not match the number of predictions.")
        return None

    results = pd.DataFrame({'driver_id': driver_ids, 'fatigue_score': predictions})
    results['fatigue_detected'] = results['fatigue_score'] > 0.5  # Llindar per detectar fatiga

    fatigued_drivers = results[results['fatigue_detected']]
    if not fatigued_drivers.empty:
        logger.info("Fatigue detected:")
        logger.info(fatigued_drivers)

    return results


def start_fatigue_monitoring():
    global running
    if running:
        logger.warning("The fatigue monitoring is already running.")
        return

    running = True
    logger.info("Starting fatigue monitoring...")

    def monitor_loop():
        while running:
            logger.info("Checking for driver fatigue...")
            predict_fatigue_tachograph()
            time.sleep(10)

    thread = threading.Thread(target=monitor_loop, daemon=True)
    thread.start()


def stop_fatigue_monitoring():
    global running
    running = False
    logger.info("Detection stopped.")


if __name__ == "__main__":
    import argparse

    ap = argparse.ArgumentParser()
    ap.add_argument("-c", "--csv", required=False, help="CSV file with tachograph data for initial training")
    args = ap.parse_args()

    train_tachograph_model(args.csv)
    start_fatigue_monitoring()
