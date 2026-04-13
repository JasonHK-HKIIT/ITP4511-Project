CREATE DATABASE IF NOT EXISTS itp4511_db;
USE itp4511_db;

-- =========================
-- 1. USERS
-- =========================
CREATE TABLE users
(
    id            INT AUTO_INCREMENT PRIMARY KEY,
    username      VARCHAR(50)  NOT NULL UNIQUE,
    password      VARCHAR(255) NOT NULL,
    full_name     VARCHAR(100) NOT NULL,
    phone         VARCHAR(20) NULL,
    role          ENUM('PATIENT', 'STAFF', 'ADMIN') NOT NULL,
    gender        ENUM('MALE', 'FEMALE') NULL,
    date_of_birth DATE NULL
);

-- =========================
-- 2. CLINICS
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

-- =========================
-- 3. SERVICES
-- =========================
CREATE TABLE services
(
    service_id               INT AUTO_INCREMENT PRIMARY KEY,
    service_name             VARCHAR(100) NOT NULL UNIQUE,
    description              VARCHAR(255),
    requires_approval        BOOLEAN      NOT NULL DEFAULT FALSE,
    default_duration_minutes INT          NOT NULL DEFAULT 15,
    status                   ENUM('ACTIVE', 'INACTIVE') NOT NULL DEFAULT 'ACTIVE'
);

-- =========================
-- 4. CLINIC_SERVICES
-- =========================
CREATE TABLE clinic_services
(
    clinic_service_id  INT AUTO_INCREMENT PRIMARY KEY,
    clinic_id          INT     NOT NULL,
    service_id         INT     NOT NULL,
    is_walkin_enabled  BOOLEAN NOT NULL DEFAULT TRUE,
    slot_capacity      INT     NOT NULL DEFAULT 1,
    max_daily_bookings INT NULL,
    status             ENUM('ACTIVE', 'INACTIVE') NOT NULL DEFAULT 'ACTIVE',

    CONSTRAINT fk_clinic_services_clinic
        FOREIGN KEY (clinic_id) REFERENCES clinics (id)
            ON DELETE CASCADE ON UPDATE CASCADE,

    CONSTRAINT fk_clinic_services_service
        FOREIGN KEY (service_id) REFERENCES services (service_id)
            ON DELETE CASCADE ON UPDATE CASCADE,

    CONSTRAINT uq_clinic_service UNIQUE (clinic_id, service_id),
    CONSTRAINT chk_slot_capacity CHECK (slot_capacity > 0),
    CONSTRAINT chk_max_daily_bookings CHECK (max_daily_bookings IS NULL OR max_daily_bookings > 0)
);

-- =========================
-- 5. TIMESLOTS
-- =========================
CREATE TABLE timeslots
(
    timeslot_id       INT AUTO_INCREMENT PRIMARY KEY,
    clinic_service_id INT  NOT NULL,
    slot_date         DATE NOT NULL,
    start_time        TIME NOT NULL,
    end_time          TIME NOT NULL,
    capacity          INT  NOT NULL,
    booked_count      INT  NOT NULL DEFAULT 0,
    status            ENUM('OPEN', 'FULL', 'CANCELLED', 'CLOSED') NOT NULL DEFAULT 'OPEN',

    CONSTRAINT fk_timeslots_clinic_service
        FOREIGN KEY (clinic_service_id) REFERENCES clinic_services (clinic_service_id)
            ON DELETE CASCADE ON UPDATE CASCADE,

    CONSTRAINT uq_timeslot UNIQUE (clinic_service_id, slot_date, start_time, end_time),
    CONSTRAINT chk_timeslot_capacity CHECK (capacity > 0),
    CONSTRAINT chk_booked_count CHECK (booked_count >= 0 AND booked_count <= capacity),
    CONSTRAINT chk_timeslot_time CHECK (end_time > start_time)
);

-- =========================
-- 6. APPOINTMENTS
-- =========================
CREATE TABLE appointments
(
    appointment_id       INT AUTO_INCREMENT PRIMARY KEY,
    patient_id           INT         NOT NULL,
    timeslot_id          INT         NOT NULL,
    appointment_no       VARCHAR(30) NOT NULL UNIQUE,
    status               ENUM('PENDING', 'CONFIRMED', 'ARRIVED', 'COMPLETED', 'NO_SHOW', 'CANCELLED') NOT NULL DEFAULT 'CONFIRMED',
    approval_status      ENUM('NOT_REQUIRED', 'PENDING', 'APPROVED', 'REJECTED') NOT NULL DEFAULT 'NOT_REQUIRED',
    booked_at            DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    rescheduled_from_id  INT NULL,
    cancelled_by_user_id INT NULL,
    cancel_reason        VARCHAR(255) NULL,
    attendance_marked_by INT NULL,
    attendance_marked_at DATETIME NULL,
    remarks              VARCHAR(255) NULL,

    CONSTRAINT fk_appointments_patient
        FOREIGN KEY (patient_id) REFERENCES users (id)
            ON DELETE RESTRICT ON UPDATE CASCADE,

    CONSTRAINT fk_appointments_timeslot
        FOREIGN KEY (timeslot_id) REFERENCES timeslots (timeslot_id)
            ON DELETE RESTRICT ON UPDATE CASCADE,

    CONSTRAINT fk_appointments_rescheduled_from
        FOREIGN KEY (rescheduled_from_id) REFERENCES appointments (appointment_id)
            ON DELETE SET NULL ON UPDATE CASCADE,

    CONSTRAINT fk_appointments_cancelled_by
        FOREIGN KEY (cancelled_by_user_id) REFERENCES users (id)
            ON DELETE SET NULL ON UPDATE CASCADE,

    CONSTRAINT fk_appointments_attendance_marked_by
        FOREIGN KEY (attendance_marked_by) REFERENCES users (id)
            ON DELETE SET NULL ON UPDATE CASCADE
);

-- Helps reduce accidental double-booking checks per patient and slot
CREATE UNIQUE INDEX uq_patient_timeslot ON appointments (patient_id, timeslot_id);

-- =========================
-- 7. QUEUE_TICKETS
-- =========================
CREATE TABLE queue_tickets
(
    queue_ticket_id        INT AUTO_INCREMENT PRIMARY KEY,
    patient_id             INT         NOT NULL,
    clinic_service_id      INT         NOT NULL,
    queue_date             DATE        NOT NULL,
    queue_number           VARCHAR(20) NOT NULL,
    status                 ENUM('WAITING', 'CALLED', 'SKIPPED', 'SERVED', 'EXPIRED', 'CANCELLED') NOT NULL DEFAULT 'WAITING',
    estimated_wait_minutes INT         NOT NULL DEFAULT 0,
    joined_at              DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    called_at              DATETIME NULL,
    served_at              DATETIME NULL,
    handled_by_staff_id    INT NULL,
    remarks                VARCHAR(255) NULL,

    CONSTRAINT fk_queue_patient
        FOREIGN KEY (patient_id) REFERENCES users (id)
            ON DELETE RESTRICT ON UPDATE CASCADE,

    CONSTRAINT fk_queue_clinic_service
        FOREIGN KEY (clinic_service_id) REFERENCES clinic_services (clinic_service_id)
            ON DELETE RESTRICT ON UPDATE CASCADE,

    CONSTRAINT fk_queue_handled_by_staff
        FOREIGN KEY (handled_by_staff_id) REFERENCES users (id)
            ON DELETE SET NULL ON UPDATE CASCADE,

    CONSTRAINT uq_queue_number UNIQUE (clinic_service_id, queue_date, queue_number),
    CONSTRAINT chk_estimated_wait CHECK (estimated_wait_minutes >= 0)
);

-- =========================
-- 8. NOTIFICATIONS
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
        FOREIGN KEY (related_appointment_id) REFERENCES appointments (appointment_id)
            ON DELETE SET NULL ON UPDATE CASCADE,

    CONSTRAINT fk_notifications_queue_ticket
        FOREIGN KEY (related_queue_ticket_id) REFERENCES queue_tickets (queue_ticket_id)
            ON DELETE SET NULL ON UPDATE CASCADE
);

-- =========================
-- 9. STAFF_CLINIC_ASSIGNMENTS
-- =========================
CREATE TABLE staff_clinic_assignments
(
    assignment_id INT AUTO_INCREMENT PRIMARY KEY,
    staff_user_id INT      NOT NULL,
    clinic_id     INT      NOT NULL,
    assigned_at   DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    status        ENUM('ACTIVE', 'INACTIVE') NOT NULL DEFAULT 'ACTIVE',

    CONSTRAINT fk_staff_assignment_user
        FOREIGN KEY (staff_user_id) REFERENCES users (id)
            ON DELETE CASCADE ON UPDATE CASCADE,

    CONSTRAINT fk_staff_assignment_clinic
        FOREIGN KEY (clinic_id) REFERENCES clinics (id)
            ON DELETE CASCADE ON UPDATE CASCADE,

    CONSTRAINT uq_staff_clinic UNIQUE (staff_user_id, clinic_id)
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
        FOREIGN KEY (service_id) REFERENCES services (service_id)
            ON DELETE SET NULL ON UPDATE CASCADE,

    CONSTRAINT fk_policy_updated_by
        FOREIGN KEY (updated_by_user_id) REFERENCES users (id)
            ON DELETE RESTRICT ON UPDATE CASCADE
);

-- Optional: avoid duplicate policy definitions for same scope
CREATE UNIQUE INDEX uq_policy_scope
    ON policy_settings (policy_name, clinic_id, service_id);
