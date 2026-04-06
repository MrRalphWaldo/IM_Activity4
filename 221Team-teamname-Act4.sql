-- ============================================================================
-- Facility/Equipment Borrowing System - Database Schema
-- ============================================================================
-- Database: equipments
-- ============================================================================

-- Drop existing database if it exists
DROP DATABASE IF EXISTS equipments;

-- Create database
CREATE DATABASE equipments;
USE equipments;

-- ============================================================================
-- TABLE: USERS (All system users with role-based access)
-- ============================================================================
CREATE TABLE USERS (
    user_id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    phone VARCHAR(20),
    role ENUM('CUSTODIAN', 'BORROWER', 'ADMIN', 'FACULTY') NOT NULL,
    status ENUM('ACTIVE', 'INACTIVE') DEFAULT 'ACTIVE',
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT chk_username_length CHECK (CHAR_LENGTH(username) >= 3),
    CONSTRAINT chk_email_format CHECK (email LIKE '%@%.%')
);

-- ============================================================================
-- TABLE: STUDENTS (Extract from SLU database or manual entry)
-- ============================================================================
CREATE TABLE STUDENTS (
    student_id INT PRIMARY KEY,
    user_id INT NOT NULL UNIQUE,
    student_number VARCHAR(20) NOT NULL UNIQUE,
    course VARCHAR(50),
    year_level INT,
    FOREIGN KEY (user_id) REFERENCES USERS(user_id) ON DELETE CASCADE,
    CONSTRAINT chk_year_level CHECK (year_level BETWEEN 1 AND 4)
);

-- ============================================================================
-- TABLE: CATEGORIES (Equipment categories)
-- ============================================================================
CREATE TABLE CATEGORIES (
    category_id INT PRIMARY KEY AUTO_INCREMENT,
    category_name VARCHAR(100) NOT NULL UNIQUE,
    description TEXT,
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ============================================================================
-- TABLE: EQUIPMENT (Lab equipment and accessories)
-- ============================================================================
CREATE TABLE EQUIPMENT (
    equipment_id INT PRIMARY KEY AUTO_INCREMENT,
    barcode VARCHAR(50) NOT NULL UNIQUE,
    equipment_name VARCHAR(100) NOT NULL,
    category_id INT NOT NULL,
    description TEXT,
    status ENUM('AVAILABLE', 'CHECKED_OUT', 'MAINTENANCE', 'RETIRED') DEFAULT 'AVAILABLE',
    purchase_date DATE,
    value DECIMAL(10, 2),
    location VARCHAR(100),
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (category_id) REFERENCES CATEGORIES(category_id) ON DELETE RESTRICT,
    CONSTRAINT chk_value CHECK (value > 0)
);

-- ============================================================================
-- TABLE: LABORATORY_CLASSES (CIS laboratory classes)
-- ============================================================================
CREATE TABLE LABORATORY_CLASSES (
    class_id INT PRIMARY KEY AUTO_INCREMENT,
    class_code VARCHAR(20) NOT NULL UNIQUE,
    class_name VARCHAR(100) NOT NULL,
    instructor_id INT,
    semester VARCHAR(10),
    academic_year VARCHAR(10),
    time_slot VARCHAR(50),
    room_location VARCHAR(50),
    FOREIGN KEY (instructor_id) REFERENCES USERS(user_id) ON DELETE SET NULL
);

-- ============================================================================
-- TABLE: CLASS_ENROLLMENT (Many-to-Many: STUDENTS to LABORATORY_CLASSES)
-- ============================================================================
CREATE TABLE CLASS_ENROLLMENT (
    enrollment_id INT PRIMARY KEY AUTO_INCREMENT,
    student_id INT NOT NULL,
    class_id INT NOT NULL,
    enrollment_date DATE,
    FOREIGN KEY (student_id) REFERENCES STUDENTS(student_id) ON DELETE CASCADE,
    FOREIGN KEY (class_id) REFERENCES LABORATORY_CLASSES(class_id) ON DELETE CASCADE,
    UNIQUE KEY unique_enrollment (student_id, class_id)
);

-- ============================================================================
-- TABLE: EVENTS (Non-class activities: training, recruitment, exams, etc.)
-- ============================================================================
CREATE TABLE EVENTS (
    event_id INT PRIMARY KEY AUTO_INCREMENT,
    event_name VARCHAR(100) NOT NULL,
    event_type ENUM('TRAINING', 'RECRUITMENT', 'CERTIFICATION_EXAM', 'MEETING', 'LECTURE', 'OTHER') NOT NULL,
    organizer_id INT NOT NULL,
    event_date DATE NOT NULL,
    start_time TIME,
    end_time TIME,
    location VARCHAR(100),
    description TEXT,
    approval_status ENUM('PENDING', 'APPROVED', 'REJECTED') DEFAULT 'PENDING',
    approved_by INT,
    approval_date DATETIME,
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (organizer_id) REFERENCES USERS(user_id) ON DELETE CASCADE,
    FOREIGN KEY (approved_by) REFERENCES USERS(user_id) ON DELETE SET NULL
);

-- ============================================================================
-- TABLE: BORROW_RECORDS (Main borrowing transactions)
-- ============================================================================
CREATE TABLE BORROW_RECORDS (
    borrow_id INT PRIMARY KEY AUTO_INCREMENT,
    borrower_id INT NOT NULL,
    custodian_id INT NOT NULL,
    class_id INT,
    event_id INT,
    borrow_type ENUM('CLASS_ACTIVITY', 'NON_CLASS_ACTIVITY') NOT NULL,
    borrow_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    expected_return_date DATE NOT NULL,
    actual_return_date DATETIME,
    return_status ENUM('ACTIVE', 'RETURNED', 'OVERDUE') DEFAULT 'ACTIVE',
    notes TEXT,
    FOREIGN KEY (borrower_id) REFERENCES USERS(user_id) ON DELETE CASCADE,
    FOREIGN KEY (custodian_id) REFERENCES USERS(user_id) ON DELETE RESTRICT,
    FOREIGN KEY (class_id) REFERENCES LABORATORY_CLASSES(class_id) ON DELETE SET NULL,
    FOREIGN KEY (event_id) REFERENCES EVENTS(event_id) ON DELETE SET NULL,
    CONSTRAINT chk_expected_return CHECK (expected_return_date >= CURDATE())
);

-- ============================================================================
-- TABLE: BORROW_DETAILS (Items borrowed in each transaction - Associative)
-- ============================================================================
CREATE TABLE BORROW_DETAILS (
    detail_id INT PRIMARY KEY AUTO_INCREMENT,
    borrow_id INT NOT NULL,
    equipment_id INT NOT NULL,
    quantity INT DEFAULT 1,
    condition_on_checkout ENUM('GOOD', 'FAIR', 'DAMAGED') DEFAULT 'GOOD',
    condition_on_return ENUM('GOOD', 'FAIR', 'DAMAGED', 'NOT_RETURNED') DEFAULT 'NOT_RETURNED',
    damage_notes TEXT,
    FOREIGN KEY (borrow_id) REFERENCES BORROW_RECORDS(borrow_id) ON DELETE CASCADE,
    FOREIGN KEY (equipment_id) REFERENCES EQUIPMENT(equipment_id) ON DELETE RESTRICT,
    CONSTRAINT chk_quantity CHECK (quantity > 0)
);

-- ============================================================================
-- INDEXES for Performance Optimization
-- ============================================================================
CREATE INDEX idx_users_role ON USERS(role);
CREATE INDEX idx_users_status ON USERS(status);
CREATE INDEX idx_equipment_barcode ON EQUIPMENT(barcode);
CREATE INDEX idx_equipment_status ON EQUIPMENT(status);
CREATE INDEX idx_borrow_borrower ON BORROW_RECORDS(borrower_id);
CREATE INDEX idx_borrow_custodian ON BORROW_RECORDS(custodian_id);
CREATE INDEX idx_borrow_status ON BORROW_RECORDS(return_status);
CREATE INDEX idx_borrow_dates ON BORROW_RECORDS(borrow_date, actual_return_date);
CREATE INDEX idx_class_enrollment_student ON CLASS_ENROLLMENT(student_id);
CREATE INDEX idx_events_date ON EVENTS(event_date);

-- ============================================================================
-- SAMPLE DATA INSERTION
-- ============================================================================

-- Insert Categories
INSERT INTO CATEGORIES (category_name, description) VALUES
('Laptop Computers', 'Dell, HP, Lenovo laptops for lab use'),
('Projectors', 'LCD and LED projectors for lectures and events'),
('Network Equipment', 'Routers, switches, cables'),
('Oscilloscopes', 'Digital and analog oscilloscopes for electronics lab'),
('Multimeters', 'Digital multimeters for circuit testing'),
('Programming Equipment', 'Microcontrollers, Arduino kits, sensors'),
('Video Equipment', 'Cameras and recording devices'),
('Audio Equipment', 'Headphones, microphones, speakers');

-- Insert Users (Custodians)
INSERT INTO USERS (username, password, first_name, last_name, email, phone, role, status) VALUES
('custodian01', 'pass123', 'Maria', 'Santos', 'maria.santos@slu.edu.ph', '09123456789', 'CUSTODIAN', 'ACTIVE'),
('custodian02', 'pass123', 'Juan', 'Dela Cruz', 'juan.delacruz@slu.edu.ph', '09123456790', 'CUSTODIAN', 'ACTIVE');

-- Insert Users (Faculty/Instructors)
INSERT INTO USERS (username, password, first_name, last_name, email, phone, role, status) VALUES
('dr_garcia', 'pass123', 'Dr. Carlos', 'Garcia', 'carlos.garcia@slu.edu.ph', '09123456791', 'FACULTY', 'ACTIVE'),
('prof_lim', 'pass123', 'Prof. Anna', 'Lim', 'anna.lim@slu.edu.ph', '09123456792', 'FACULTY', 'ACTIVE');

-- Insert Users (Students/Borrowers)
INSERT INTO USERS (username, password, first_name, last_name, email, phone, role, status) VALUES
('student001', 'pass123', 'Jose', 'Rizal', 'jose.rizal@student.slu.edu.ph', '09187654321', 'BORROWER', 'ACTIVE'),
('student002', 'pass123', 'Maria', 'Clara', 'maria.clara@student.slu.edu.ph', '09187654322', 'BORROWER', 'ACTIVE'),
('student003', 'pass123', 'Andres', 'Bonifacio', 'andres.bonifacio@student.slu.edu.ph', '09187654323', 'BORROWER', 'ACTIVE'),
('student004', 'pass123', 'Emilio', 'Aguinaldo', 'emilio.aguinaldo@student.slu.edu.ph', '09187654324', 'BORROWER', 'ACTIVE'),
('student005', 'pass123', 'Apolinario', 'Mabini', 'apolinario.mabini@student.slu.edu.ph', '09187654325', 'BORROWER', 'ACTIVE');

-- Insert Users (Admin)
INSERT INTO USERS (username, password, first_name, last_name, email, phone, role, status) VALUES
('admin01', 'admin123', 'Admin', 'User', 'admin@slu.edu.ph', '09123456800', 'ADMIN', 'ACTIVE');

-- Insert Students (Extract from SLU database simulation)
INSERT INTO STUDENTS (student_id, user_id, student_number, course, year_level) VALUES
(1, 5, 'CS2023001', 'BS Computer Science', 2),
(2, 6, 'CS2023002', 'BS Computer Science', 2),
(3, 7, 'CS2022001', 'BS Computer Science', 3),
(4, 8, 'IT2023001', 'BS Information Technology', 2),
(5, 9, 'IT2022001', 'BS Information Technology', 3);

-- Insert Laboratory Classes
INSERT INTO LABORATORY_CLASSES (class_code, class_name, instructor_id, semester, academic_year, time_slot, room_location) VALUES
('CSIS301L', 'Data Structures Lab', 3, 'First', '2026-2027', 'MWF 1:00-3:00 PM', 'ICT Lab 101'),
('CSIS401L', 'Database Systems Lab', 4, 'First', '2026-2027', 'TTh 10:00 AM-12:00 PM', 'ICT Lab 102'),
('CSIS302L', 'Computer Architecture Lab', 3, 'First', '2026-2027', 'MWF 3:00-5:00 PM', 'ICT Lab 103');

-- Insert Class Enrollments
INSERT INTO CLASS_ENROLLMENT (student_id, class_id, enrollment_date) VALUES
(1, 1, '2026-01-15'),
(2, 1, '2026-01-15'),
(3, 1, '2026-01-15'),
(4, 2, '2026-01-15'),
(5, 2, '2026-01-15'),
(1, 3, '2026-01-15'),
(3, 3, '2026-01-15');

-- Insert Equipment
INSERT INTO EQUIPMENT (barcode, equipment_name, category_id, description, status, purchase_date, value, location) VALUES
('BAR001', 'Dell Latitude 5000 Laptop', 1, '15.6 inch FHD, Intel i5, 8GB RAM', 'AVAILABLE', '2024-06-15', 45000, 'ICT Lab 101'),
('BAR002', 'HP Pavilion Laptop', 1, '14 inch HD, Intel i7, 16GB RAM', 'AVAILABLE', '2024-07-20', 55000, 'ICT Lab 101'),
('BAR003', 'Lenovo ThinkPad Laptop', 1, '13 inch FHD, Intel i5, 8GB RAM', 'AVAILABLE', '2024-08-10', 48000, 'ICT Lab 102'),
('BAR004', 'Epson EB-U42 Projector', 2, 'LCD Projector, 3600 Lumens, WUXGA', 'AVAILABLE', '2024-05-12', 35000, 'ICT Lab 101'),
('BAR005', 'Sony HDR Projector', 2, 'LED Projector, 2500 Lumens', 'AVAILABLE', '2024-09-01', 28000, 'ICT Lab 102'),
('BAR006', 'Cisco Catalyst Switch', 3, '24-port Gigabit Ethernet', 'AVAILABLE', '2023-11-05', 65000, 'Server Room'),
('BAR007', 'Fluke Oscilloscope', 4, 'Digital Oscilloscope, 100 MHz', 'AVAILABLE', '2024-03-18', 150000, 'Electronics Lab'),
('BAR008', 'Agilent Oscilloscope', 4, 'Digital Oscilloscope, 50 MHz', 'AVAILABLE', '2024-04-22', 120000, 'Electronics Lab'),
('BAR009', 'Fluke Multimeter 87', 5, 'Digital Multimeter, Handheld', 'AVAILABLE', '2024-02-14', 15000, 'Electronics Lab'),
('BAR010', 'Arduino Uno Kit', 6, 'Complete Arduino development kit with sensors', 'AVAILABLE', '2024-08-28', 5000, 'ICT Lab 103');

-- Insert Events (Non-class activities)
INSERT INTO EVENTS (event_name, event_type, organizer_id, event_date, start_time, end_time, location, description, approval_status, approved_by, approval_date) VALUES
('CIS Bootcamp 2026', 'TRAINING', 3, '2026-04-10', '09:00:00', '17:00:00', 'ICT Building', 'Spring bootcamp for programming skills', 'APPROVED', 1, '2026-03-20 10:30:00'),
('Tech Recruitment Drive', 'RECRUITMENT', 4, '2026-04-15', '10:00:00', '16:00:00', 'Gymnasium', 'Campus recruitment for tech companies', 'APPROVED', 1, '2026-03-22 14:15:00'),
('Python Certification Exam', 'CERTIFICATION_EXAM', 5, '2026-04-20', '13:00:00', '16:00:00', 'ICT Lab 102', 'Official Python certification examination', 'PENDING', NULL, NULL);

-- Insert Borrow Records (Class activities)
INSERT INTO BORROW_RECORDS (borrower_id, custodian_id, class_id, borrow_type, borrow_date, expected_return_date, actual_return_date, return_status, notes) VALUES
(5, 1, 1, 'CLASS_ACTIVITY', '2026-03-20 08:00:00', '2026-03-20', '2026-03-20 15:30:00', 'RETURNED', 'Used for Data Structures Lab'),
(6, 1, 1, 'CLASS_ACTIVITY', '2026-03-20 08:00:00', '2026-03-20', '2026-03-20 15:30:00', 'RETURNED', 'Used for Data Structures Lab'),
(7, 2, 1, 'CLASS_ACTIVITY', '2026-03-22 08:00:00', '2026-03-22', NULL, 'ACTIVE', 'Still in use'),
(8, 1, 2, 'CLASS_ACTIVITY', '2026-03-21 10:00:00', '2026-03-21', '2026-03-21 11:45:00', 'RETURNED', 'Database Systems Lab session'),
(9, 2, 2, 'CLASS_ACTIVITY', '2026-03-21 10:00:00', '2026-03-21', NULL, 'ACTIVE', 'Still in use');

-- Insert Borrow Records (Non-class activities)
INSERT INTO BORROW_RECORDS (borrower_id, custodian_id, event_id, borrow_type, borrow_date, expected_return_date, actual_return_date, return_status, notes) VALUES
(3, 1, 1, 'NON_CLASS_ACTIVITY', '2026-04-08 09:00:00', '2026-04-10', '2026-04-10 18:00:00', 'RETURNED', 'CIS Bootcamp equipment setup'),
(4, 2, 2, 'NON_CLASS_ACTIVITY', '2026-04-14 08:00:00', '2026-04-15', NULL, 'ACTIVE', 'Tech Recruitment Drive - Projectors and laptops');

-- Insert Borrow Details
INSERT INTO BORROW_DETAILS (borrow_id, equipment_id, quantity, condition_on_checkout, condition_on_return, damage_notes) VALUES
(1, 1, 1, 'GOOD', 'GOOD', NULL),
(1, 4, 1, 'GOOD', 'GOOD', NULL),
(2, 2, 1, 'GOOD', 'GOOD', NULL),
(3, 3, 1, 'GOOD', 'GOOD', NULL),
(4, 1, 1, 'GOOD', 'GOOD', NULL),
(4, 7, 1, 'GOOD', 'GOOD', NULL),
(5, 2, 1, 'GOOD', 'NOT_RETURNED', NULL),
(6, 5, 1, 'GOOD', 'GOOD', NULL),
(6, 1, 2, 'GOOD', 'GOOD', NULL),
(7, 4, 1, 'GOOD', 'NOT_RETURNED', NULL),
(7, 5, 1, 'GOOD', 'NOT_RETURNED', NULL),
(8, 10, 3, 'GOOD', 'GOOD', 'One unit had loose connection');

-- ============================================================================
-- END OF SCHEMA AND SAMPLE DATA
-- ============================================================================

