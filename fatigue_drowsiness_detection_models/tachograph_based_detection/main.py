__author__ = 'mpages1'

import argparse
import os
import sys
import threading
import time

sys.path.insert(0, os.path.abspath(os.path.join(os.path.dirname(__file__), '..')))

from lstm_fatigue_detection_model import train_tachograph_model, predict_fatigue_tachograph
from generate_tachograph_data import start_tachograph_simulation, stop_tachograph_simulation

active_simulations = {}


def start_tachograph_simulation_thread(driver_id):
    print(f"Starting tachograph simulation for driver {driver_id}...")

    if driver_id in active_simulations:
        print(f"Simulation already running for driver {driver_id}.")
        return

    thread = threading.Thread(target=start_tachograph_simulation, args=(driver_id,), daemon=True)
    thread.start()
    active_simulations[driver_id] = thread


def stop_tachograph_simulation_thread(driver_id):
    if driver_id in active_simulations:
        print(f"Stopping tachograph simulation for driver {driver_id}...")
        stop_tachograph_simulation(driver_id)
        del active_simulations[driver_id]


def main():
    print("Starting tachograph-based fatigue detection system...")
    ap = argparse.ArgumentParser(description="Detection system based on tachograph data")
    ap.add_argument("-c", "--csv", required=False, help="CSV file with tachograph data for initial training")
    ap.add_argument("-d", "--driver-id", type=int, default=1, help="Driver ID for detection tracking")
    ap.add_argument("--stop", action="store_true", help="Stop the tachograph simulation")

    args = ap.parse_args()

    try:
        if args.stop:
            stop_tachograph_simulation_thread(args.driver_id)
            return

        if not os.path.exists("tachograph_fatigue_model.keras"):
            print("Training tachograph fatigue detection model...")
            train_tachograph_model(csv_path=args.csv)
        else:
            print("Model already trained. Loading...")

        start_tachograph_simulation_thread(args.driver_id)

        while True:
            print(f"\n Verifying fatigue for driver {args.driver_id}...")
            predictions = predict_fatigue_tachograph()

            if predictions is not None and not predictions.empty:
                print(f" Fatigue predictions for driver {args.driver_id}:\n{predictions.head()}")
                print(predictions)

            time.sleep(10)

    except KeyboardInterrupt:
        print("\n[INFO] Exiting tachograph-based fatigue detection system...")
        stop_tachograph_simulation_thread(args.driver_id)

    finally:
        print("[INFO] Cleaning resources before exiting.")


if __name__ == "__main__":
    main()
