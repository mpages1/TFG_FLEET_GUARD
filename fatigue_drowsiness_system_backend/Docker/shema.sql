CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    email VARCHAR(255) UNIQUE NOT NULL,
	role_id INTEGER REFERENCES roles(id) ON DELETE CASCADE,
    password VARCHAR(255) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE roles (
    id SERIAL PRIMARY KEY,
    name VARCHAR(50) UNIQUE NOT NULL
);

CREATE TABLE administrator (
    id SERIAL PRIMARY KEY,
    user_id INTEGER REFERENCES users(id) ON DELETE CASCADE,
    name VARCHAR(255),
    phone VARCHAR(20),
    department VARCHAR(100)
);

CREATE TABLE drivers (
    id SERIAL PRIMARY KEY,
    user_id INTEGER REFERENCES users(id) ON DELETE CASCADE,
    name VARCHAR(255) NOT NULL,
    phone VARCHAR(20) UNIQUE NOT NULL,
    license_number VARCHAR(50) UNIQUE NOT NULL,
    date_of_birth TIMESTAMP,
    address VARCHAR(255)
);

CREATE TABLE tachograph_data (
    id SERIAL PRIMARY KEY,
    driver_id INT NOT NULL,
	timestamp TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,  
    driving_time FLOAT NOT NULL,
    driving_distance FLOAT NOT NULL,
    speed FLOAT NOT NULL,
    break_time FLOAT NOT NULL,
    engine_hours FLOAT NOT NULL,
    fuel_consumption FLOAT NOT NULL,
    rpm FLOAT NOT NULL,
    gear_position INT,
    brake_pedal BOOLEAN,
    accelerator_pedal BOOLEAN,
    steering_wheel_angle FLOAT,
    driver_active BOOLEAN GENERATED ALWAYS AS (
        (speed > 0 AND driving_time > 0 AND accelerator_pedal IS TRUE)
    ) STORED,
    predictions FLOAT,
    FOREIGN KEY (driver_id) REFERENCES drivers(id)
);


CREATE TABLE camera_data (
    id SERIAL PRIMARY KEY,
    driver_id INT NOT NULL,
    timestamp TIMESTAMP TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    eye_aspect_ratio FLOAT NOT NULL,
    mouth_aspect_ratio FLOAT NOT NULL,
    drowsiness_detected BOOLEAN,
    yawning_detected BOOLEAN,
    FOREIGN KEY (driver_id) REFERENCES drivers(id)
);


CREATE TABLE combined_predictions (
    id SERIAL PRIMARY KEY,  
    driver_id INT NOT NULL,  
    latest_timestamp_camera TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,  
    eye_aspect_ratio FLOAT,  
    mouth_aspect_ratio FLOAT,  
    latest_timestamp_tachograph TTIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP, 
    combined_prediction FLOAT,  
    fatigue_score FLOAT,  
    drowsiness_score FLOAT,  
    fatigue_detected BOOLEAN,  
    drowsiness_detected BOOLEAN,  
	predictions	FLOAT,
    ada_fatigue_detected BOOLEAN,
    FOREIGN KEY (driver_id) REFERENCES drivers(id)
);

