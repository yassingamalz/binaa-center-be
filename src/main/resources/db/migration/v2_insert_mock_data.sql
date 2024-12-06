-- 1. Users Setup
INSERT INTO Users (username, password, role) VALUES 
('yasmien.gamal', '$2a$10$xLJLvX5lCOFYoUjB6mJKWe2YXz5DZp5QD4yqhEGZV3NLF0mxGpiIi', 'admin'),
('fatima.hassan', '$2a$10$xLJLvX5lCOFYoUjB6mJKWe2YXz5DZp5QD4yqhEGZV3NLF0mxGpiIi', 'staff'),
('omar.ahmed', '$2a$10$xLJLvX5lCOFYoUjB6mJKWe2YXz5DZp5QD4yqhEGZV3NLF0mxGpiIi', 'staff'),
('nour.ibrahim', '$2a$10$xLJLvX5lCOFYoUjB6mJKWe2YXz5DZp5QD4yqhEGZV3NLF0mxGpiIi', 'staff');

-- 2. Staff Setup
INSERT INTO Staff (name, role, contact_number) VALUES 
('ياسمين جمال', 'أخصائي نطق ولغة', '+20 100 123 4567'),
('فاطمة حسن', 'أخصائي علاج طبيعي', '+20 100 234 5678'),
('عمر أحمد', 'أخصائي نفسي', '+20 100 345 6789'),
('نور إبراهيم', 'معالج وظيفي', '+20 100 456 7890');

-- 3. Cases Setup
INSERT INTO Cases (
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
 'تأخر في النطق', '2024-01-15', 'active', 
 '+20 101 234 5679', 'لا يوجد تاريخ مرضي', 'لا يوجد', 'لا يوجد',
 'مدرسة النيل الدولية', 'الصف الثاني', 'اضطراب النطق واللغة', 'لا يوجد',
 'شركة مصر للتأمين - بوليصة رقم 12345'),

('سارة علي', 5, 'علي حسن', '+20 102 345 6789',
 'تأخر في النمو اللغوي', '2024-02-01', 'active',
 '+20 102 345 6780', 'ولادة مبكرة', 'فيتامينات', 'حساسية من البيض',
 'مدرسة المستقبل', 'الروضة', 'اضطراب اللغة التعبيري', 'تأخر نمائي بسيط',
 'شركة الدلتا للتأمين - بوليصة رقم 23456'),

('مريم خالد', 9, 'خالد إبراهيم', '+20 103 456 7890',
 'صعوبات تعلم', '2024-01-20', 'active',
 '+20 103 456 7891', 'لا يوجد', 'لا يوجد', 'حساسية من البنسلين',
 'مدرسة الرواد', 'الصف الرابع', 'عسر القراءة', 'صعوبات في الانتباه',
 'التأمين الصحي الحكومي');

-- 4. Sessions Setup
INSERT INTO Sessions (
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
 'تم إجراء تقييم شامل للنطق', 1, 'individual', 'present', 60,
 'تحديد نقاط القوة والضعف في النطق', 'البدء في تمارين نطق حرف الراء'),

(1, 'جلسة علاج نطق', '2024-02-01 11:00:00',
 'التركيز على تمارين النطق', 1, 'individual', 'present', 45,
 'تحسن في نطق حرف السين', 'متابعة تمارين حرف الراء'),

(2, 'تقييم تطور لغوي', '2024-02-05 09:00:00',
 'تقييم المهارات اللغوية الحالية', 1, 'individual', 'present', 60,
 'تحديد مستوى المفردات والجمل', 'وضع خطة لتطوير المفردات');

-- 5. Treatment Plans Setup
INSERT INTO Treatment_Plans (
    case_id,
    goals,
    start_date,
    end_date,
    status,
    progress_notes,
    specialist_id
) VALUES 
(1, 'تحسين نطق الحروف وخاصة الراء والسين', 
    '2024-01-25', '2024-07-25', 'active',
    'تحسن ملحوظ في نطق حرف السين', 1),

(2, 'تطوير المفردات وبناء الجمل وتحسين مهارات المحادثة',
    '2024-02-10', '2024-08-10', 'active',
    'تحسن في استخدام المفردات الأساسية', 1);

-- 6. Appointments Setup
INSERT INTO Appointments (
    case_id,
    staff_id,
    date_time,
    status,
    type,
    notes
) VALUES 
(1, 1, '2024-03-15 10:00:00', 'scheduled', 'therapy',
    'جلسة علاج نطق دورية'),
(2, 1, '2024-03-15 11:00:00', 'scheduled', 'therapy',
    'جلسة متابعة تطور لغوي');

-- 7. Payments Setup
INSERT INTO Payments (
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
    'رسوم جلسة تقييم', 'cash', 'INV-2024-001', 'paid'),
(1, 2, 250.00, '2024-02-01 12:00:00',
    'رسوم جلسة علاج', 'card', 'INV-2024-002', 'paid'),
(2, 3, 300.00, '2024-02-05 10:00:00',
    'رسوم جلسة تقييم', 'bank', 'INV-2024-003', 'paid');

-- 8. Documents Setup
INSERT INTO Documents (
    case_id,
    type,
    file_path,
    upload_date,
    uploaded_by
) VALUES 
(1, 'assessment', '/documents/cases/1/assessment_2024_01.pdf', '2024-01-20', 1),
(1, 'progress_report', '/documents/cases/1/progress_2024_02.pdf', '2024-02-01', 1),
(2, 'assessment', '/documents/cases/2/assessment_2024_02.pdf', '2024-02-05', 1);

-- 9. Expenses Setup
INSERT INTO Expenses (
    category,
    amount,
    date,
    description,
    receipt_path,
    paid_by,
    payment_method
) VALUES 
('supplies', 500.00, '2024-01-15', 'أدوات تعليمية', '/receipts/2024/01/sup_001.pdf', 1, 'cash'),
('utilities', 1000.00, '2024-02-01', 'فاتورة الكهرباء', '/receipts/2024/02/util_001.pdf', 1, 'bank'),
('marketing', 750.00, '2024-02-15', 'إعلانات على فيسبوك', '/receipts/2024/02/mkt_001.pdf', 1, 'card');

-- 10. Fidelity Points Setup
INSERT INTO Fidelity_Points (
    case_id,
    points,
    transaction_date,
    type,
    source,
    expiry_date,
    status
) VALUES 
(1, 100, '2024-01-20 11:00:00', 'earn', 'تقييم أولي', '2024-12-31', 'active'),
(1, 50, '2024-02-01 12:00:00', 'earn', 'جلسة علاج', '2024-12-31', 'active'),
(2, 100, '2024-02-05 10:00:00', 'earn', 'تقييم أولي', '2024-12-31', 'active');

-- 11. Rewards Setup
INSERT INTO Rewards (
    name,
    points_required,
    description,
    valid_until,
    status
) VALUES 
('جلسة مجانية', 500, 'جلسة علاج مجانية', '2024-12-31', 'active'),
('تقييم مجاني', 300, 'جلسة تقييم مجانية', '2024-12-31', 'active'),
('خصم 25%', 200, 'خصم على الجلسة القادمة', '2024-12-31', 'active');