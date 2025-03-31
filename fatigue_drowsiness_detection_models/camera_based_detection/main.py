__author__ = 'mpages1'

import argparse
import logging
import os
import sys

sys.path.insert(0, os.path.abspath(os.path.join(os.path.dirname(__file__), '..')))

from camera_based_detection import CameraFatigueDetector

logging.basicConfig(level=logging.INFO)
logger = logging.getLogger(__name__)

detector = None


def start_detection(driver_id, shape_predictor_path, alarm_path, webcam_index):
    global detector
    logger.info(f"Trying to start detection for driver {driver_id}...")

    if detector is None:
        logger.info("Initializing CameraFatigueDetector...")
        detector = CameraFatigueDetector(shape_predictor_path, alarm_path, webcam_index)

    logger.info(f"Starting detection for driver {driver_id}...")
    detector.start_detection(driver_id)


def stop_detection():
    global detector
    if detector is not None:
        logger.info("Stopping detection...")
        detector.stop_detection()
        detector = None
    else:
        logger.warning("No detection running to stop!")


def main():
    ap = argparse.ArgumentParser()
    ap.add_argument("-p", "--shape-predictor", required=True, help="Path to facial landmark predictor")
    ap.add_argument("-a", "--alarm", type=str, default="", help="Path to alarm sound")
    ap.add_argument("-w", "--webcam", type=int, default=0, help="Index of webcam on system")
    ap.add_argument("-d", "--driver-id", type=int, required=True, help="Driver ID for detection tracking")
    ap.add_argument("--stop", action="store_true", help="Stop the fatigue detection process")
    ap.add_argument("--debug", action="store_true", help="Activate debug mode")

    args = vars(ap.parse_args())

    if args["debug"]:
        logger.setLevel(logging.DEBUG)
        logger.debug("Debug mode activated")

    logger.info("Arguments: %s", args)

    try:
        if args["stop"]:
            stop_detection()
        else:

            shape_predictor_path = os.path.abspath(args["shape_predictor"])
            alarm_path = args["alarm"]
            if alarm_path:
                alarm_path = os.path.abspath(alarm_path)

            if not os.path.exists(shape_predictor_path):
                raise FileNotFoundError(f"No shape predictor found at path: {shape_predictor_path}")

            if alarm_path and not os.path.exists(alarm_path):
                raise FileNotFoundError(f"No alarm sound found at path: {alarm_path}")

            start_detection(
                args["driver_id"],
                shape_predictor_path,
                alarm_path,
                args["webcam"]
            )

    except KeyboardInterrupt:
        logger.info("\n[INFO] KeyboardInterrupt detected. Stopping detection safely...")
        stop_detection()
    finally:
        logger.info("[INFO] Cleaning up resources before exiting.")


if __name__ == "__main__":
    logger.info("Starting program...")
    main()
