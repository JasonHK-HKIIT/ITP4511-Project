CREATE DATABASE IF NOT EXISTS itp4511_db;
USE itp4511_db;

-- =========================
-- 1. CLINICS
-- =========================
CREATE TABLE clinics
(
    id                INT AUTO_INCREMENT PRIMARY KEY,
    location          VARCHAR(100) NOT NULL UNIQUE,
    opening_time      TIME         NOT NULL,
    closing_time      TIME         NOT NULL,
    is_walkin_enabled BOOLEAN      NOT NULL DEFAULT TRUE,
    status            ENUM('ACTIVE', 'SUSPENDED', 'CLOSED') NOT NULL DEFAULT 'ACTIVE'
);

-- Seed 5 clinics for development/testing
INSERT INTO clinics (location, opening_time, closing_time, is_walkin_enabled, status)
VALUES
    ('Chai Wan', '09:00:00', '18:00:00', TRUE, 'ACTIVE'),
    ('Tseung Kwan O', '09:00:00', '18:00:00', TRUE, 'ACTIVE'),
    ('Sha Tin', '09:00:00', '18:00:00', TRUE, 'ACTIVE'),
    ('Tuen Mun', '09:00:00', '18:00:00', FALSE, 'ACTIVE'),
    ('Tsing Yi', '09:00:00', '18:00:00', TRUE, 'ACTIVE')
ON DUPLICATE KEY UPDATE
    opening_time = VALUES(opening_time),
    closing_time = VALUES(closing_time),
    is_walkin_enabled = VALUES(is_walkin_enabled),
    status = VALUES(status);

-- =========================
-- 2. USERS
-- =========================
CREATE TABLE users
(
    id            INT AUTO_INCREMENT PRIMARY KEY,
    username      VARCHAR(50)  NOT NULL UNIQUE,
    password      VARCHAR(255) NOT NULL,
    full_name     VARCHAR(100) NOT NULL,
    phone         VARCHAR(20) NULL,
    role          ENUM('PATIENT', 'STAFF', 'ADMIN') NOT NULL,
    clinic_id     INT NULL,
    gender        ENUM('MALE', 'FEMALE') NULL,
    date_of_birth DATE NULL,

    INDEX idx_users_clinic_id (clinic_id),
    CONSTRAINT fk_users_clinic
        FOREIGN KEY (clinic_id) REFERENCES clinics (id)
            ON DELETE RESTRICT ON UPDATE CASCADE
);

-- Seed 5 default users for development/testing
INSERT INTO users (username, password, full_name, phone, role, clinic_id, gender, date_of_birth)
VALUES
    ('admin1', 'admin123', 'Admin User', '90000001', 'ADMIN', NULL, 'MALE', '1988-01-15'),
    ('staff1', 'staff123', 'Staff User', '90000002', 'STAFF', 1, 'FEMALE', '1992-06-20'),
    ('patient1', 'patient123', 'Patient One', '90000003', 'PATIENT', NULL, 'MALE', '2000-03-10'),
    ('patient2', 'patient123', 'Patient Two', '90000004', 'PATIENT', NULL, 'FEMALE', '2001-09-25'),
    ('patient3', 'patient123', 'Patient Three', '90000005', 'PATIENT', NULL, 'MALE', '1999-12-05')
    ON DUPLICATE KEY UPDATE
                         password = VALUES(password),
                         full_name = VALUES(full_name),
                         phone = VALUES(phone),
                         role = VALUES(role),
                         clinic_id = VALUES(clinic_id),
                         gender = VALUES(gender),
                         date_of_birth = VALUES(date_of_birth);


-- =========================
-- 3. SERVICES
-- =========================
CREATE TABLE services
(
    id                       INT AUTO_INCREMENT PRIMARY KEY,
    name                     VARCHAR(100) NOT NULL UNIQUE,
    default_duration_minutes INT          NOT NULL DEFAULT 15,
    status                   ENUM('ACTIVE', 'INACTIVE') NOT NULL DEFAULT 'ACTIVE'
);

-- Seed 3 services for development/testing
INSERT INTO services (name, default_duration_minutes, status)
VALUES
    ('General Consultation', 15, 'ACTIVE'),
    ('Vaccination', 10, 'ACTIVE'),
    ('Basic Health Screening', 30, 'ACTIVE')
ON DUPLICATE KEY UPDATE
    default_duration_minutes = VALUES(default_duration_minutes),
    status = VALUES(status);

-- =========================
-- 4. CLINIC_SERVICES
-- =========================
CREATE TABLE clinic_services
(
    id                 INT AUTO_INCREMENT PRIMARY KEY,
    clinic_id          INT NOT NULL,
    service_id         INT NOT NULL,
    slot_capacity      INT NOT NULL DEFAULT 1,
    max_daily_bookings INT NULL,
    status             ENUM('ACTIVE', 'INACTIVE') NOT NULL DEFAULT 'ACTIVE',

    CONSTRAINT fk_clinic_services_clinic
        FOREIGN KEY (clinic_id) REFERENCES clinics (id)
            ON DELETE CASCADE ON UPDATE CASCADE,

    CONSTRAINT fk_clinic_services_service
        FOREIGN KEY (service_id) REFERENCES services (id)
            ON DELETE CASCADE ON UPDATE CASCADE,

    CONSTRAINT uq_clinic_service UNIQUE (clinic_id, service_id),
    CONSTRAINT chk_slot_capacity CHECK (slot_capacity > 0),
    CONSTRAINT chk_max_daily_bookings CHECK (max_daily_bookings IS NULL OR max_daily_bookings > 0)
);

-- Seed clinic-service mappings manually (1-hour slot capacity by service duration)
-- General Consultation (15 mins) -> 4, Vaccination (10 mins) -> 6, Basic Health Screening (30 mins) -> 2
INSERT INTO clinic_services (clinic_id, service_id, slot_capacity, max_daily_bookings, status)
VALUES
    (1, 1, 4, 80, 'ACTIVE'),
    (1, 2, 6, 80, 'ACTIVE'),
    (1, 3, 2, 80, 'ACTIVE'),
    (2, 1, 4, 80, 'ACTIVE'),
    (2, 2, 6, 80, 'ACTIVE'),
    (2, 3, 2, 80, 'ACTIVE'),
    (3, 1, 4, 80, 'ACTIVE'),
    (3, 2, 6, 80, 'ACTIVE'),
    (3, 3, 2, 80, 'ACTIVE'),
    (4, 1, 4, 80, 'ACTIVE'),
    (4, 2, 6, 80, 'ACTIVE'),
    (4, 3, 2, 80, 'ACTIVE'),
    (5, 1, 4, 80, 'ACTIVE'),
    (5, 2, 6, 80, 'ACTIVE'),
    (5, 3, 2, 80, 'ACTIVE')
ON DUPLICATE KEY UPDATE
    slot_capacity = VALUES(slot_capacity),
    max_daily_bookings = VALUES(max_daily_bookings),
    status = VALUES(status);

-- =========================
-- 5. TIMESLOTS
-- =========================
CREATE TABLE timeslots
(
    id                INT AUTO_INCREMENT PRIMARY KEY,
    clinic_service_id INT  NOT NULL,
    slot_date         DATE NOT NULL,
    start_time        TIME NOT NULL,
    end_time          TIME NOT NULL,
    capacity          INT  NOT NULL,
    booked_count      INT  NOT NULL DEFAULT 0,
    status            ENUM('OPEN', 'FULL', 'CANCELLED', 'CLOSED') NOT NULL DEFAULT 'OPEN',

    CONSTRAINT fk_timeslots_clinic_service
        FOREIGN KEY (clinic_service_id) REFERENCES clinic_services (id)
            ON DELETE CASCADE ON UPDATE CASCADE,

    CONSTRAINT uq_timeslot UNIQUE (clinic_service_id, slot_date, start_time, end_time),
    CONSTRAINT chk_timeslot_capacity CHECK (capacity > 0),
    CONSTRAINT chk_booked_count CHECK (booked_count >= 0 AND booked_count <= capacity),
    CONSTRAINT chk_timeslot_time CHECK (end_time > start_time)
);

-- Seed timeslots for clinic 1 services (assumes clinic_service_id 1, 2, 3)
-- Dates: today and tomorrow; hourly windows: 09:00-13:00 and 14:00-18:00
INSERT INTO timeslots (clinic_service_id, slot_date, start_time, end_time, capacity, booked_count, status)
VALUES
    -- Today - clinic_service_id 1 (General Consultation, capacity 4)
    (1, CURDATE(), '09:00:00', '10:00:00', 4, 0, 'OPEN'),
    (1, CURDATE(), '10:00:00', '11:00:00', 4, 0, 'OPEN'),
    (1, CURDATE(), '11:00:00', '12:00:00', 4, 0, 'OPEN'),
    (1, CURDATE(), '12:00:00', '13:00:00', 4, 0, 'OPEN'),
    (1, CURDATE(), '14:00:00', '15:00:00', 4, 0, 'OPEN'),
    (1, CURDATE(), '15:00:00', '16:00:00', 4, 0, 'OPEN'),
    (1, CURDATE(), '16:00:00', '17:00:00', 4, 0, 'OPEN'),
    (1, CURDATE(), '17:00:00', '18:00:00', 4, 0, 'OPEN'),

    -- Today - clinic_service_id 2 (Vaccination, capacity 6)
    (2, CURDATE(), '09:00:00', '10:00:00', 6, 0, 'OPEN'),
    (2, CURDATE(), '10:00:00', '11:00:00', 6, 0, 'OPEN'),
    (2, CURDATE(), '11:00:00', '12:00:00', 6, 0, 'OPEN'),
    (2, CURDATE(), '12:00:00', '13:00:00', 6, 0, 'OPEN'),
    (2, CURDATE(), '14:00:00', '15:00:00', 6, 0, 'OPEN'),
    (2, CURDATE(), '15:00:00', '16:00:00', 6, 0, 'OPEN'),
    (2, CURDATE(), '16:00:00', '17:00:00', 6, 0, 'OPEN'),
    (2, CURDATE(), '17:00:00', '18:00:00', 6, 0, 'OPEN'),

    -- Today - clinic_service_id 3 (Basic Health Screening, capacity 2)
    (3, CURDATE(), '09:00:00', '10:00:00', 2, 0, 'OPEN'),
    (3, CURDATE(), '10:00:00', '11:00:00', 2, 0, 'OPEN'),
    (3, CURDATE(), '11:00:00', '12:00:00', 2, 0, 'OPEN'),
    (3, CURDATE(), '12:00:00', '13:00:00', 2, 0, 'OPEN'),
    (3, CURDATE(), '14:00:00', '15:00:00', 2, 0, 'OPEN'),
    (3, CURDATE(), '15:00:00', '16:00:00', 2, 0, 'OPEN'),
    (3, CURDATE(), '16:00:00', '17:00:00', 2, 0, 'OPEN'),
    (3, CURDATE(), '17:00:00', '18:00:00', 2, 0, 'OPEN'),

    -- Tomorrow - clinic_service_id 1 (General Consultation, capacity 4)
    (1, DATE_ADD(CURDATE(), INTERVAL 1 DAY), '09:00:00', '10:00:00', 4, 0, 'OPEN'),
    (1, DATE_ADD(CURDATE(), INTERVAL 1 DAY), '10:00:00', '11:00:00', 4, 4, 'OPEN'),
    (1, DATE_ADD(CURDATE(), INTERVAL 1 DAY), '11:00:00', '12:00:00', 4, 0, 'OPEN'),
    (1, DATE_ADD(CURDATE(), INTERVAL 1 DAY), '12:00:00', '13:00:00', 4, 0, 'OPEN'),
    (1, DATE_ADD(CURDATE(), INTERVAL 1 DAY), '14:00:00', '15:00:00', 4, 0, 'OPEN'),
    (1, DATE_ADD(CURDATE(), INTERVAL 1 DAY), '15:00:00', '16:00:00', 4, 0, 'OPEN'),
    (1, DATE_ADD(CURDATE(), INTERVAL 1 DAY), '16:00:00', '17:00:00', 4, 0, 'OPEN'),
    (1, DATE_ADD(CURDATE(), INTERVAL 1 DAY), '17:00:00', '18:00:00', 4, 0, 'OPEN'),

    -- Tomorrow - clinic_service_id 2 (Vaccination, capacity 6)
    (2, DATE_ADD(CURDATE(), INTERVAL 1 DAY), '09:00:00', '10:00:00', 6, 0, 'OPEN'),
    (2, DATE_ADD(CURDATE(), INTERVAL 1 DAY), '10:00:00', '11:00:00', 6, 0, 'OPEN'),
    (2, DATE_ADD(CURDATE(), INTERVAL 1 DAY), '11:00:00', '12:00:00', 6, 0, 'OPEN'),
    (2, DATE_ADD(CURDATE(), INTERVAL 1 DAY), '12:00:00', '13:00:00', 6, 0, 'OPEN'),
    (2, DATE_ADD(CURDATE(), INTERVAL 1 DAY), '14:00:00', '15:00:00', 6, 0, 'OPEN'),
    (2, DATE_ADD(CURDATE(), INTERVAL 1 DAY), '15:00:00', '16:00:00', 6, 0, 'OPEN'),
    (2, DATE_ADD(CURDATE(), INTERVAL 1 DAY), '16:00:00', '17:00:00', 6, 0, 'OPEN'),
    (2, DATE_ADD(CURDATE(), INTERVAL 1 DAY), '17:00:00', '18:00:00', 6, 0, 'OPEN'),

    -- Tomorrow - clinic_service_id 3 (Basic Health Screening, capacity 2)
    (3, DATE_ADD(CURDATE(), INTERVAL 1 DAY), '09:00:00', '10:00:00', 2, 0, 'OPEN'),
    (3, DATE_ADD(CURDATE(), INTERVAL 1 DAY), '10:00:00', '11:00:00', 2, 0, 'OPEN'),
    (3, DATE_ADD(CURDATE(), INTERVAL 1 DAY), '11:00:00', '12:00:00', 2, 0, 'OPEN'),
    (3, DATE_ADD(CURDATE(), INTERVAL 1 DAY), '12:00:00', '13:00:00', 2, 0, 'OPEN'),
    (3, DATE_ADD(CURDATE(), INTERVAL 1 DAY), '14:00:00', '15:00:00', 2, 0, 'OPEN'),
    (3, DATE_ADD(CURDATE(), INTERVAL 1 DAY), '15:00:00', '16:00:00', 2, 0, 'OPEN'),
    (3, DATE_ADD(CURDATE(), INTERVAL 1 DAY), '16:00:00', '17:00:00', 2, 0, 'OPEN'),
    (3, DATE_ADD(CURDATE(), INTERVAL 1 DAY), '17:00:00', '18:00:00', 2, 0, 'OPEN')
ON DUPLICATE KEY UPDATE
    id = id;

-- =========================
-- 6. APPOINTMENTS
-- =========================
CREATE TABLE appointments
(
    id              INT AUTO_INCREMENT PRIMARY KEY,
    patient_id      INT      NOT NULL,
    timeslot_id     INT      NOT NULL,
    status          ENUM('PENDING', 'CONFIRMED', 'ARRIVED', 'COMPLETED', 'NO_SHOW', 'CANCELLED') NOT NULL DEFAULT 'PENDING',
    approval_status ENUM('NOT_REQUIRED', 'PENDING', 'APPROVED', 'REJECTED') NOT NULL DEFAULT 'NOT_REQUIRED',
    booked_at       DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    cancel_reason   VARCHAR(255) NULL,

    CONSTRAINT fk_appointments_patient
        FOREIGN KEY (patient_id) REFERENCES users (id)
            ON DELETE RESTRICT ON UPDATE CASCADE,

    CONSTRAINT fk_appointments_timeslot
        FOREIGN KEY (timeslot_id) REFERENCES timeslots (id)
            ON DELETE RESTRICT ON UPDATE CASCADE
);

-- Helps reduce accidental double-booking checks per patient and slot
CREATE UNIQUE INDEX uq_patient_timeslot ON appointments (patient_id, timeslot_id);

-- Seed 2 appointments using fixed IDs from this seed order:
-- users: patient1=3, patient2=4; timeslots: today 09:00 (clinic_service_id 1)=1, tomorrow 09:00 (clinic_service_id 2)=33
INSERT INTO appointments (patient_id, timeslot_id, status, approval_status, booked_at, cancel_reason)
VALUES
    (3, 1, 'CONFIRMED', 'NOT_REQUIRED', NOW(), NULL),
    (4, 33, 'PENDING', 'NOT_REQUIRED', NOW(), NULL)
ON DUPLICATE KEY UPDATE
    status = VALUES(status),
    approval_status = VALUES(approval_status),
    cancel_reason = VALUES(cancel_reason);

-- Keep booked_count aligned for the seeded slots
UPDATE timeslots
SET booked_count = CASE
    WHEN id = 1 THEN 1
    WHEN id = 33 THEN 1
    ELSE booked_count
END
WHERE id IN (1, 33);

-- =========================
-- 7. QUEUES
-- =========================
CREATE TABLE queues
(
    id                INT AUTO_INCREMENT PRIMARY KEY,
    clinic_service_id INT  NOT NULL,
    queue_date        DATE NOT NULL,
    last_queue_number INT  NOT NULL DEFAULT 0,

    CONSTRAINT fk_queues_clinic_service
        FOREIGN KEY (clinic_service_id) REFERENCES clinic_services (id)
            ON DELETE CASCADE ON UPDATE CASCADE,

    CONSTRAINT uq_queues_clinic_service_date UNIQUE (clinic_service_id, queue_date),
    CONSTRAINT chk_last_queue_number CHECK (last_queue_number >= 0)
);

-- =========================
-- 8. QUEUE_TICKETS
-- =========================
CREATE TABLE queue_tickets
(
    id                     INT AUTO_INCREMENT PRIMARY KEY,
    patient_id             INT      NOT NULL,
    clinic_service_id      INT      NOT NULL,
    queue_date             DATE     NOT NULL,
    queue_number           INT      NOT NULL,
    status                 ENUM('WAITING', 'CALLED', 'COMPLETED', 'SKIPPED', 'LEFT') NOT NULL DEFAULT 'WAITING',
    estimated_wait_minutes INT      NOT NULL DEFAULT 0,
    joined_at              DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    called_at              DATETIME NULL,
    served_at              DATETIME NULL,

    CONSTRAINT fk_queue_patient
        FOREIGN KEY (patient_id) REFERENCES users (id)
            ON DELETE RESTRICT ON UPDATE CASCADE,

    CONSTRAINT fk_queue_clinic_service
        FOREIGN KEY (clinic_service_id) REFERENCES clinic_services (id)
            ON DELETE RESTRICT ON UPDATE CASCADE,

    CONSTRAINT uq_queue_number UNIQUE (clinic_service_id, queue_date, queue_number),
    CONSTRAINT chk_estimated_wait CHECK (estimated_wait_minutes >= 0)
);

-- =========================
-- 9. NOTIFICATIONS
-- =========================
CREATE TABLE notifications
(
    notification_id         INT AUTO_INCREMENT PRIMARY KEY,
    user_id                 INT          NOT NULL,
    type                    VARCHAR(30)  NOT NULL,
    title                   VARCHAR(100) NOT NULL,
    message                 VARCHAR(255) NOT NULL,
    related_appointment_id  INT NULL,
    related_queue_ticket_id INT NULL,

    CONSTRAINT fk_notifications_user
        FOREIGN KEY (user_id) REFERENCES users (id)
            ON DELETE CASCADE ON UPDATE CASCADE,

    CONSTRAINT fk_notifications_appointment
        FOREIGN KEY (related_appointment_id) REFERENCES appointments (id)
            ON DELETE SET NULL ON UPDATE CASCADE,

    CONSTRAINT fk_notifications_queue_ticket
        FOREIGN KEY (related_queue_ticket_id) REFERENCES queue_tickets (id)
            ON DELETE SET NULL ON UPDATE CASCADE
);

-- =========================
-- 10. POLICY_SETTINGS
-- =========================
CREATE TABLE policy_settings
(
    policy_id          INT AUTO_INCREMENT PRIMARY KEY,
    policy_name        VARCHAR(100) NOT NULL,
    policy_value       VARCHAR(100) NOT NULL,
    description        VARCHAR(255) NULL,
    clinic_id          INT NULL,
    service_id         INT NULL,
    is_active          BOOLEAN      NOT NULL DEFAULT TRUE,
    updated_by_user_id INT          NOT NULL,

    CONSTRAINT fk_policy_clinic
        FOREIGN KEY (clinic_id) REFERENCES clinics (id)
            ON DELETE SET NULL ON UPDATE CASCADE,

    CONSTRAINT fk_policy_service
        FOREIGN KEY (service_id) REFERENCES services (id)
            ON DELETE SET NULL ON UPDATE CASCADE,

    CONSTRAINT fk_policy_updated_by
        FOREIGN KEY (updated_by_user_id) REFERENCES users (id)
            ON DELETE RESTRICT ON UPDATE CASCADE
);

-- Optional: avoid duplicate policy definitions for same scope
CREATE UNIQUE INDEX uq_policy_scope
    ON policy_settings (policy_name, clinic_id, service_id);
