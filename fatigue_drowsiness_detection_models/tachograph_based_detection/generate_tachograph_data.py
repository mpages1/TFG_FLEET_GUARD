import random
import time
from datetime import timedelta, datetime

import pandas as pd
from sqlalchemy import exc

from database_connection import get_engine

running = {}


def generate_tachograph_data(timestamp, driver_id, state):
    state.setdefault('driving_time', 0)
    state.setdefault('break_time', 0)
    state.setdefault('driving_distance', 0)
    state.setdefault('speed', 0)
    state.setdefault('engine_hours', 0)
    state.setdefault('fuel_consumption', 0)
    state.setdefault('brake_pedal', False)
    state.setdefault('accelerator_pedal', False)

    is_driving = state['speed'] > 5 or state['accelerator_pedal']
    driving_time = min(state['driving_time'] + (0.75 if is_driving else 0), 600)
    break_time = state['break_time'] + (0.75 if not is_driving else 0)

    if state['brake_pedal']:
        speed = max(0, state['speed'] - random.uniform(5, 15))
    elif state['accelerator_pedal']:
        speed = min(120, state['speed'] + random.uniform(3, 10))
    else:
        speed = max(0, state['speed'] - random.uniform(0.5, 2.0))

    driving_distance = state['driving_distance'] + (speed * (45 / 3600))

    fuel_rate = 0.1 + (speed / 100) * random.uniform(0.05, 0.15)
    fuel_consumption = state['fuel_consumption'] + fuel_rate

    engine_hours = state['engine_hours'] + (0.75 / 3600)

    brake_pedal = speed > 0 and random.random() < 0.2
    accelerator_pedal = speed < 100 and random.random() < 0.6

    predictions = min(1.0, (driving_time / 600) + (1 - break_time / 60) * 0.15)

    return {
        'timestamp': timestamp,
        'driver_id': driver_id,
        'driving_time': driving_time,
        'driving_distance': driving_distance,
        'speed': speed,
        'break_time': break_time,
        'engine_hours': engine_hours,
        'fuel_consumption': fuel_consumption,
        'rpm': int(speed * random.uniform(30, 55)) if speed > 0 else random.randint(600, 900),
        'gear_position': int(min(speed // 20 + 1, 6)) if speed > 5 else 0,
        'brake_pedal': brake_pedal,
        'accelerator_pedal': accelerator_pedal,
        'steering_wheel_angle': random.uniform(-15, 15) if speed > 0 else 0,
        'predictions': predictions
    }


def save_simulated_data_to_db(data):
    try:
        engine = get_engine()
        df = pd.DataFrame([data])

        with engine.begin() as connection:
            df.to_sql('tachograph_data', connection, if_exists='append', index=False)
            print(
                f"Data inserted: {data['timestamp']} - Driver {data['driver_id']} - Speed: {data['speed']} - Predictions: {data['predictions']}")

    except exc.SQLAlchemyError as e:
        print(f"Error en inserir dades: {e}")


def start_tachograph_simulation(driver_id):
    global running
    running[driver_id] = True

    start_date = datetime.now().replace(microsecond=int(datetime.now().microsecond / 1000) * 1000)

    driver_state = {
        'driving_time': 0,
        'driving_distance': 0,
        'speed': 0,
        'engine_hours': 0,
        'fuel_consumption': 0,
        'brake_pedal': False,
        'accelerator_pedal': False
    }

    print(f"Starting tachograph simulation for driver {driver_id}...")

    while running.get(driver_id, False):
        data = generate_tachograph_data(start_date, driver_id, driver_state)
        save_simulated_data_to_db(data)
        driver_state = data
        start_date += timedelta(seconds=45)
        time.sleep(1)

    print(f"Tachograph simulation stopped for driver {driver_id}.")


def stop_tachograph_simulation(driver_id):
    global running
    if driver_id in running:
        running[driver_id] = False
        print(f"Stopping tachograph simulation for driver {driver_id}...")


if __name__ == "__main__":
    import argparse

    ap = argparse.ArgumentParser(description="Tachograph data generation")
    ap.add_argument("-d", "--driver-id", type=int, required=True, help="Driver ID for data generation")

    args = ap.parse_args()
    start_tachograph_simulation(args.driver_id)
