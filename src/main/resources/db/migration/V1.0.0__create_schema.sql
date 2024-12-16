-- Create database

-- Users table
CREATE TABLE IF NOT EXISTS users (
    user_id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role ENUM('ADMIN','STAFF') NOT NULL
);

-- Staff table
CREATE TABLE IF NOT EXISTS staff (
    staff_id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    role VARCHAR(50) NOT NULL,
    contact_number VARCHAR(20) NOT NULL
);

-- Cases table
CREATE TABLE IF NOT EXISTS cases (
    case_id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    age TINYINT UNSIGNED NOT NULL,
    guardian_name VARCHAR(100) NOT NULL,
    contact_number VARCHAR(20) NOT NULL,
    special_needs TEXT,
    admission_date DATE NOT NULL,
    status ENUM('ACTIVE','INACTIVE') NOT NULL DEFAULT 'ACTIVE',
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
CREATE TABLE IF NOT EXISTS sessions (
    session_id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    case_id INT UNSIGNED,
    purpose VARCHAR(255) NOT NULL,
    session_date DATETIME NOT NULL,
    notes TEXT,
    staff_id INT UNSIGNED,
    session_type ENUM('INDIVIDUAL','GROUP') NOT NULL,
    attendance_status ENUM('PRESENT','ABSENT') NOT NULL,
    duration SMALLINT UNSIGNED NOT NULL,
    goals_achieved TEXT,
    next_session_plan TEXT,
    attachments VARCHAR(255),
    FOREIGN KEY (case_id) REFERENCES cases(case_id),
    FOREIGN KEY (staff_id) REFERENCES staff(staff_id)
);

-- Payments table
CREATE TABLE IF NOT EXISTS payments (
    payment_id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    case_id INT UNSIGNED,
    session_id INT UNSIGNED,
    amount DECIMAL(10,2) NOT NULL,
    payment_date DATETIME NOT NULL,
    description VARCHAR(255),
    payment_method ENUM('CASH','CARD','BANK') NOT NULL,
    invoice_number VARCHAR(50) NOT NULL UNIQUE,
    payment_status ENUM('PAID','PENDING','OVERDUE') NOT NULL DEFAULT 'PENDING',
    discount_applied DECIMAL(10,2) DEFAULT 0.00,
    tax_amount DECIMAL(10,2) DEFAULT 0.00,
    FOREIGN KEY (case_id) REFERENCES cases(case_id),
    FOREIGN KEY (session_id) REFERENCES sessions(session_id)
);

-- Reports table
CREATE TABLE IF NOT EXISTS reports (
    report_id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    case_id INT UNSIGNED NOT NULL,
    session_id INT UNSIGNED,
    report_content TEXT NOT NULL,
    created_date DATE NOT NULL,
    report_type VARCHAR(20) NOT NULL,
    created_by INT UNSIGNED,
    template_id VARCHAR(50),
    FOREIGN KEY (case_id) REFERENCES cases(case_id),
    FOREIGN KEY (session_id) REFERENCES sessions(session_id),
    FOREIGN KEY (created_by) REFERENCES users(user_id)
);

-- Assessments table
CREATE TABLE IF NOT EXISTS assessments (
    assessment_id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    case_id INT UNSIGNED,
    assessment_type ENUM('IQ','PSYCHOLOGICAL','LEARNING_DIFFICULTIES'),
    score DECIMAL(5,2),
    assessment_date DATE,
    next_assessment_date DATE,
    assessor_id INT UNSIGNED,
    recommendations TEXT,
    status ENUM('COMPLETED','PENDING','SCHEDULED'),
    FOREIGN KEY (case_id) REFERENCES cases(case_id),
    FOREIGN KEY (assessor_id) REFERENCES staff(staff_id)
);

-- Treatment Plans table
CREATE TABLE IF NOT EXISTS treatment_plans (
    plan_id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    case_id INT UNSIGNED,
    goals TEXT,
    start_date DATE,
    end_date DATE,
    status ENUM('ACTIVE','COMPLETED','ON_HOLD'),
    progress_notes TEXT,
    specialist_id INT UNSIGNED,
    FOREIGN KEY (case_id) REFERENCES cases(case_id),
    FOREIGN KEY (specialist_id) REFERENCES staff(staff_id)
);

-- Appointments table
CREATE TABLE IF NOT EXISTS appointments (
    appointment_id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    case_id INT UNSIGNED,
    staff_id INT UNSIGNED,
    date_time DATETIME NOT NULL,
    status ENUM('SCHEDULED','COMPLETED','CANCELLED'),
    type ENUM('ASSESSMENT','THERAPY','CONSULTATION'),
    notes TEXT,
    FOREIGN KEY (case_id) REFERENCES cases(case_id),
    FOREIGN KEY (staff_id) REFERENCES staff(staff_id)
);

-- Documents table
CREATE TABLE IF NOT EXISTS documents (
    document_id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    case_id INT UNSIGNED,
    type ENUM('MEDICAL','ASSESSMENT','PROGRESS_REPORT'),
    file_name VARCHAR(255),
    content_type VARCHAR(100),
    file_data MEDIUMBLOB,
    file_size BIGINT,
    upload_date DATE,
    uploaded_by INT UNSIGNED,
    FOREIGN KEY (case_id) REFERENCES cases(case_id),
    FOREIGN KEY (uploaded_by) REFERENCES users(user_id)
);

-- Expenses table
CREATE TABLE IF NOT EXISTS expenses (
    expense_id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    category ENUM('SALARY','RENT','SUPPLIES','MARKETING','MAINTENANCE','UTILITIES','OTHER'),
    amount DECIMAL(10,2) NOT NULL,
    date DATE NOT NULL,
    description VARCHAR(255),
    receipt_path VARCHAR(255),
    paid_by INT UNSIGNED,
    payment_method ENUM('CASH','CARD','BANK'),
    FOREIGN KEY (paid_by) REFERENCES staff(staff_id)
);

-- Fidelity Points table
CREATE TABLE IF NOT EXISTS fidelity_points (
    point_id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    case_id INT UNSIGNED,
    points INT NOT NULL,
    transaction_date DATETIME NOT NULL,
    type ENUM('EARN','REDEEM'),
    source VARCHAR(50),
    expiry_date DATE,
    status ENUM('ACTIVE','USED','EXPIRED'),
    FOREIGN KEY (case_id) REFERENCES cases(case_id)
);

-- Rewards table
CREATE TABLE IF NOT EXISTS rewards (
    reward_id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    points_required INT NOT NULL,
    description TEXT,
    valid_until DATE,
    status ENUM('ACTIVE','INACTIVE')
);

-- Add indexes for better performance
CREATE INDEX idx_cases_name ON cases(name);
CREATE INDEX idx_cases_status ON cases(status);
CREATE INDEX idx_sessions_date ON sessions(session_date);
CREATE INDEX idx_payments_date ON payments(payment_date);
CREATE INDEX idx_appointments_datetime ON appointments(date_time);
CREATE INDEX idx_fidelity_points_status ON fidelity_points(status);