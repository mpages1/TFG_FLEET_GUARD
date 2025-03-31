import logging
import os
import time
from collections import deque
from threading import Thread, Event

import cv2
import dlib
import imutils
import pandas as pd
import pygame
from imutils import face_utils
from imutils.video import VideoStream
from scipy.spatial import distance as dist
from sqlalchemy import exc

from database_connection import get_engine

logger = logging.getLogger(__name__)


def sound_alarm(path):
    try:
        if not path or not os.path.exists(path):
            print(f"Alarm path invalid or not found: {path}")
            return

        print(f"Playing alarm from: {path}")
        pygame.mixer.init()
        pygame.mixer.music.load(path)
        pygame.mixer.music.play()

        while pygame.mixer.music.get_busy():
            time.sleep(1)
    except Exception as e:
        print(f"Error playing alarm sound: {e}")


def eye_aspect_ratio(eye):
    A = dist.euclidean(eye[1], eye[5])
    B = dist.euclidean(eye[2], eye[4])
    C = dist.euclidean(eye[0], eye[3])
    return (A + B) / (2.0 * C)


def mouth_aspect_ratio(mouth):
    A = dist.euclidean(mouth[2], mouth[10])
    B = dist.euclidean(mouth[4], mouth[8])
    C = dist.euclidean(mouth[0], mouth[6])
    return (A + B) / (2.0 * C)


class FeatureSmoother:
    def __init__(self, window_size=5):
        self.values = deque(maxlen=window_size)

    def smooth(self, new_value):
        self.values.append(new_value)
        return sum(self.values) / len(self.values)


class CameraFatigueDetector:
    def __init__(self, shape_predictor_path, alarm_path, webcam_index):
        self.stop_event = Event()
        self.thread = None
        self.vs = None

        self.shape_predictor_path = shape_predictor_path
        self.alarm_path = alarm_path
        self.webcam_index = webcam_index
        self.ALARM_ON = False

        self.EYE_AR_THRESH = 0.25
        self.EYE_AR_CONSEC_FRAMES = 12
        self.MOUTH_AR_THRESH = 0.6
        self.MOUTH_AR_CONSEC_FRAMES = 10
        self.COUNTER_EAR = 0
        self.COUNTER_MAR = 0

        self.face_cascade = cv2.CascadeClassifier(cv2.data.haarcascades + 'haarcascade_frontalface_default.xml')
        self.predictor = dlib.shape_predictor(shape_predictor_path)

        logger.info("Starting video stream...")
        self.vs = VideoStream(src=self.webcam_index).start()
        time.sleep(2.0)

        test_frame = self.vs.read()
        if test_frame is None or test_frame.size == 0:
            logger.error("No camera feed available")
            raise RuntimeError("No camera feed available")

        (self.lStart, self.lEnd) = face_utils.FACIAL_LANDMARKS_IDXS["left_eye"]
        (self.rStart, self.rEnd) = face_utils.FACIAL_LANDMARKS_IDXS["right_eye"]
        (self.mStart, self.mEnd) = face_utils.FACIAL_LANDMARKS_IDXS["mouth"]

    def start_detection(self, driver_id):
        if self.thread is not None and self.thread.is_alive():
            logger.warning(f"Detection already running for driver {driver_id}")
            return

        self.stop_event.clear()
        self.thread = Thread(target=self._detection_loop, args=(driver_id,), daemon=True)
        self.thread.start()

    def _detection_loop(self, driver_id):
        logger.info(f"Starting detection loop for driver {driver_id}")
        ear_smoother = FeatureSmoother(5)
        mar_smoother = FeatureSmoother(5)

        while not self.stop_event.is_set():
            try:
                frame = self.vs.read()
                if frame is None or frame.size == 0:
                    logger.warning("Invalid frame, skipping...")
                    time.sleep(0.5)
                    continue

                frame = imutils.resize(frame, width=450)
                gray = cv2.cvtColor(frame, cv2.COLOR_BGR2GRAY)
                faces = self.face_cascade.detectMultiScale(gray, scaleFactor=1.1, minNeighbors=5, minSize=(30, 30))

                for (x, y, w, h) in faces:
                    rect = dlib.rectangle(int(x), int(y), int(x + w), int(y + h))
                    shape = self.predictor(gray, rect)
                    shape = face_utils.shape_to_np(shape)

                    leftEye = shape[self.lStart:self.lEnd]
                    rightEye = shape[self.rStart:self.rEnd]
                    mouth = shape[self.mStart:self.mEnd]

                    ear = ear_smoother.smooth((eye_aspect_ratio(leftEye) + eye_aspect_ratio(rightEye)) / 2.0)
                    mar = mar_smoother.smooth(mouth_aspect_ratio(mouth))

                    # Draw landmarks
                    leftEyeHull = cv2.convexHull(leftEye)
                    rightEyeHull = cv2.convexHull(rightEye)
                    mouthHull = cv2.convexHull(mouth)

                    cv2.drawContours(frame, [leftEyeHull], -1, (0, 255, 0), 1)
                    cv2.drawContours(frame, [rightEyeHull], -1, (0, 255, 0), 1)
                    cv2.drawContours(frame, [mouthHull], -1, (0, 255, 0), 1)

                    drowsy = False
                    if ear < self.EYE_AR_THRESH:
                        self.COUNTER_EAR += 1
                        if self.COUNTER_EAR >= self.EYE_AR_CONSEC_FRAMES:
                            logger.info(" Drowsiness detected!")
                            drowsy = True
                            self._trigger_alarm()
                    else:
                        self.COUNTER_EAR = 0
                        self.ALARM_ON = False

                    fatigue = False
                    if mar > self.MOUTH_AR_THRESH:
                        self.COUNTER_MAR += 1
                        if self.COUNTER_MAR >= self.MOUTH_AR_CONSEC_FRAMES:
                            logger.info(" Fatigue detected!")
                            fatigue = True
                            self._trigger_alarm()
                    else:
                        self.COUNTER_MAR = 0
                        self.ALARM_ON = False

                    save_camera_data_to_db(driver_id, ear, mar, drowsy, fatigue)

                    cv2.putText(frame, f"EAR: {ear:.2f}", (300, 30), cv2.FONT_HERSHEY_SIMPLEX, 0.7, (255, 0, 0), 2)
                    cv2.putText(frame, f"MAR: {mar:.2f}", (300, 60), cv2.FONT_HERSHEY_SIMPLEX, 0.7, (255, 0, 0), 2)

                cv2.imshow("Fatigue Detection", frame)
                if cv2.waitKey(1) & 0xFF == ord("q"):
                    logger.info(" Q key pressed, stopping detection...")
                    self.stop_detection()
                    break

                time.sleep(0.05)

            except Exception as e:
                logger.error(f" Error in detection loop: {e}")

        logger.info(" Detection loop stopped.")

    def _trigger_alarm(self):
        if not self.ALARM_ON and self.alarm_path:
            self.ALARM_ON = True
            Thread(target=sound_alarm, args=(self.alarm_path,), daemon=True).start()

    def stop_detection(self):
        logger.info(" Stopping detection...")
        self.stop_event.set()
        if self.thread and self.thread.is_alive():
            self.thread.join(timeout=5)
        if self.vs:
            self.vs.stop()
        cv2.destroyAllWindows()
        self.thread = None


def save_camera_data_to_db(driver_id, ear, mar, drowsiness, yawning):
    try:
        engine = get_engine()
        data = {
            "driver_id": driver_id,
            "timestamp": pd.Timestamp.now(),
            "eye_aspect_ratio": ear,
            "mouth_aspect_ratio": mar,
            "drowsiness_detected": drowsiness,
            "yawning_detected": yawning
        }
        df = pd.DataFrame([data])

        with engine.begin() as connection:
            df.to_sql('camera_data', connection, if_exists='append', index=False)

        print(
            f"Data inserted: {data['timestamp']} - Driver {data['driver_id']} - EAR: {data['eye_aspect_ratio']} - MAR: {data['mouth_aspect_ratio']}")
    except exc.SQLAlchemyError as e:
        print(f"Error inserting data: {e}")
