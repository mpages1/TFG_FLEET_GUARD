DO
$$
BEGIN
   IF NOT EXISTS (SELECT FROM pg_database WHERE datname = 'FLEET_GUARD') THEN
      CREATE DATABASE "FLEET_GUARD";
   END IF;
END
$$;  

\connect "FLEET_GUARD";

CREATE TABLE roles (
    id SERIAL PRIMARY KEY,
    name VARCHAR(50) UNIQUE NOT NULL
);

CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    email VARCHAR(255) UNIQUE NOT NULL,
    role_id INTEGER NOT NULL,
    password VARCHAR(255) NOT NULL,
    created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_users_role FOREIGN KEY (role_id) REFERENCES roles(id) ON DELETE CASCADE
);

CREATE TABLE administrators (
    id SERIAL PRIMARY KEY,
    user_id INTEGER NOT NULL UNIQUE,
    name VARCHAR(255),
    phone VARCHAR(20),
    department VARCHAR(100),
    CONSTRAINT fk_admin_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE drivers (
    id SERIAL PRIMARY KEY,
    user_id INTEGER NOT NULL UNIQUE,
    name VARCHAR(255) NOT NULL,
    phone VARCHAR(20) UNIQUE NOT NULL,
    license_number VARCHAR(50) UNIQUE NOT NULL,
    date_of_birth DATE,
    address VARCHAR(255),
    CONSTRAINT fk_driver_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE tachograph_data (
    id SERIAL PRIMARY KEY,
    driver_id INTEGER NOT NULL,
    timestamp TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    driving_time FLOAT NOT NULL,
    driving_distance FLOAT NOT NULL,
    speed FLOAT NOT NULL,
    break_time FLOAT NOT NULL,
    engine_hours FLOAT NOT NULL,
    fuel_consumption FLOAT NOT NULL,
    rpm FLOAT NOT NULL,
    gear_position INTEGER,
    brake_pedal BOOLEAN DEFAULT FALSE,
    accelerator_pedal BOOLEAN DEFAULT FALSE,
    steering_wheel_angle FLOAT,
    driver_active BOOLEAN GENERATED ALWAYS AS (
        (speed > 0 AND driving_time > 0 AND accelerator_pedal IS TRUE)
    ) STORED,
    predictions FLOAT,
    CONSTRAINT fk_tacho_driver FOREIGN KEY (driver_id) REFERENCES drivers(id) ON DELETE CASCADE
);

CREATE TABLE camera_data (
    id SERIAL PRIMARY KEY,
    driver_id INTEGER NOT NULL,
    timestamp TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    eye_aspect_ratio FLOAT NOT NULL,
    mouth_aspect_ratio FLOAT NOT NULL,
    drowsiness_detected BOOLEAN DEFAULT FALSE,
    yawning_detected BOOLEAN DEFAULT FALSE,
    CONSTRAINT fk_camera_driver FOREIGN KEY (driver_id) REFERENCES drivers(id) ON DELETE CASCADE
);

CREATE TABLE combined_predictions (
    id SERIAL PRIMARY KEY,
    driver_id INTEGER NOT NULL,
    latest_timestamp_camera TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    eye_aspect_ratio FLOAT,
    mouth_aspect_ratio FLOAT,
    latest_timestamp_tachograph TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    combined_prediction FLOAT,
    fatigue_score FLOAT,
    drowsiness_score FLOAT,
    fatigue_detected BOOLEAN DEFAULT FALSE,
    drowsiness_detected BOOLEAN DEFAULT FALSE,
    predictions FLOAT,
    ada_fatigue_detected BOOLEAN DEFAULT FALSE,
    CONSTRAINT fk_combined_driver FOREIGN KEY (driver_id) REFERENCES drivers(id) ON DELETE CASCADE
);


INSERT INTO roles (id, name) VALUES (1, 'admin');
INSERT INTO roles (id, name) VALUES (2, 'driver');

INSERT INTO users (id, email, role_id, password)
VALUES (1, 'admin@tfg.com', 1, '$2a$10$MPcGZey5H1RzHlhvdgieWeAX/suMCGDavsYWTvJZjMjzCH2HL8VSO'),
       (2, 'driver@tfg.com', 2, '$2a$10$MPcGZey5H1RzHlhvdgieWeAX/suMCGDavsYWTvJZjMjzCH2HL8VSO');

INSERT INTO administrators (user_id, name, phone, department)
VALUES (1, 'ADMIN', '654323273', 'IT');

INSERT INTO drivers (user_id, name, phone, license_number, date_of_birth, address)
VALUES (2, 'DRIVER', '634532452', 'Y543D4A', '2000-04-02', 'La Rambla 2 1');