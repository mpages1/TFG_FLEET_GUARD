# Comprehensive Driver Fatigue and Drowsiness Detection System (TFG 2025) - Tecnocampus & Taxitronic

## Contributors
**Author**: Mahesh Pagès Fenoy  
**Tutor**: Imanol Rojas Pérez



This repository contains modules for detecting driver fatigue and drowsiness based on camera data and tachograph data. The system is designed to combine these predictions to provide a comprehensive evaluation of a driver's state.

![image](https://github.com/user-attachments/assets/3c6ce630-7dd8-4a26-8135-d4225d2b7a73)


## Camera-Based Detection (`camera_based_detection`)

The `camera_based_detection` module is designed to analyze real-time camera data to detect signs of drowsiness and fatigue in drivers. This module integrates computer vision, machine learning, and database technologies to provide continuous monitoring and alert mechanisms.

### Components

#### Data Collection

- **Video Stream Initialization**: Uses OpenCV and imutils to initialize a video stream from a webcam.
- **Face Detection**: Utilizes OpenCV's Haar cascades and DNN-based methods (ResNet SSD) to detect faces in real-time.
- **Facial Landmarks Detection**: Employs dlib's shape predictor to detect facial landmarks such as eyes and mouth.

#### Feature Extraction

- **Eye Aspect Ratio (EAR)**: Measures the distance between vertical and horizontal eye landmarks to calculate the EAR, which helps detect eye closures.
- **Mouth Aspect Ratio (MAR)**: Measures the distance between vertical and horizontal mouth landmarks to calculate the MAR, which helps detect yawning.

#### Prediction and Detection

- **Drowsiness Detection**: Monitors the EAR values to detect prolonged eye closures, indicating drowsiness.
- **Fatigue Detection**: Monitors the MAR values to detect yawning, indicating fatigue.
- **Thresholds and Counters**: Uses predefined thresholds and consecutive frame counters to reduce false positives.
- **Machine Learning Integration**: The module can be enhanced with machine learning models like AdaBoost to improve accuracy in detecting drowsiness and fatigue.

#### Alert Mechanism

- **Sound Alarm**: Integrates pygame to play an alarm sound when drowsiness or fatigue is detected. The alarm continues until the condition ceases.
- **Visual Alerts**: Displays visual alerts on the video feed to notify the driver of detected drowsiness or fatigue.
- **Frame Overlay**: Draws bounding boxes around detected faces and highlights the detected eyes and mouth to provide real-time feedback.

#### Data Storage

- **Database Connection**: Connects to a database to store detected camera data using SQLAlchemy.
- **Data Insertion**: Saves the detected EAR, MAR, drowsiness, and yawning data along with timestamps to the database.
- **Logging and Monitoring**: Provides timestamps and structured logs for tracking historical fatigue patterns of drivers.

#### Performance Optimization

- **Efficient Face Detection**: Combines Haar cascades for quick initial detection and CNN-based models for improved accuracy.
- **Parallel Processing**: Utilizes multi-threading to ensure real-time performance without frame drops.
- **Configurable Parameters**: Allows fine-tuning of detection thresholds and alert delays to reduce false positives and improve system reliability.

## Tachograph-Based Detection (`tachograph_based_detection`)

The `tachograph_based_detection` module is designed to analyze tachograph data to detect signs of driver fatigue and drowsiness. This module includes both simulation of tachograph data and a machine learning model for fatigue detection using LSTM networks.

### Components

#### Data Simulation

- **Generating Simulated Data**: Creates synthetic tachograph data for testing and development purposes.
- **Saving Data to Database**: Inserts the simulated tachograph data into the database.

#### LSTM-Based Fatigue Detection Model

- **Preprocessing Data**: Prepares the tachograph data for training by normalizing and structuring it.
- **Training Model**: Trains an LSTM model to predict driver fatigue based on tachograph data.
- **Predicting Fatigue**: Uses the trained model to make real-time predictions on new tachograph data.

#### Real-Time Fatigue Detection with GUI

- **Pop-Up Alerts**: Displays a pop-up window with driver fatigue levels when fatigue is detected.
- **Continuous Monitoring**: Continuously checks for driver fatigue and updates the pop-up window accordingly.

## Combine Results (`combine_results`)

The `combine_results` module integrates and evaluates predictions from both the camera-based and tachograph-based systems to provide a comprehensive assessment of a driver's fatigue and drowsiness. This module leverages machine learning models to combine, normalize, and classify predictions, and to store the results in a database for further analysis.

### Components

#### Model Management

- **Loading Model**: Loads the AdaBoost model from disk if it exists; otherwise, it indicates the need for training.
- **Saving Model**: Saves the trained AdaBoost model to disk for future use.

#### Data Fetching

- **Fetching Camera Data**: Retrieves the latest camera data for a given driver from the database.
- **Fetching Tachograph Data**: Retrieves the latest tachograph data for a given driver from the database.

#### Normalization

- **Min-Max Scaling**: Applies Min-Max scaling to normalize the features from both the camera and tachograph data.

#### Combining Predictions

- **Merging Data**: Combines the normalized camera and tachograph data based on the driver ID.
- **Computing Combined Prediction**: Calculates a combined prediction score by averaging the normalized camera and tachograph predictions.

#### Score Calculation and Classification

- **Fatigue Score**: Computes a fatigue score based on the combined prediction.
- **Drowsiness Score**: Computes a drowsiness score based on the combined prediction.
- **Classification**: Classifies whether the driver is fatigued or drowsy based on the calculated scores and predefined thresholds.

#### Model Training

- **Training AdaBoost Model**: Trains an AdaBoost model using the combined data and true labels, with a focus on reducing sample size for faster training.

#### Prediction

- **Predicting with AdaBoost**: Uses the trained AdaBoost model to predict fatigue and drowsiness on the combined data, with an option to reduce sample size to avoid overload.

#### Data Storage

- **Saving Predictions to Database**: Inserts the combined predictions and classifications into the database for further analysis and reporting.

#### Continuous Processing

- **Continuous Prediction and Storage**: Continuously fetches, combines, predicts, and stores predictions for a given driver in a loop, with the ability to handle keyboard interrupts gracefully.

## Installation and Usage

To install and use the Fatigue and Drowsiness Detection System, follow these steps:

1. **Clone the Repository**:
   ```bash
   https://github.com/mpages1/fatigue_drowsiness_detection_models_TFG.git

2. **Install the Required Libraries**:
     ```bash
     pip install -r requirements.txt
3. **Follow Commands in commands.txt**:
    - Execute camera_based_detection.py
    - Execute tachograph_based_detection.py
    - Execute combine_predictions.py


