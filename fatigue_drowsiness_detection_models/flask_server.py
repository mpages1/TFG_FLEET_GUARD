import os
import signal
import sys

from combine_results.combine_models import load_model, combine_predictions_for_driver, predict_with_ada_boost, \
    save_combined_predictions_to_db, generate_labels_for_training, train_ada_boost, save_model

sys.path.insert(0, os.path.abspath(os.path.join(os.path.dirname(__file__), 'combine_results')))

sys.path.insert(0, os.path.abspath(os.path.dirname(__file__)))
sys.path.insert(0, os.path.abspath(os.path.join(os.path.dirname(__file__), 'tachograph_based_detection')))
sys.path.insert(0, os.path.abspath(os.path.join(os.path.dirname(__file__), 'combine_results')))

import logging
import threading

from flask import Flask, request, jsonify

from camera_based_detection import CameraFatigueDetector
from tachograph_based_detection.generate_tachograph_data import start_tachograph_simulation, stop_tachograph_simulation

app = Flask(__name__)

logging.basicConfig(level=logging.INFO)
logger = logging.getLogger(__name__)

detector_camera = None
detector_tachograph = None

# Dictionaries to manage active threads for each driver
active_detectors = {}  # For camera-based detection
active_tachograph_simulations = {}  # For tachograph simulation
active_combined_detectors = {}  # For combined detection

active_threads = {}


def extract_driver_id(data):
    driver_id = data.get("driver_id")
    if driver_id is None:
        raise ValueError("Missing driver_id in request body")
    return int(driver_id)


@app.route("/init_camera_detection", methods=["POST"])
def init_camera_detection():
    global detector_camera
    data = request.json
    shape_predictor_path = data.get("shape_predictor_path")
    alarm_path = data.get("alarm_path", "")
    webcam_index = data.get("webcam_index", 0)

    if not shape_predictor_path or not os.path.exists(shape_predictor_path):
        return jsonify({"error": "Missing required parameters or model not found"}), 400

    detector_camera = CameraFatigueDetector(shape_predictor_path, alarm_path, webcam_index)
    logger.info("Camera detection model initialized.")

    return jsonify({"message": "Camera model is ready. Waiting for commands."}), 200


@app.route("/start_camera_detection", methods=["POST"])
def start_camera_detection():
    global detector_camera
    try:
        driver_id = extract_driver_id(request.json)

        if driver_id in active_detectors:
            return jsonify({"message": f"Detection is already active for driver {driver_id}"}), 200

        if not detector_camera:
            return jsonify({"error": "Camera model has not been initialized"}), 400

        logger.info(f"Starting camera detection for driver {driver_id}...")
        thread = threading.Thread(target=detector_camera.start_detection, args=(driver_id,), daemon=True)
        thread.start()
        active_detectors[driver_id] = thread

        return jsonify({"message": f"Camera detection started for driver {driver_id}"}), 200
    except Exception as e:
        logger.error(f"Failed to start camera detection: {str(e)}")
        return jsonify({"error": str(e)}), 400


@app.route("/stop_camera_detection", methods=["POST"])
def stop_camera_detection():
    try:
        driver_id = extract_driver_id(request.json)

        if driver_id not in active_detectors:
            return jsonify({"message": f"No active detection for driver {driver_id}"}), 200

        logger.info(f"[Driver {driver_id}] Stopping camera detection...")
        detector_camera.stop_detection()
        del active_detectors[driver_id]

        return jsonify({"message": f"Camera detection stopped for driver {driver_id}"}), 200
    except Exception as e:
        return jsonify({"error": str(e)}), 400


@app.route("/init_tachograph_detection", methods=["POST"])
def init_tachograph_detection():
    global detector_tachograph
    detector_tachograph = True
    logger.info("Tachograph model initialized.")

    return jsonify({"message": "Tachograph model is ready. Waiting for commands."}), 200


@app.route("/start_tachograph_detection", methods=["POST"])
def start_tachograph_detection():
    try:
        driver_id = extract_driver_id(request.json)

        if driver_id in active_tachograph_simulations:
            return jsonify({"message": f"Tachograph simulation already running for driver {driver_id}"}), 200

        logger.info(f"[Driver {driver_id}] Starting tachograph simulation...")

        thread = threading.Thread(target=start_tachograph_simulation, args=(driver_id,), daemon=True)
        thread.start()
        active_tachograph_simulations[driver_id] = thread

        return jsonify({"message": f"Tachograph simulation started for driver {driver_id}"}), 200
    except Exception as e:
        return jsonify({"error": str(e)}), 400


@app.route("/stop_tachograph_detection", methods=["POST"])
def stop_tachograph_detection():
    try:
        driver_id = extract_driver_id(request.json)

        if driver_id not in active_tachograph_simulations:
            return jsonify({"message": f"No active simulation for driver {driver_id}"}), 200

        logger.info(f"[Driver {driver_id}] Stopping tachograph simulation...")
        stop_tachograph_simulation(driver_id)
        del active_tachograph_simulations[driver_id]

        return jsonify({"message": f"Tachograph simulation stopped for driver {driver_id}"}), 200
    except Exception as e:
        return jsonify({"error": str(e)}), 400


@app.route("/init_combined_detection", methods=["POST"])
def init_combined_detection():
    if not request.is_json:
        return jsonify({"error": "Request must be JSON"}), 415

    logger.info("Received request to initialize combined detection model.")

    try:
        load_model()
        logger.info("Combined detection model initialized successfully.")
        return jsonify({"message": "Combined detection model is ready."}), 200
    except Exception as e:
        logger.error(f"ERROR: Failed to initialize combined detection model: {str(e)}")
        return jsonify({"error": f"Failed to initialize combined detection: {str(e)}"}), 500


@app.route("/start_combined_detection", methods=["POST"])
def start_combined_detection():
    try:
        driver_id = extract_driver_id(request.json)

        if driver_id in active_combined_detectors:
            return jsonify({"message": f"Combined detection already active for driver {driver_id}"}), 200

        logger.info(f"[Driver {driver_id}] Starting combined detection...")

        def detection_loop():
            while driver_id in active_combined_detectors:
                logger.info(f"[Driver {driver_id}] Running combined detection step...")
                combined_data = combine_predictions_for_driver(driver_id)

                if combined_data is not None:
                    combined_data = predict_with_ada_boost(combined_data)
                    save_combined_predictions_to_db(combined_data)
                else:
                    logger.warning(f"[Driver {driver_id}] No data to process.")
                threading.Event().wait(5)

        thread = threading.Thread(target=detection_loop, daemon=True)
        active_combined_detectors[driver_id] = thread
        thread.start()

        return jsonify({"message": f"Combined detection started for driver {driver_id}"}), 200
    except Exception as e:
        return jsonify({"error": str(e)}), 400


@app.route("/train_combined_model", methods=["POST"])
def train_combined_model():
    try:
        driver_id = extract_driver_id(request.json)

        logger.info(f"[Driver {driver_id}] Training combined model...")

        combined_data = combine_predictions_for_driver(driver_id)
        if combined_data is None or combined_data.empty:
            logger.warning(f"[Driver {driver_id}] No training data found. Training skipped.")
            return jsonify({"error": "No data available for training"}), 204  # No Content

        true_labels = generate_labels_for_training(combined_data)
        train_ada_boost(combined_data, true_labels)
        save_model()

        logger.info(f"[Driver {driver_id}] Combined model trained successfully.")
        return jsonify({"message": f"Model trained for driver {driver_id}"}), 200

    except ValueError as ve:
        return jsonify({"error": str(ve)}), 400

    except Exception as e:
        logger.error(f"[Driver] Unexpected training error: {str(e)}")
        return jsonify({"error": "Internal error during training"}), 500


@app.route("/stop_combined_detection", methods=["POST"])
def stop_combined_detection():
    try:
        driver_id = extract_driver_id(request.json)

        if driver_id not in active_combined_detectors:
            return jsonify({"message": f"No active combined detection for driver {driver_id}"}), 200

        del active_combined_detectors[driver_id]
        logger.info(f"[Driver {driver_id}] Combined detection stopped.")

        return jsonify({"message": f"Combined detection stopped for driver {driver_id}"}), 200
    except Exception as e:
        return jsonify({"error": str(e)}), 400


@app.route("/check_adaboost_model", methods=["GET"])
def check_adaboost_model():
    model_path = "combine_results/adaboost_model.pkl"

    if os.path.exists(model_path):
        return jsonify({"message": "AdaBoost model is trained"}), 200
    else:
        return jsonify({"error": "AdaBoost model is not trained"}), 404


@app.route("/stop_server", methods=["POST"])
def stop_server():
    os.kill(os.getpid(), signal.SIGTERM)
    return jsonify({"message": "Flask server shutting down..."})


if __name__ == "__main__":
    logger.info("Starting Flask API for combined fatigue and drowsiness detection...")
    app.run(host="0.0.0.0", port=5002, debug=True)
