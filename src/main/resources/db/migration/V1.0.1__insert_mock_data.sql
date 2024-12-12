-- 1. Users Setup
INSERT INTO users (username, password, role) VALUES
('yasmin.gamal', '$2a$10$TCVMqihKDgc0Glit3hOzJOUzMJXKAb3OA8Xvbrej6iIm6MgdNfzla', 'ADMIN'),
('ali.hassan', '$2a$10$HRfX7z4DrW.uhH7BENQCZOlMHL1FhcayXNQ3Yd7L7JQSHYPBP4TOO', 'STAFF'),
('farah.omar', '$2a$10$3tCZ3BBrP5tQmFw2rDbKkOd8HbUHpA2Mfchcm3TlkAfm3ddqcJZ9W', 'STAFF'),
('ahmed.nasser', '$2a$10$Vkj9ECMx5KzOgh3PvUldruE/MpMaHrnXHFqCZPhvKKE1jXZL8J40q', 'ADMIN'),
('nada.elsayed', '$2a$10$WBX0KCVIF9XmIEu.5pQTPeDSkVp3VSoTpGRVb4fASU4GHkTukfwO6', 'STAFF');

-- 2. Staff Setup
INSERT INTO staff (name, role, contact_number) VALUES
('ياسمين جمال', 'أخصائي نطق ولغة', '+20 100 123 4567'),
('فاطمة حسن', 'أخصائي علاج طبيعي', '+20 100 234 5678'),
('عمر أحمد', 'أخصائي نفسي', '+20 100 345 6789'),
('نور إبراهيم', 'معالج وظيفي', '+20 100 456 7890');

-- 3. Cases Setup
INSERT INTO cases (
    name,
    age,
    guardian_name,
    contact_number,
    special_needs,
    admission_date,
    status,
    emergency_contact,
    medical_history,
    current_medications,
    allergies,
    school_name,
    grade_level,
    primary_diagnosis,
    secondary_diagnosis,
    insurance_info
) VALUES
('أحمد محمد', 7, 'محمد أحمد', '+20 101 234 5678',
 'تأخر في النطق', '2024-01-15', 'ACTIVE',
 '+20 101 234 5679', 'لا يوجد تاريخ مرضي', 'لا يوجد', 'لا يوجد',
 'مدرسة النيل الدولية', 'الصف الثاني', 'اضطراب النطق واللغة', 'لا يوجد',
 'شركة مصر للتأمين - بوليصة رقم 12345'),

('سارة علي', 5, 'علي حسن', '+20 102 345 6789',
 'تأخر في النمو اللغوي', '2024-02-01', 'ACTIVE',
 '+20 102 345 6780', 'ولادة مبكرة', 'فيتامينات', 'حساسية من البيض',
 'مدرسة المستقبل', 'الروضة', 'اضطراب اللغة التعبيري', 'تأخر نمائي بسيط',
 'شركة الدلتا للتأمين - بوليصة رقم 23456'),

('مريم خالد', 9, 'خالد إبراهيم', '+20 103 456 7890',
 'صعوبات تعلم', '2024-01-20', 'ACTIVE',
 '+20 103 456 7891', 'لا يوجد', 'لا يوجد', 'حساسية من البنسلين',
 'مدرسة الرواد', 'الصف الرابع', 'عسر القراءة', 'صعوبات في الانتباه',
 'التأمين الصحي الحكومي');

-- 4. Sessions Setup
INSERT INTO sessions (
    case_id,
    purpose,
    session_date,
    notes,
    staff_id,
    session_type,
    attendance_status,
    duration,
    goals_achieved,
    next_session_plan
) VALUES
(1, 'تقييم نطق أولي', '2024-01-20 10:00:00',
 'تم إجراء تقييم شامل للنطق', 1, 'INDIVIDUAL', 'PRESENT', 60,
 'تحديد نقاط القوة والضعف في النطق', 'البدء في تمارين نطق حرف الراء'),

(1, 'جلسة علاج نطق', '2024-02-01 11:00:00',
 'التركيز على تمارين النطق', 1, 'INDIVIDUAL', 'PRESENT', 45,
 'تحسن في نطق حرف السين', 'متابعة تمارين حرف الراء'),

(2, 'تقييم تطور لغوي', '2024-02-05 09:00:00',
 'تقييم المهارات اللغوية الحالية', 1, 'INDIVIDUAL', 'PRESENT', 60,
 'تحديد مستوى المفردات والجمل', 'وضع خطة لتطوير المفردات');

-- 5. Treatment Plans Setup
INSERT INTO treatment_plans (
    case_id,
    goals,
    start_date,
    end_date,
    status,
    progress_notes,
    specialist_id
) VALUES
(1, 'تحسين نطق الحروف وخاصة الراء والسين',
    '2024-01-25', '2024-07-25', 'ACTIVE',
    'تحسن ملحوظ في نطق حرف السين', 1),

(2, 'تطوير المفردات وبناء الجمل وتحسين مهارات المحادثة',
    '2024-02-10', '2024-08-10', 'ACTIVE',
    'تحسن في استخدام المفردات الأساسية', 1);

-- 6. Appointments Setup
INSERT INTO appointments (
    case_id,
    staff_id,
    date_time,
    status,
    type,
    notes
) VALUES
(1, 1, '2024-03-15 10:00:00', 'SCHEDULED', 'THERAPY',
    'جلسة علاج نطق دورية'),
(2, 1, '2024-03-15 11:00:00', 'SCHEDULED', 'THERAPY',
    'جلسة متابعة تطور لغوي');

-- 7. Payments Setup
INSERT INTO payments (
    case_id,
    session_id,
    amount,
    payment_date,
    description,
    payment_method,
    invoice_number,
    payment_status
) VALUES
(1, 1, 300.00, '2024-01-20 11:00:00',
    'رسوم جلسة تقييم', 'CASH', 'INV-2024-001', 'PAID'),
(1, 2, 250.00, '2024-02-01 12:00:00',
    'رسوم جلسة علاج', 'CARD', 'INV-2024-002', 'PAID'),
(2, 3, 300.00, '2024-02-05 10:00:00',
    'رسوم جلسة تقييم', 'BANK', 'INV-2024-003', 'PAID');

-- 8. Documents Setup
INSERT INTO documents (
    case_id,
    type,
    file_path,
    upload_date,
    uploaded_by
) VALUES
(1, 'ASSESSMENT', '/documents/cases/1/ASSESSMENT_2024_01.pdf', '2024-01-20', 1),
(1, 'PROGRESS_REPORT', '/documents/cases/1/progress_2024_02.pdf', '2024-02-01', 1),
(2, 'ASSESSMENT', '/documents/cases/2/ASSESSMENT_2024_02.pdf', '2024-02-05', 1);

-- 9. Expenses Setup
INSERT INTO expenses (
    category,
    amount,
    date,
    description,
    receipt_path,
    PAID_by,
    payment_method
) VALUES
('SUPPLIES', 500.00, '2024-01-15', 'أدوات تعليمية', '/receipts/2024/01/sup_001.pdf', 1, 'CASH'),
('UTILITIES', 1000.00, '2024-02-01', 'فاتورة الكهرباء', '/receipts/2024/02/util_001.pdf', 1, 'BANK'),
('MARKETING', 750.00, '2024-02-15', 'إعلانات على فيسبوك', '/receipts/2024/02/mkt_001.pdf', 1, 'CARD');

-- 10. Fidelity Points Setup
INSERT INTO fidelity_points (
    case_id,
    points,
    transaction_date,
    type,
    source,
    expiry_date,
    status
) VALUES
(1, 100, '2024-01-20 11:00:00', 'EARN', 'تقييم أولي', '2024-12-31', 'ACTIVE'),
(1, 50, '2024-02-01 12:00:00', 'EARN', 'جلسة علاج', '2024-12-31', 'ACTIVE'),
(2, 100, '2024-02-05 10:00:00', 'EARN', 'تقييم أولي', '2024-12-31', 'ACTIVE');

-- 11. Rewards Setup
INSERT INTO rewards (
    name,
    points_required,
    description,
    valid_until,
    status
) VALUES 
('جلسة مجانية', 500, 'جلسة علاج مجانية', '2024-12-31', 'ACTIVE'),
('تقييم مجاني', 300, 'جلسة تقييم مجانية', '2024-12-31', 'ACTIVE'),
('خصم 25%', 200, 'خصم على الجلسة القادمة', '2024-12-31', 'ACTIVE');

-- 11. Assessments Setup
INSERT INTO assessments (case_id, assessment_type, score, assessment_date, next_assessment_date, assessor_id, recommendations, status)
VALUES
(3, 'IQ', 95.50, '2024-02-15', '2024-08-15', 3, 'الاستمرار في خطة التدخل الحالية', 'COMPLETED'),
(2, 'PSYCHOLOGICAL', 82.00, '2024-03-01', '2024-09-01', 3, 'البدء في جلسات العلاج باللعب', 'COMPLETED'),
(1, 'LEARNING_DIFFICULTIES', 88.75, '2024-03-10', '2024-09-10', 4, 'تطبيق استراتيجيات التدخل في القراءة', 'COMPLETED');