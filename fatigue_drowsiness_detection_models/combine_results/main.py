__author__ = 'mpages1'

import argparse
import logging
import os
import sys
import threading
import time

sys.path.insert(0, os.path.abspath(os.path.join(os.path.dirname(__file__), '..')))

from combine_models import combine_predictions_for_driver, train_ada_boost, save_combined_predictions_to_db

logging.basicConfig(level=logging.INFO)
logger = logging.getLogger(__name__)

active_threads = {}


def start_combined_detection(driver_id):
    logger.info(f"Starting combined detection for driver {driver_id}...")

    if driver_id in active_threads:
        logger.warning(f"Detection already running for driver {driver_id}.")
        return

    detection_thread = threading.Thread(target=run_combined_detection, args=(driver_id,), daemon=True)
    detection_thread.start()
    active_threads[driver_id] = detection_thread


def run_combined_detection(driver_id):
    while driver_id in active_threads:
        logger.info(f"Checking for new data for driver {driver_id}...")

        combined_data = combine_predictions_for_driver(driver_id)

        if combined_data is not None and not combined_data.empty:
            logger.info(f"Combined data for driver {driver_id}:\n{combined_data.head()}")
            save_combined_predictions_to_db(combined_data)
        else:
            logger.warning(f"No combined data available for driver {driver_id}.")

        time.sleep(5)


def stop_combined_detection(driver_id):
    if driver_id in active_threads:
        logger.info(f"Stopping combined detection for driver {driver_id}...")
        del active_threads[driver_id]
    else:
        logger.warning(f"No active detection found for driver {driver_id}.")


def main():
    ap = argparse.ArgumentParser(description="Combined fatigue and drowsiness detection system")
    ap.add_argument("-d", "--driver-id", type=int, required=True, help="Driver ID for detection tracking")
    ap.add_argument("--train", action="store_true", help="Train AdaBoost model")
    ap.add_argument("--stop", action="store_true", help="Stop combined detection for a driver")
    ap.add_argument("--debug", action="store_true", help="Activate debug mode")

    args = vars(ap.parse_args())

    if args["debug"]:
        logger.setLevel(logging.DEBUG)
        logger.debug("Debug mode activated")

    logger.info("Arguments: %s", args)

    try:
        driver_id = args["driver_id"]

        if args["stop"]:
            stop_combined_detection(driver_id)
        elif args["train"]:
            logger.info(f"Training AdaBoost model for driver {driver_id}...")
            combined_data = combine_predictions_for_driver(driver_id)

            if combined_data is None or combined_data.empty:
                logger.warning("No combined data available for training AdaBoost model.")
                return

            true_labels = [1 if x > 0.5 else 0 for x in combined_data["combined_prediction"]]
            train_ada_boost(combined_data, true_labels)
            logger.info("AdaBoost model trained successfully!")
        else:
            start_combined_detection(driver_id)

            try:
                while True:
                    time.sleep(10)
            except KeyboardInterrupt:
                logger.info("\n[INFO] Exiting combined detection system...")
                stop_combined_detection(driver_id)

    except Exception as e:
        logger.error(f"ERROR: {e}")
    finally:
        logger.info("[INFO] Cleaning up resources before exiting.")


if __name__ == "__main__":
    logger.info("Starting program...")
    main()
