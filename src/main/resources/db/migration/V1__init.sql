-- Create database
CREATE DATABASE binaa_center;
USE binaa_center;

-- Users table
CREATE TABLE Users (
    user_id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role ENUM('admin','staff') NOT NULL
);

-- Staff table
CREATE TABLE Staff (
    staff_id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    role VARCHAR(50) NOT NULL,
    contact_number VARCHAR(20) NOT NULL
);

-- Cases table
CREATE TABLE Cases (
    case_id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    age TINYINT UNSIGNED NOT NULL,
    guardian_name VARCHAR(100) NOT NULL,
    contact_number VARCHAR(20) NOT NULL,
    special_needs TEXT,
    admission_date DATE NOT NULL,
    status ENUM('active','inactive') NOT NULL DEFAULT 'active',
    emergency_contact VARCHAR(100) NOT NULL,
    medical_history TEXT,
    current_medications TEXT,
    allergies TEXT,
    school_name VARCHAR(100),
    grade_level VARCHAR(20),
    primary_diagnosis VARCHAR(100),
    secondary_diagnosis VARCHAR(100),
    insurance_info TEXT
);

-- Sessions table
CREATE TABLE Sessions (
    session_id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    case_id INT UNSIGNED,
    purpose VARCHAR(255) NOT NULL,
    session_date DATETIME NOT NULL,
    notes TEXT,
    staff_id INT UNSIGNED,
    session_type ENUM('individual','group') NOT NULL,
    attendance_status ENUM('present','absent') NOT NULL,
    duration SMALLINT UNSIGNED NOT NULL,
    goals_achieved TEXT,
    next_session_plan TEXT,
    attachments VARCHAR(255),
    FOREIGN KEY (case_id) REFERENCES Cases(case_id),
    FOREIGN KEY (staff_id) REFERENCES Staff(staff_id)
);

-- Payments table
CREATE TABLE Payments (
    payment_id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    case_id INT UNSIGNED,
    session_id INT UNSIGNED,
    amount DECIMAL(10,2) NOT NULL,
    payment_date DATETIME NOT NULL,
    description VARCHAR(255),
    payment_method ENUM('cash','card','bank') NOT NULL,
    invoice_number VARCHAR(50) NOT NULL UNIQUE,
    payment_status ENUM('paid','pending','overdue') NOT NULL DEFAULT 'pending',
    discount_applied DECIMAL(10,2) DEFAULT 0.00,
    tax_amount DECIMAL(10,2) DEFAULT 0.00,
    FOREIGN KEY (case_id) REFERENCES Cases(case_id),
    FOREIGN KEY (session_id) REFERENCES Sessions(session_id)
);

-- Reports table
CREATE TABLE Reports (
    report_id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    case_id INT UNSIGNED,
    session_id INT UNSIGNED,
    report_content TEXT,
    created_date DATE,
    FOREIGN KEY (case_id) REFERENCES Cases(case_id),
    FOREIGN KEY (session_id) REFERENCES Sessions(session_id)
);

-- Assessments table
CREATE TABLE Assessments (
    assessment_id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    case_id INT UNSIGNED,
    assessment_type ENUM('IQ','psychological','learning_difficulties'),
    score DECIMAL(5,2),
    assessment_date DATE,
    next_assessment_date DATE,
    assessor_id INT UNSIGNED,
    recommendations TEXT,
    status ENUM('completed','pending','scheduled'),
    FOREIGN KEY (case_id) REFERENCES Cases(case_id),
    FOREIGN KEY (assessor_id) REFERENCES Staff(staff_id)
);

-- Treatment Plans table
CREATE TABLE Treatment_Plans (
    plan_id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    case_id INT UNSIGNED,
    goals TEXT,
    start_date DATE,
    end_date DATE,
    status ENUM('active','completed','on-hold'),
    progress_notes TEXT,
    specialist_id INT UNSIGNED,
    FOREIGN KEY (case_id) REFERENCES Cases(case_id),
    FOREIGN KEY (specialist_id) REFERENCES Staff(staff_id)
);

-- Appointments table
CREATE TABLE Appointments (
    appointment_id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    case_id INT UNSIGNED,
    staff_id INT UNSIGNED,
    date_time DATETIME NOT NULL,
    status ENUM('scheduled','completed','cancelled'),
    type ENUM('assessment','therapy','consultation'),
    notes TEXT,
    FOREIGN KEY (case_id) REFERENCES Cases(case_id),
    FOREIGN KEY (staff_id) REFERENCES Staff(staff_id)
);

-- Documents table
CREATE TABLE Documents (
    document_id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    case_id INT UNSIGNED,
    type ENUM('medical','assessment','progress_report'),
    file_path VARCHAR(255),
    upload_date DATE,
    uploaded_by INT UNSIGNED,
    FOREIGN KEY (case_id) REFERENCES Cases(case_id),
    FOREIGN KEY (uploaded_by) REFERENCES Users(user_id)
);

-- Expenses table
CREATE TABLE Expenses (
    expense_id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    category ENUM('salary','rent','utilities','supplies','marketing','maintenance','other'),
    amount DECIMAL(10,2) NOT NULL,
    date DATE NOT NULL,
    description VARCHAR(255),
    receipt_path VARCHAR(255),
    paid_by INT UNSIGNED,
    payment_method ENUM('cash','card','bank'),
    FOREIGN KEY (paid_by) REFERENCES Staff(staff_id)
);

-- Fidelity Points table
CREATE TABLE Fidelity_Points (
    point_id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    case_id INT UNSIGNED,
    points INT NOT NULL,
    transaction_date DATETIME NOT NULL,
    type ENUM('earn','redeem'),
    source VARCHAR(50),
    expiry_date DATE,
    status ENUM('active','used','expired'),
    FOREIGN KEY (case_id) REFERENCES Cases(case_id)
);

-- Rewards table
CREATE TABLE Rewards (
    reward_id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    points_required INT NOT NULL,
    description TEXT,
    valid_until DATE,
    status ENUM('active','inactive')
);

-- Add indexes for better performance
CREATE INDEX idx_cases_name ON Cases(name);
CREATE INDEX idx_cases_status ON Cases(status);
CREATE INDEX idx_sessions_date ON Sessions(session_date);
CREATE INDEX idx_payments_date ON Payments(payment_date);
CREATE INDEX idx_appointments_datetime ON Appointments(date_time);
CREATE INDEX idx_fidelity_points_status ON Fidelity_Points(status);