-- =============================================================================
-- HỆ THỐNG QUẢN LÝ HỌC TẬP THPT (High School LMS)
-- Database Schema - MySQL 8.0+
-- Version: 1.0.0
-- =============================================================================

SET NAMES utf8mb4;
SET CHARACTER SET utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;
SET SQL_MODE = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION';

DROP DATABASE IF EXISTS thpt_lms;
CREATE DATABASE thpt_lms
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE thpt_lms;

-- =============================================================================
-- NHÓM 1: NGƯỜI DÙNG & PHÂN QUYỀN
-- =============================================================================

-- -----------------------------------------------------------------------------
-- 1.1 users - Tài khoản người dùng (trung tâm xác thực)
-- -----------------------------------------------------------------------------
CREATE TABLE users (
    id                  BIGINT UNSIGNED     NOT NULL AUTO_INCREMENT,
    username            VARCHAR(50)         NOT NULL,
    password_hash       VARCHAR(255)        NOT NULL COMMENT 'BCrypt hash',
    email               VARCHAR(100)        NULL,
    full_name           VARCHAR(100)        NOT NULL,
    phone               VARCHAR(15)         NULL,
    avatar_url          VARCHAR(500)        NULL,
    is_active           TINYINT(1)          NOT NULL DEFAULT 1 COMMENT '1=hoat dong, 0=khoa',
    last_login_at       DATETIME            NULL,
    password_changed_at DATETIME            NULL,
    created_at          DATETIME            NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at          DATETIME            NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    PRIMARY KEY (id),
    UNIQUE KEY uq_users_username (username),
    UNIQUE KEY uq_users_email (email),
    INDEX idx_users_is_active (is_active),
    INDEX idx_users_full_name (full_name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci
  COMMENT='Tai khoan nguoi dung: Admin, Giao vien, Hoc sinh';

-- -----------------------------------------------------------------------------
-- 1.2 roles - Vai trò hệ thống
-- -----------------------------------------------------------------------------
CREATE TABLE roles (
    id          TINYINT UNSIGNED    NOT NULL AUTO_INCREMENT,
    name        ENUM(
                    'ADMIN',
                    'TEACHER',
                    'HOMEROOM_TEACHER',
                    'STUDENT'
                )                   NOT NULL,
    description VARCHAR(255)        NULL,

    PRIMARY KEY (id),
    UNIQUE KEY uq_roles_name (name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci
  COMMENT='Vai tro: Admin, Giao vien bo mon, GVCN, Hoc sinh';

-- Dữ liệu mặc định
INSERT INTO roles (name, description) VALUES
    ('ADMIN',             'Quan tri vien he thong'),
    ('TEACHER',           'Giao vien bo mon'),
    ('HOMEROOM_TEACHER',  'Giao vien chu nhiem'),
    ('STUDENT',           'Hoc sinh');

-- -----------------------------------------------------------------------------
-- 1.3 user_roles - Gán vai trò (nhiều-nhiều)
-- -----------------------------------------------------------------------------
CREATE TABLE user_roles (
    user_id     BIGINT UNSIGNED     NOT NULL,
    role_id     TINYINT UNSIGNED    NOT NULL,
    assigned_at DATETIME            NOT NULL DEFAULT CURRENT_TIMESTAMP,
    assigned_by BIGINT UNSIGNED     NULL COMMENT 'Admin thuc hien phan quyen',

    PRIMARY KEY (user_id, role_id),
    INDEX idx_user_roles_role_id (role_id),

    CONSTRAINT fk_user_roles_user
        FOREIGN KEY (user_id) REFERENCES users (id)
        ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_user_roles_role
        FOREIGN KEY (role_id) REFERENCES roles (id)
        ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT fk_user_roles_assigned_by
        FOREIGN KEY (assigned_by) REFERENCES users (id)
        ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci
  COMMENT='Quan he nhieu-nhieu: User - Role';

-- -----------------------------------------------------------------------------
-- 1.4 teachers - Hồ sơ giáo viên
-- -----------------------------------------------------------------------------
CREATE TABLE teachers (
    id              BIGINT UNSIGNED     NOT NULL AUTO_INCREMENT,
    user_id         BIGINT UNSIGNED     NOT NULL,
    teacher_code    VARCHAR(20)         NOT NULL COMMENT 'Ma GV: GV001',
    department      VARCHAR(100)        NULL COMMENT 'To bo mon',
    specialization  VARCHAR(200)        NULL COMMENT 'Chuyen mon chinh',
    degree          ENUM(
                        'BACHELOR',
                        'MASTER',
                        'DOCTOR',
                        'PROFESSOR'
                    )                   NULL,
    joining_date    DATE                NULL COMMENT 'Ngay vao truong',
    is_homeroom     TINYINT(1)          NOT NULL DEFAULT 0 COMMENT '1=la GVCN',
    notes           TEXT                NULL,
    created_at      DATETIME            NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at      DATETIME            NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    PRIMARY KEY (id),
    UNIQUE KEY uq_teachers_user_id (user_id),
    UNIQUE KEY uq_teachers_code (teacher_code),
    INDEX idx_teachers_is_homeroom (is_homeroom),

    CONSTRAINT fk_teachers_user
        FOREIGN KEY (user_id) REFERENCES users (id)
        ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci
  COMMENT='Ho so giao vien mo rong tu users';

-- -----------------------------------------------------------------------------
-- 1.5 students - Hồ sơ học sinh
-- (class_id se them FK sau khi tao bang classes)
-- -----------------------------------------------------------------------------
CREATE TABLE students (
    id              BIGINT UNSIGNED     NOT NULL AUTO_INCREMENT,
    user_id         BIGINT UNSIGNED     NOT NULL,
    student_code    VARCHAR(20)         NOT NULL COMMENT 'Ma HS: HS2024001',
    class_id        BIGINT UNSIGNED     NULL COMMENT 'Lop hoc hien tai',
    date_of_birth   DATE                NOT NULL,
    gender          ENUM('MALE','FEMALE','OTHER') NOT NULL,
    address         TEXT                NULL,
    enrollment_year YEAR                NOT NULL COMMENT 'Nam nhap hoc',
    is_active       TINYINT(1)          NOT NULL DEFAULT 1 COMMENT '1=dang hoc',
    notes           TEXT                NULL COMMENT 'Ghi chu cua GVCN',
    created_at      DATETIME            NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at      DATETIME            NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    PRIMARY KEY (id),
    UNIQUE KEY uq_students_user_id (user_id),
    UNIQUE KEY uq_students_code (student_code),
    INDEX idx_students_class_id (class_id),
    INDEX idx_students_is_active (is_active),
    INDEX idx_students_enrollment_year (enrollment_year),

    CONSTRAINT fk_students_user
        FOREIGN KEY (user_id) REFERENCES users (id)
        ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci
  COMMENT='Ho so hoc sinh mo rong tu users';

-- -----------------------------------------------------------------------------
-- 1.6 parent_info - Thông tin phụ huynh
-- -----------------------------------------------------------------------------
CREATE TABLE parent_info (
    id              BIGINT UNSIGNED     NOT NULL AUTO_INCREMENT,
    student_id      BIGINT UNSIGNED     NOT NULL,
    relationship    ENUM('FATHER','MOTHER','GUARDIAN') NOT NULL,
    full_name       VARCHAR(100)        NOT NULL,
    phone           VARCHAR(15)         NOT NULL,
    email           VARCHAR(100)        NULL,
    occupation      VARCHAR(100)        NULL COMMENT 'Nghe nghiep',
    is_primary      TINYINT(1)          NOT NULL DEFAULT 1 COMMENT '1=phu huynh chinh de lien he',
    created_at      DATETIME            NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at      DATETIME            NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    PRIMARY KEY (id),
    INDEX idx_parent_info_student_id (student_id),

    CONSTRAINT fk_parent_info_student
        FOREIGN KEY (student_id) REFERENCES students (id)
        ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci
  COMMENT='Thong tin phu huynh hoc sinh (chi GVCN truy cap)';

-- =============================================================================
-- NHÓM 2: CẤU TRÚC TRƯỜNG HỌC
-- =============================================================================

-- -----------------------------------------------------------------------------
-- 2.1 academic_years - Năm học
-- -----------------------------------------------------------------------------
CREATE TABLE academic_years (
    id          INT UNSIGNED    NOT NULL AUTO_INCREMENT,
    name        VARCHAR(20)     NOT NULL COMMENT 'VD: 2024-2025',
    start_date  DATE            NOT NULL,
    end_date    DATE            NOT NULL,
    is_active   TINYINT(1)      NOT NULL DEFAULT 0 COMMENT 'Chi 1 nam hoc active tai moi thoi diem',
    created_by  BIGINT UNSIGNED NOT NULL,
    created_at  DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at  DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    PRIMARY KEY (id),
    UNIQUE KEY uq_academic_years_name (name),
    INDEX idx_academic_years_is_active (is_active),

    CONSTRAINT fk_academic_years_created_by
        FOREIGN KEY (created_by) REFERENCES users (id)
        ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci
  COMMENT='Nam hoc: 2024-2025, 2025-2026...';

-- -----------------------------------------------------------------------------
-- 2.2 semesters - Học kỳ
-- -----------------------------------------------------------------------------
CREATE TABLE semesters (
    id                  INT UNSIGNED    NOT NULL AUTO_INCREMENT,
    academic_year_id    INT UNSIGNED    NOT NULL,
    semester_number     TINYINT         NOT NULL COMMENT '1=HK1, 2=HK2',
    name                VARCHAR(30)     NOT NULL COMMENT 'VD: Hoc ky 1 (2024-2025)',
    start_date          DATE            NOT NULL,
    end_date            DATE            NOT NULL,
    is_locked           TINYINT(1)      NOT NULL DEFAULT 0 COMMENT '1=khoa, khong cho nhap/sua diem',
    created_at          DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at          DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    PRIMARY KEY (id),
    UNIQUE KEY uq_semesters_year_number (academic_year_id, semester_number),
    INDEX idx_semesters_is_locked (is_locked),

    CONSTRAINT fk_semesters_academic_year
        FOREIGN KEY (academic_year_id) REFERENCES academic_years (id)
        ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci
  COMMENT='Hoc ky 1, Hoc ky 2 theo tung nam hoc';

-- -----------------------------------------------------------------------------
-- 2.3 grades - Khối học
-- -----------------------------------------------------------------------------
CREATE TABLE grades (
    id              TINYINT UNSIGNED    NOT NULL AUTO_INCREMENT,
    grade_number    TINYINT             NOT NULL COMMENT '10, 11 hoac 12',
    name            VARCHAR(20)         NOT NULL COMMENT 'VD: Khoi 10',

    PRIMARY KEY (id),
    UNIQUE KEY uq_grades_number (grade_number)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci
  COMMENT='Khoi hoc: 10, 11, 12';

INSERT INTO grades (grade_number, name) VALUES (10,'Khoi 10'),(11,'Khoi 11'),(12,'Khoi 12');

-- -----------------------------------------------------------------------------
-- 2.4 rooms - Phòng học
-- -----------------------------------------------------------------------------
CREATE TABLE rooms (
    id              INT UNSIGNED    NOT NULL AUTO_INCREMENT,
    room_code       VARCHAR(20)     NOT NULL COMMENT 'Ma phong: A101, B203',
    name            VARCHAR(100)    NOT NULL COMMENT 'VD: Phong hoc A101',
    capacity        TINYINT         NOT NULL DEFAULT 45 COMMENT 'Suc chua toi da',
    room_type       ENUM(
                        'CLASSROOM',
                        'LAB',
                        'GYM',
                        'HALL',
                        'OTHER'
                    )               NOT NULL DEFAULT 'CLASSROOM',
    floor           TINYINT         NULL COMMENT 'Tang so',
    building        VARCHAR(50)     NULL COMMENT 'Toa nha',
    has_projector   TINYINT(1)      NOT NULL DEFAULT 0,
    is_active       TINYINT(1)      NOT NULL DEFAULT 1,
    created_at      DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP,

    PRIMARY KEY (id),
    UNIQUE KEY uq_rooms_code (room_code),
    INDEX idx_rooms_type (room_type),
    INDEX idx_rooms_is_active (is_active)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci
  COMMENT='Phong hoc, phong thi nghiem, phong the duc...';

-- -----------------------------------------------------------------------------
-- 2.5 classes - Lớp học
-- -----------------------------------------------------------------------------
CREATE TABLE classes (
    id                      BIGINT UNSIGNED     NOT NULL AUTO_INCREMENT,
    class_code              VARCHAR(20)         NOT NULL COMMENT '10A1, 11B2, 12C3',
    name                    VARCHAR(50)         NOT NULL,
    grade_id                TINYINT UNSIGNED    NOT NULL,
    academic_year_id        INT UNSIGNED        NOT NULL,
    homeroom_teacher_id     BIGINT UNSIGNED     NULL COMMENT 'GVCN cua lop',
    max_students            TINYINT             NOT NULL DEFAULT 45,
    room_id                 INT UNSIGNED        NULL COMMENT 'Phong hoc chinh',
    is_active               TINYINT(1)          NOT NULL DEFAULT 1,
    created_at              DATETIME            NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at              DATETIME            NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    PRIMARY KEY (id),
    UNIQUE KEY uq_classes_code_year (class_code, academic_year_id),
    INDEX idx_classes_grade_id (grade_id),
    INDEX idx_classes_academic_year (academic_year_id),
    INDEX idx_classes_homeroom_teacher (homeroom_teacher_id),
    INDEX idx_classes_is_active (is_active),

    CONSTRAINT fk_classes_grade
        FOREIGN KEY (grade_id) REFERENCES grades (id)
        ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT fk_classes_academic_year
        FOREIGN KEY (academic_year_id) REFERENCES academic_years (id)
        ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT fk_classes_homeroom_teacher
        FOREIGN KEY (homeroom_teacher_id) REFERENCES teachers (id)
        ON DELETE SET NULL ON UPDATE CASCADE,
    CONSTRAINT fk_classes_room
        FOREIGN KEY (room_id) REFERENCES rooms (id)
        ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci
  COMMENT='Lop hoc: 10A1, 11B2, 12C3...';

-- Thêm FK cho students.class_id (sau khi tạo classes)
ALTER TABLE students
    ADD CONSTRAINT fk_students_class
        FOREIGN KEY (class_id) REFERENCES classes (id)
        ON DELETE SET NULL ON UPDATE CASCADE;

-- -----------------------------------------------------------------------------
-- 2.6 subjects - Môn học
-- -----------------------------------------------------------------------------
CREATE TABLE subjects (
    id              INT UNSIGNED        NOT NULL AUTO_INCREMENT,
    subject_code    VARCHAR(20)         NOT NULL COMMENT 'TOAN, LY, HOA, VAN...',
    name            VARCHAR(100)        NOT NULL COMMENT 'Ten mon day du',
    coefficient     DECIMAL(3,1)        NOT NULL DEFAULT 1.0 COMMENT 'He so mon (1.0, 2.0...)',
    periods_per_week TINYINT            NOT NULL DEFAULT 3 COMMENT 'So tiet/tuan',
    grade_id        TINYINT UNSIGNED    NULL COMMENT 'Mon thuoc khoi (NULL=tat ca khoi)',
    is_active       TINYINT(1)          NOT NULL DEFAULT 1,
    description     TEXT                NULL,
    created_at      DATETIME            NOT NULL DEFAULT CURRENT_TIMESTAMP,

    PRIMARY KEY (id),
    UNIQUE KEY uq_subjects_code (subject_code),
    INDEX idx_subjects_grade_id (grade_id),
    INDEX idx_subjects_is_active (is_active),

    CONSTRAINT fk_subjects_grade
        FOREIGN KEY (grade_id) REFERENCES grades (id)
        ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci
  COMMENT='Mon hoc: Toan, Ly, Hoa, Van, Anh...';

-- Dữ liệu môn học mẫu
INSERT INTO subjects (subject_code, name, coefficient, periods_per_week) VALUES
    ('TOAN',   'Toan hoc',                2.0, 4),
    ('VAN',    'Ngu van',                 2.0, 4),
    ('ANH',    'Tieng Anh',               1.0, 3),
    ('LY',     'Vat ly',                  1.0, 3),
    ('HOA',    'Hoa hoc',                 1.0, 3),
    ('SINH',   'Sinh hoc',                1.0, 2),
    ('SU',     'Lich su',                 1.0, 2),
    ('DIA',    'Dia ly',                  1.0, 2),
    ('GDCD',   'Giao duc cong dan',       1.0, 1),
    ('TIN',    'Tin hoc',                 1.0, 2),
    ('THE',    'Giao duc the chat',       1.0, 2),
    ('QPAN',   'Giao duc quoc phong',     1.0, 1);

-- -----------------------------------------------------------------------------
-- 2.7 score_configs - Cấu hình hệ số điểm theo môn và học kỳ
-- -----------------------------------------------------------------------------
CREATE TABLE score_configs (
    id                      INT UNSIGNED    NOT NULL AUTO_INCREMENT,
    subject_id              INT UNSIGNED    NOT NULL,
    semester_id             INT UNSIGNED    NOT NULL,
    weight_attendance       DECIMAL(3,2)    NOT NULL DEFAULT 0.10 COMMENT 'He so chuyen can',
    weight_oral             DECIMAL(3,2)    NOT NULL DEFAULT 0.10 COMMENT 'He so diem mien',
    weight_15min            DECIMAL(3,2)    NOT NULL DEFAULT 0.10 COMMENT 'He so 15 phut',
    weight_1period          DECIMAL(3,2)    NOT NULL DEFAULT 0.25 COMMENT 'He so 1 tiet',
    weight_midterm          DECIMAL(3,2)    NOT NULL DEFAULT 0.20 COMMENT 'He so giua ky',
    weight_final            DECIMAL(3,2)    NOT NULL DEFAULT 0.25 COMMENT 'He so cuoi ky',
    absence_deduct          DECIMAL(3,2)    NOT NULL DEFAULT 0.50 COMMENT 'Diem tru/buoi vang khong phep',
    base_attendance_score   DECIMAL(4,2)    NOT NULL DEFAULT 10.00 COMMENT 'Diem chuyen can ban dau',
    max_oral_columns        TINYINT         NOT NULL DEFAULT 0  COMMENT '0=khong gioi han',
    max_15min_columns       TINYINT         NOT NULL DEFAULT 0  COMMENT '0=khong gioi han',
    max_1period_columns     TINYINT         NOT NULL DEFAULT 3  COMMENT 'Toi da 3 cot 1 tiet',
    created_at              DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at              DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    PRIMARY KEY (id),
    UNIQUE KEY uq_score_configs (subject_id, semester_id),

    CONSTRAINT fk_score_configs_subject
        FOREIGN KEY (subject_id) REFERENCES subjects (id)
        ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT fk_score_configs_semester
        FOREIGN KEY (semester_id) REFERENCES semesters (id)
        ON DELETE RESTRICT ON UPDATE CASCADE,

    CONSTRAINT chk_score_config_weights
        CHECK (
            ABS(weight_attendance + weight_oral + weight_15min
              + weight_1period + weight_midterm + weight_final - 1.00) < 0.01
        )
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci
  COMMENT='Cau hinh cong thuc tinh diem TB theo mon va hoc ky';

-- =============================================================================
-- NHÓM 3: PHÂN CÔNG & THỜI KHÓA BIỂU
-- =============================================================================

-- -----------------------------------------------------------------------------
-- 3.1 teaching_assignments - Phân công giảng dạy
-- -----------------------------------------------------------------------------
CREATE TABLE teaching_assignments (
    id          BIGINT UNSIGNED     NOT NULL AUTO_INCREMENT,
    teacher_id  BIGINT UNSIGNED     NOT NULL,
    subject_id  INT UNSIGNED        NOT NULL,
    class_id    BIGINT UNSIGNED     NOT NULL,
    semester_id INT UNSIGNED        NOT NULL,
    assigned_by BIGINT UNSIGNED     NOT NULL COMMENT 'Admin thuc hien',
    assigned_at DATETIME            NOT NULL DEFAULT CURRENT_TIMESTAMP,
    is_active   TINYINT(1)          NOT NULL DEFAULT 1 COMMENT '1=con hieu luc',

    PRIMARY KEY (id),
    UNIQUE KEY uq_teaching_assignment (teacher_id, subject_id, class_id, semester_id),
    INDEX idx_ta_teacher_id (teacher_id),
    INDEX idx_ta_subject_id (subject_id),
    INDEX idx_ta_class_id (class_id),
    INDEX idx_ta_semester_id (semester_id),
    INDEX idx_ta_is_active (is_active),

    CONSTRAINT fk_ta_teacher
        FOREIGN KEY (teacher_id) REFERENCES teachers (id)
        ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT fk_ta_subject
        FOREIGN KEY (subject_id) REFERENCES subjects (id)
        ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT fk_ta_class
        FOREIGN KEY (class_id) REFERENCES classes (id)
        ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT fk_ta_semester
        FOREIGN KEY (semester_id) REFERENCES semesters (id)
        ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT fk_ta_assigned_by
        FOREIGN KEY (assigned_by) REFERENCES users (id)
        ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci
  COMMENT='Phan cong: Giao vien day mon nao o lop nao trong hoc ky nao';

-- -----------------------------------------------------------------------------
-- 3.2 time_slots - Khung giờ học
-- -----------------------------------------------------------------------------
CREATE TABLE time_slots (
    id          TINYINT UNSIGNED    NOT NULL AUTO_INCREMENT,
    slot_number TINYINT             NOT NULL COMMENT 'Tiet 1, 2, 3...10',
    start_time  TIME                NOT NULL COMMENT 'VD: 07:00:00',
    end_time    TIME                NOT NULL COMMENT 'VD: 07:45:00',
    shift       ENUM('MORNING','AFTERNOON') NOT NULL DEFAULT 'MORNING',
    description VARCHAR(100)        NULL COMMENT 'Tiet 1 (7:00 - 7:45)',

    PRIMARY KEY (id),
    UNIQUE KEY uq_time_slots_number (slot_number)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci
  COMMENT='Khung gio hoc: Tiet 1 den Tiet 10';

INSERT INTO time_slots (slot_number, start_time, end_time, shift, description) VALUES
    (1,  '07:00', '07:45', 'MORNING',   'Tiet 1  (07:00 - 07:45)'),
    (2,  '07:50', '08:35', 'MORNING',   'Tiet 2  (07:50 - 08:35)'),
    (3,  '08:40', '09:25', 'MORNING',   'Tiet 3  (08:40 - 09:25)'),
    (4,  '09:35', '10:20', 'MORNING',   'Tiet 4  (09:35 - 10:20)'),
    (5,  '10:25', '11:10', 'MORNING',   'Tiet 5  (10:25 - 11:10)'),
    (6,  '13:00', '13:45', 'AFTERNOON', 'Tiet 6  (13:00 - 13:45)'),
    (7,  '13:50', '14:35', 'AFTERNOON', 'Tiet 7  (13:50 - 14:35)'),
    (8,  '14:40', '15:25', 'AFTERNOON', 'Tiet 8  (14:40 - 15:25)'),
    (9,  '15:35', '16:20', 'AFTERNOON', 'Tiet 9  (15:35 - 16:20)'),
    (10, '16:25', '17:10', 'AFTERNOON', 'Tiet 10 (16:25 - 17:10)');

-- -----------------------------------------------------------------------------
-- 3.3 timetables - Thời khóa biểu
-- -----------------------------------------------------------------------------
CREATE TABLE timetables (
    id                      BIGINT UNSIGNED     NOT NULL AUTO_INCREMENT,
    semester_id             INT UNSIGNED        NOT NULL,
    class_id                BIGINT UNSIGNED     NOT NULL,
    subject_id              INT UNSIGNED        NOT NULL,
    teacher_id              BIGINT UNSIGNED     NOT NULL,
    room_id                 INT UNSIGNED        NULL,
    time_slot_id            TINYINT UNSIGNED    NOT NULL,
    day_of_week             TINYINT             NOT NULL COMMENT '2=T2, 3=T3, 4=T4, 5=T5, 6=T6, 7=T7, 8=CN',
    effective_from          DATE                NULL COMMENT 'NULL=ap dung ca hoc ky',
    effective_to            DATE                NULL COMMENT 'NULL=ap dung ca hoc ky',
    week_type               ENUM('ALL','ODD','EVEN') NOT NULL DEFAULT 'ALL' COMMENT 'Tuan thuong/le/chan',
    is_active               TINYINT(1)          NOT NULL DEFAULT 1,
    created_by              BIGINT UNSIGNED     NOT NULL,
    created_at              DATETIME            NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at              DATETIME            NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    PRIMARY KEY (id),
    -- Tranh trung lich lop
    UNIQUE KEY uq_timetable_class  (semester_id, class_id,   time_slot_id, day_of_week, week_type, effective_from),
    -- Tranh trung lich giao vien
    UNIQUE KEY uq_timetable_teacher(semester_id, teacher_id, time_slot_id, day_of_week, week_type, effective_from),
    -- Tranh trung phong hoc
    UNIQUE KEY uq_timetable_room   (semester_id, room_id,    time_slot_id, day_of_week, week_type, effective_from),

    INDEX idx_timetable_semester (semester_id),
    INDEX idx_timetable_class (class_id),
    INDEX idx_timetable_teacher (teacher_id),
    INDEX idx_timetable_dow (day_of_week),

    CONSTRAINT fk_timetable_semester
        FOREIGN KEY (semester_id) REFERENCES semesters (id)
        ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT fk_timetable_class
        FOREIGN KEY (class_id) REFERENCES classes (id)
        ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT fk_timetable_subject
        FOREIGN KEY (subject_id) REFERENCES subjects (id)
        ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT fk_timetable_teacher
        FOREIGN KEY (teacher_id) REFERENCES teachers (id)
        ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT fk_timetable_room
        FOREIGN KEY (room_id) REFERENCES rooms (id)
        ON DELETE SET NULL ON UPDATE CASCADE,
    CONSTRAINT fk_timetable_time_slot
        FOREIGN KEY (time_slot_id) REFERENCES time_slots (id)
        ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT fk_timetable_created_by
        FOREIGN KEY (created_by) REFERENCES users (id)
        ON DELETE RESTRICT ON UPDATE CASCADE,

    CONSTRAINT chk_timetable_dow
        CHECK (day_of_week BETWEEN 2 AND 8)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci
  COMMENT='TKB: Lop nao, Mon gi, GV nao, Phong nao, Thu may, Tiet may';

-- =============================================================================
-- NHÓM 4: ĐIỂM DANH & THÁI ĐỘ
-- =============================================================================

-- -----------------------------------------------------------------------------
-- 4.1 attendances - Điểm danh từng buổi học
-- -----------------------------------------------------------------------------
CREATE TABLE attendances (
    id                      BIGINT UNSIGNED     NOT NULL AUTO_INCREMENT,
    student_id              BIGINT UNSIGNED     NOT NULL,
    teaching_assignment_id  BIGINT UNSIGNED     NOT NULL,
    session_date            DATE                NOT NULL COMMENT 'Ngay buoi hoc',
    time_slot_id            TINYINT UNSIGNED    NOT NULL COMMENT 'Tiet hoc cu the',
    status                  ENUM(
                                'PRESENT',
                                'ABSENT_EXCUSED',
                                'ABSENT_UNEXCUSED',
                                'LATE'
                            )                   NOT NULL DEFAULT 'PRESENT',
    reason                  VARCHAR(500)        NULL COMMENT 'Ly do vang/tre',
    recorded_by             BIGINT UNSIGNED     NOT NULL COMMENT 'Giao vien diem danh',
    recorded_at             DATETIME            NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at              DATETIME            NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    PRIMARY KEY (id),
    UNIQUE KEY uq_attendance (student_id, teaching_assignment_id, session_date, time_slot_id),
    INDEX idx_attendance_ta_date (teaching_assignment_id, session_date),
    INDEX idx_attendance_student (student_id),
    INDEX idx_attendance_date (session_date),
    INDEX idx_attendance_status (status),

    CONSTRAINT fk_attendance_student
        FOREIGN KEY (student_id) REFERENCES students (id)
        ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT fk_attendance_ta
        FOREIGN KEY (teaching_assignment_id) REFERENCES teaching_assignments (id)
        ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT fk_attendance_time_slot
        FOREIGN KEY (time_slot_id) REFERENCES time_slots (id)
        ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT fk_attendance_recorded_by
        FOREIGN KEY (recorded_by) REFERENCES users (id)
        ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci
  COMMENT='Diem danh tung hoc sinh moi buoi hoc';

-- -----------------------------------------------------------------------------
-- 4.2 attitude_types - Loại hành vi tích điểm thái độ
-- -----------------------------------------------------------------------------
CREATE TABLE attitude_types (
    id              INT UNSIGNED        NOT NULL AUTO_INCREMENT,
    category        ENUM('POSITIVE','NEGATIVE') NOT NULL COMMENT 'POSITIVE=cong diem, NEGATIVE=tru diem',
    name            VARCHAR(100)        NOT NULL COMMENT 'VD: Tham gia tich cuc',
    default_points  DECIMAL(3,1)        NOT NULL DEFAULT 0.5 COMMENT 'Diem mac dinh (+0.5, -1...)',
    description     VARCHAR(255)        NULL,
    is_active       TINYINT(1)          NOT NULL DEFAULT 1,

    PRIMARY KEY (id),
    INDEX idx_attitude_types_category (category),
    INDEX idx_attitude_types_is_active (is_active)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci
  COMMENT='Danh sach loai hanh vi thai do de cham diem';

INSERT INTO attitude_types (category, name, default_points, description) VALUES
    ('POSITIVE', 'Tham gia tich cuc',         0.5, 'Hoc sinh chu dong phat bieu, tham gia bai hoc'),
    ('POSITIVE', 'Tra loi dung cau hoi',       1.0, 'Tra loi chinh xac cau hoi kho'),
    ('POSITIVE', 'Hoan thanh bai tap tot',     0.5, 'Bai tap day du, chinh xac va sang tao'),
    ('POSITIVE', 'Giup do ban be hoc tap',     0.5, 'Ho tro ban trong nhom hoc'),
    ('POSITIVE', 'Co gang tien bo',            1.0, 'The hien su co gang tien bo ro ret'),
    ('NEGATIVE', 'Noi chuyen rieng',          -0.5, 'Noi chuyen rieng trong gio hoc'),
    ('NEGATIVE', 'Khong lam bai tap',         -1.0, 'Khong hoan thanh bai tap ve nha'),
    ('NEGATIVE', 'Khong chu y nghe giang',    -0.5, 'Lam viec rieng, khong tap trung'),
    ('NEGATIVE', 'Vi pham noi quy lop hoc',   -1.0, 'Vi pham cac quy dinh cua lop/truong'),
    ('NEGATIVE', 'Su dung dien thoai',        -0.5, 'Dung dien thoai trong gio hoc');

-- -----------------------------------------------------------------------------
-- 4.3 attitude_records - Ghi nhận tích điểm thái độ
-- -----------------------------------------------------------------------------
CREATE TABLE attitude_records (
    id                      BIGINT UNSIGNED     NOT NULL AUTO_INCREMENT,
    student_id              BIGINT UNSIGNED     NOT NULL,
    teaching_assignment_id  BIGINT UNSIGNED     NOT NULL,
    attitude_type_id        INT UNSIGNED        NOT NULL,
    session_date            DATE                NOT NULL,
    points                  DECIMAL(3,1)        NOT NULL COMMENT 'Diem thuc te (co the khac default)',
    note                    VARCHAR(500)        NULL COMMENT 'Ghi chu them',
    recorded_by             BIGINT UNSIGNED     NOT NULL,
    recorded_at             DATETIME            NOT NULL DEFAULT CURRENT_TIMESTAMP,

    PRIMARY KEY (id),
    INDEX idx_attitude_records_student (student_id),
    INDEX idx_attitude_records_ta (teaching_assignment_id),
    INDEX idx_attitude_records_date (session_date),

    CONSTRAINT fk_attitude_records_student
        FOREIGN KEY (student_id) REFERENCES students (id)
        ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT fk_attitude_records_ta
        FOREIGN KEY (teaching_assignment_id) REFERENCES teaching_assignments (id)
        ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT fk_attitude_records_type
        FOREIGN KEY (attitude_type_id) REFERENCES attitude_types (id)
        ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT fk_attitude_records_recorded_by
        FOREIGN KEY (recorded_by) REFERENCES users (id)
        ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci
  COMMENT='Lich su tich diem thai do moi buoi hoc';

-- =============================================================================
-- NHÓM 5: ĐIỂM SỐ
-- =============================================================================

-- -----------------------------------------------------------------------------
-- 5.1 score_columns - Định nghĩa cột điểm
-- -----------------------------------------------------------------------------
CREATE TABLE score_columns (
    id                      BIGINT UNSIGNED     NOT NULL AUTO_INCREMENT,
    teaching_assignment_id  BIGINT UNSIGNED     NOT NULL,
    score_type              ENUM(
                                'ATTENDANCE',   -- Chuyen can (tu dong)
                                'ORAL',         -- Kiem tra mien
                                'MIN15',        -- Kiem tra 15 phut
                                'PERIOD1',      -- Kiem tra 1 tiet
                                'MIDTERM',      -- Giua ky
                                'FINAL'         -- Cuoi ky
                            )                   NOT NULL,
    column_number           TINYINT             NOT NULL DEFAULT 1 COMMENT 'Thu tu cot cung loai',
    name                    VARCHAR(100)        NULL COMMENT 'Ten cot tuy chinh: KT chuong 3',
    exam_date               DATE                NULL,
    max_score               DECIMAL(4,1)        NOT NULL DEFAULT 10.0,
    is_locked               TINYINT(1)          NOT NULL DEFAULT 0 COMMENT '1=giao vien khong sua duoc',
    created_at              DATETIME            NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at              DATETIME            NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    PRIMARY KEY (id),
    UNIQUE KEY uq_score_column (teaching_assignment_id, score_type, column_number),
    INDEX idx_score_columns_ta (teaching_assignment_id),
    INDEX idx_score_columns_type (score_type),

    CONSTRAINT fk_score_columns_ta
        FOREIGN KEY (teaching_assignment_id) REFERENCES teaching_assignments (id)
        ON DELETE RESTRICT ON UPDATE CASCADE,

    CONSTRAINT chk_score_col_max
        CHECK (max_score > 0 AND max_score <= 10)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci
  COMMENT='Dinh nghia cac cot diem: M1, M2, 15p1, 15p2, 1T1, 1T2, 1T3, GK, CK, CC';

-- -----------------------------------------------------------------------------
-- 5.2 scores - Điểm từng học sinh theo từng cột
-- -----------------------------------------------------------------------------
CREATE TABLE scores (
    id              BIGINT UNSIGNED     NOT NULL AUTO_INCREMENT,
    score_column_id BIGINT UNSIGNED     NOT NULL,
    student_id      BIGINT UNSIGNED     NOT NULL,
    score_value     DECIMAL(4,1)        NULL COMMENT 'NULL=chua nhap diem',
    is_exempt       TINYINT(1)          NOT NULL DEFAULT 0 COMMENT '1=mien kiem tra, khong tinh',
    note            VARCHAR(255)        NULL,
    entered_by      BIGINT UNSIGNED     NOT NULL,
    entered_at      DATETIME            NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at      DATETIME            NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    updated_by      BIGINT UNSIGNED     NULL,

    PRIMARY KEY (id),
    UNIQUE KEY uq_scores (score_column_id, student_id),
    INDEX idx_scores_student_id (student_id),
    INDEX idx_scores_column_id (score_column_id),

    CONSTRAINT fk_scores_column
        FOREIGN KEY (score_column_id) REFERENCES score_columns (id)
        ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT fk_scores_student
        FOREIGN KEY (student_id) REFERENCES students (id)
        ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT fk_scores_entered_by
        FOREIGN KEY (entered_by) REFERENCES users (id)
        ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT fk_scores_updated_by
        FOREIGN KEY (updated_by) REFERENCES users (id)
        ON DELETE SET NULL ON UPDATE CASCADE,

    CONSTRAINT chk_score_value
        CHECK (score_value IS NULL OR (score_value >= 0 AND score_value <= 10))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci
  COMMENT='Diem so cua tung hoc sinh theo tung cot diem';

-- -----------------------------------------------------------------------------
-- 5.3 grade_results - Kết quả tổng hợp môn học (bảng cache)
-- -----------------------------------------------------------------------------
CREATE TABLE grade_results (
    id                      BIGINT UNSIGNED     NOT NULL AUTO_INCREMENT,
    student_id              BIGINT UNSIGNED     NOT NULL,
    teaching_assignment_id  BIGINT UNSIGNED     NOT NULL,
    attendance_score        DECIMAL(4,2)        NULL COMMENT 'Diem chuyen can (tu diem danh + thai do)',
    oral_avg                DECIMAL(4,2)        NULL COMMENT 'TB diem mien',
    min15_avg               DECIMAL(4,2)        NULL COMMENT 'TB diem 15 phut',
    period1_avg             DECIMAL(4,2)        NULL COMMENT 'TB diem 1 tiet',
    midterm_score           DECIMAL(4,2)        NULL COMMENT 'Diem giua ky',
    final_score             DECIMAL(4,2)        NULL COMMENT 'Diem cuoi ky',
    average_score           DECIMAL(4,2)        NULL COMMENT 'DTB mon (lam tron 0.5)',
    classification          ENUM(
                                'EXCELLENT',    -- Gioi  >= 8.0
                                'GOOD',         -- Kha   6.5 - 7.9
                                'AVERAGE',      -- TB    5.0 - 6.4
                                'WEAK',         -- Yeu   3.5 - 4.9
                                'POOR'          -- Kem   < 3.5
                            )                   NULL,
    class_rank              INT                 NULL COMMENT 'Xep hang trong lop (mon nay)',
    recalculated_at         DATETIME            NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    PRIMARY KEY (id),
    UNIQUE KEY uq_grade_results (student_id, teaching_assignment_id),
    INDEX idx_grade_results_student (student_id),
    INDEX idx_grade_results_ta (teaching_assignment_id),
    INDEX idx_grade_results_avg (average_score),
    INDEX idx_grade_results_class (classification),

    CONSTRAINT fk_grade_results_student
        FOREIGN KEY (student_id) REFERENCES students (id)
        ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT fk_grade_results_ta
        FOREIGN KEY (teaching_assignment_id) REFERENCES teaching_assignments (id)
        ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci
  COMMENT='Cache ket qua tong hop tung mon - duoc tinh lai khi co thay doi diem';

-- =============================================================================
-- NHÓM 6: BÀI TẬP & NỘP BÀI
-- =============================================================================

-- -----------------------------------------------------------------------------
-- 6.1 assignments - Bài tập giáo viên giao
-- -----------------------------------------------------------------------------
CREATE TABLE assignments (
    id                      BIGINT UNSIGNED     NOT NULL AUTO_INCREMENT,
    teaching_assignment_id  BIGINT UNSIGNED     NOT NULL COMMENT 'GV + Lop + Mon',
    title                   VARCHAR(255)        NOT NULL,
    description             TEXT                NULL COMMENT 'Noi dung huong dan, de bai',
    assignment_type         ENUM(
                                'HOMEWORK',
                                'PROJECT',
                                'EXERCISE',
                                'EXAM'
                            )                   NOT NULL DEFAULT 'HOMEWORK',
    due_date                DATETIME            NOT NULL COMMENT 'Han nop bai',
    max_score               DECIMAL(4,1)        NULL DEFAULT 10.0 COMMENT 'Diem toi da (NULL=khong cham diem)',
    allow_late              TINYINT(1)          NOT NULL DEFAULT 0 COMMENT 'Cho phep nop muon',
    late_penalty_per_day    DECIMAL(3,1)        NOT NULL DEFAULT 0 COMMENT 'Tru diem moi ngay muon',
    allow_file_upload       TINYINT(1)          NOT NULL DEFAULT 1,
    allow_text_submission   TINYINT(1)          NOT NULL DEFAULT 1,
    max_file_size_mb        INT                 NOT NULL DEFAULT 50,
    allowed_file_types      VARCHAR(200)        NULL COMMENT 'pdf,docx,jpg,png (NULL=tat ca)',
    is_published            TINYINT(1)          NOT NULL DEFAULT 0 COMMENT '1=hoc sinh thay duoc',
    published_at            DATETIME            NULL COMMENT 'Thoi diem cong bo',
    created_at              DATETIME            NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at              DATETIME            NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    PRIMARY KEY (id),
    INDEX idx_assignments_ta (teaching_assignment_id),
    INDEX idx_assignments_due_date (due_date),
    INDEX idx_assignments_is_published (is_published),
    INDEX idx_assignments_type (assignment_type),

    CONSTRAINT fk_assignments_ta
        FOREIGN KEY (teaching_assignment_id) REFERENCES teaching_assignments (id)
        ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci
  COMMENT='Bai tap/du an giao vien giao cho lop';

-- -----------------------------------------------------------------------------
-- 6.2 assignment_submissions - Bài nộp của học sinh
-- -----------------------------------------------------------------------------
CREATE TABLE assignment_submissions (
    id              BIGINT UNSIGNED     NOT NULL AUTO_INCREMENT,
    assignment_id   BIGINT UNSIGNED     NOT NULL,
    student_id      BIGINT UNSIGNED     NOT NULL,
    text_content    LONGTEXT            NULL COMMENT 'Nop bang van ban (optional)',
    submitted_at    DATETIME            NOT NULL DEFAULT CURRENT_TIMESTAMP,
    is_late         TINYINT(1)          NOT NULL DEFAULT 0 COMMENT '1=nop muon',
    score           DECIMAL(4,1)        NULL COMMENT 'Diem cham cua giao vien',
    feedback        TEXT                NULL COMMENT 'Nhan xet cua giao vien',
    graded_by       BIGINT UNSIGNED     NULL,
    graded_at       DATETIME            NULL,
    status          ENUM(
                        'DRAFT',        -- Luu nhap, chua nop
                        'SUBMITTED',    -- Da nop
                        'GRADED',       -- Da cham diem
                        'RETURNED'      -- Da tra bai
                    )                   NOT NULL DEFAULT 'SUBMITTED',
    submission_count TINYINT            NOT NULL DEFAULT 1 COMMENT 'So lan nop',

    PRIMARY KEY (id),
    UNIQUE KEY uq_submissions (assignment_id, student_id),
    INDEX idx_submissions_assignment (assignment_id),
    INDEX idx_submissions_student (student_id),
    INDEX idx_submissions_status (status),
    INDEX idx_submissions_submitted_at (submitted_at),

    CONSTRAINT fk_submissions_assignment
        FOREIGN KEY (assignment_id) REFERENCES assignments (id)
        ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT fk_submissions_student
        FOREIGN KEY (student_id) REFERENCES students (id)
        ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT fk_submissions_graded_by
        FOREIGN KEY (graded_by) REFERENCES users (id)
        ON DELETE SET NULL ON UPDATE CASCADE,

    CONSTRAINT chk_submission_score
        CHECK (score IS NULL OR (score >= 0 AND score <= 10))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci
  COMMENT='Bai nop cua hoc sinh (text hoac file)';

-- -----------------------------------------------------------------------------
-- 6.3 submission_files - File đính kèm bài nộp
-- -----------------------------------------------------------------------------
CREATE TABLE submission_files (
    id              BIGINT UNSIGNED     NOT NULL AUTO_INCREMENT,
    submission_id   BIGINT UNSIGNED     NOT NULL,
    original_name   VARCHAR(255)        NOT NULL COMMENT 'Ten file goc khi upload',
    stored_name     VARCHAR(255)        NOT NULL COMMENT 'Ten file luu server (UUID + ext)',
    file_path       VARCHAR(500)        NOT NULL COMMENT 'Duong dan day du tren server',
    file_size       INT UNSIGNED        NOT NULL COMMENT 'Dung luong byte',
    mime_type       VARCHAR(100)        NOT NULL COMMENT 'VD: application/pdf, image/png',
    uploaded_at     DATETIME            NOT NULL DEFAULT CURRENT_TIMESTAMP,

    PRIMARY KEY (id),
    UNIQUE KEY uq_submission_files_stored_name (stored_name),
    INDEX idx_submission_files_submission (submission_id),

    CONSTRAINT fk_submission_files_submission
        FOREIGN KEY (submission_id) REFERENCES assignment_submissions (id)
        ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci
  COMMENT='File dinh kem cua tung bai nop';

-- =============================================================================
-- NHÓM 7: XẾP LOẠI, HỌC BẠ & NHẬT KÝ
-- =============================================================================

-- -----------------------------------------------------------------------------
-- 7.1 student_rankings - Xếp hạng tổng hợp học kỳ
-- -----------------------------------------------------------------------------
CREATE TABLE student_rankings (
    id                      BIGINT UNSIGNED     NOT NULL AUTO_INCREMENT,
    student_id              BIGINT UNSIGNED     NOT NULL,
    semester_id             INT UNSIGNED        NOT NULL,
    gpa                     DECIMAL(4,2)        NULL COMMENT 'DTB chung (co trong so he so mon)',
    rank_in_class           INT                 NULL COMMENT 'Xep hang trong lop (1=cao nhat)',
    total_students          INT                 NULL COMMENT 'Tong so HS trong lop o hoc ky nay',
    academic_classification ENUM(
                                'EXCELLENT',    -- Gioi  >= 8.0, khong co mon < 6.5
                                'GOOD',         -- Kha   6.5 - 7.9, khong co mon < 5.0
                                'AVERAGE',      -- TB    5.0 - 6.4, khong co mon < 3.5
                                'WEAK',         -- Yeu   3.5 - 4.9
                                'POOR'          -- Kem   < 3.5 hoac co mon diem liet
                            )                   NULL,
    calculated_at           DATETIME            NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    PRIMARY KEY (id),
    UNIQUE KEY uq_student_rankings (student_id, semester_id),
    INDEX idx_student_rankings_semester (semester_id),
    INDEX idx_student_rankings_gpa (gpa),
    INDEX idx_student_rankings_classification (academic_classification),

    CONSTRAINT fk_student_rankings_student
        FOREIGN KEY (student_id) REFERENCES students (id)
        ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT fk_student_rankings_semester
        FOREIGN KEY (semester_id) REFERENCES semesters (id)
        ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci
  COMMENT='Xep hang tong hop HS theo hoc ky (GPA, xep hang lop, hoc luc)';

-- -----------------------------------------------------------------------------
-- 7.2 conduct_ratings - Xếp loại hạnh kiểm
-- -----------------------------------------------------------------------------
CREATE TABLE conduct_ratings (
    id          BIGINT UNSIGNED     NOT NULL AUTO_INCREMENT,
    student_id  BIGINT UNSIGNED     NOT NULL,
    semester_id INT UNSIGNED        NOT NULL,
    rating      ENUM(
                    'EXCELLENT',    -- Tot
                    'GOOD',         -- Kha
                    'AVERAGE',      -- Trung binh
                    'WEAK'          -- Yeu
                )                   NOT NULL,
    comment     TEXT                NULL COMMENT 'Nhan xet cua GVCN',
    rated_by    BIGINT UNSIGNED     NOT NULL COMMENT 'GVCN danh gia',
    rated_at    DATETIME            NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at  DATETIME            NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    PRIMARY KEY (id),
    UNIQUE KEY uq_conduct_ratings (student_id, semester_id),
    INDEX idx_conduct_ratings_semester (semester_id),
    INDEX idx_conduct_ratings_rating (rating),

    CONSTRAINT fk_conduct_ratings_student
        FOREIGN KEY (student_id) REFERENCES students (id)
        ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT fk_conduct_ratings_semester
        FOREIGN KEY (semester_id) REFERENCES semesters (id)
        ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT fk_conduct_ratings_rated_by
        FOREIGN KEY (rated_by) REFERENCES teachers (id)
        ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci
  COMMENT='Hanh kiem hoc sinh theo hoc ky (GVCN danh gia)';

-- -----------------------------------------------------------------------------
-- 7.3 academic_titles - Danh hiệu thi đua
-- -----------------------------------------------------------------------------
CREATE TABLE academic_titles (
    id          BIGINT UNSIGNED     NOT NULL AUTO_INCREMENT,
    student_id  BIGINT UNSIGNED     NOT NULL,
    semester_id INT UNSIGNED        NOT NULL,
    title       ENUM(
                    'EXCELLENCE',   -- Hoc sinh Gioi
                    'MERIT',        -- Hoc sinh Tien tien
                    'AWARD'         -- Khen thuong dac biet
                )                   NOT NULL,
    issued_by   BIGINT UNSIGNED     NOT NULL COMMENT 'Admin/GVCN cap danh hieu',
    issued_at   DATE                NOT NULL,
    note        VARCHAR(255)        NULL,

    PRIMARY KEY (id),
    INDEX idx_academic_titles_student (student_id),
    INDEX idx_academic_titles_semester (semester_id),
    INDEX idx_academic_titles_title (title),

    CONSTRAINT fk_academic_titles_student
        FOREIGN KEY (student_id) REFERENCES students (id)
        ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT fk_academic_titles_semester
        FOREIGN KEY (semester_id) REFERENCES semesters (id)
        ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT fk_academic_titles_issued_by
        FOREIGN KEY (issued_by) REFERENCES users (id)
        ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci
  COMMENT='Danh hieu thi dua: HS Gioi, HS Tien tien, Khen thuong';

-- -----------------------------------------------------------------------------
-- 7.4 audit_logs - Nhật ký hoạt động hệ thống
-- -----------------------------------------------------------------------------
CREATE TABLE audit_logs (
    id          BIGINT UNSIGNED     NOT NULL AUTO_INCREMENT,
    user_id     BIGINT UNSIGNED     NULL COMMENT 'NULL neu he thong tu dong thuc hien',
    action      VARCHAR(100)        NOT NULL COMMENT 'VD: LOGIN, UPDATE_SCORE, DELETE_USER',
    table_name  VARCHAR(100)        NULL COMMENT 'Bang bi anh huong',
    record_id   BIGINT              NULL COMMENT 'ID ban ghi bi anh huong',
    old_value   JSON                NULL COMMENT 'Gia tri truoc thay doi',
    new_value   JSON                NULL COMMENT 'Gia tri sau thay doi',
    ip_address  VARCHAR(45)         NULL COMMENT 'IPv4 hoac IPv6',
    user_agent  VARCHAR(500)        NULL COMMENT 'Browser/thiet bi',
    created_at  DATETIME            NOT NULL DEFAULT CURRENT_TIMESTAMP,

    PRIMARY KEY (id),
    INDEX idx_audit_logs_user_id (user_id),
    INDEX idx_audit_logs_action (action),
    INDEX idx_audit_logs_table_record (table_name, record_id),
    INDEX idx_audit_logs_created_at (created_at),

    CONSTRAINT fk_audit_logs_user
        FOREIGN KEY (user_id) REFERENCES users (id)
        ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci
  COMMENT='Nhat ky hoat dong: dang nhap, sua diem, xoa du lieu...'
  -- Partition theo thang de hieu nang tot hon voi du lieu lon:
  -- PARTITION BY RANGE (YEAR(created_at) * 100 + MONTH(created_at)) (...)
;

-- =============================================================================
-- VIEWS HỮU ÍCH
-- =============================================================================

-- View: Thời khóa biểu đầy đủ thông tin
CREATE OR REPLACE VIEW v_timetable_full AS
SELECT
    tt.id,
    ay.name                 AS academic_year,
    s.name                  AS semester_name,
    c.class_code,
    c.name                  AS class_name,
    g.grade_number,
    sub.name                AS subject_name,
    u.full_name             AS teacher_name,
    r.room_code,
    r.name                  AS room_name,
    ts.slot_number,
    ts.start_time,
    ts.end_time,
    ts.shift,
    tt.day_of_week,
    CASE tt.day_of_week
        WHEN 2 THEN 'Thu Hai'  WHEN 3 THEN 'Thu Ba'
        WHEN 4 THEN 'Thu Tu'   WHEN 5 THEN 'Thu Nam'
        WHEN 6 THEN 'Thu Sau'  WHEN 7 THEN 'Thu Bay'
        WHEN 8 THEN 'Chu Nhat' END AS day_name,
    tt.week_type,
    tt.effective_from,
    tt.effective_to,
    tt.is_active
FROM timetables tt
JOIN semesters       s   ON tt.semester_id   = s.id
JOIN academic_years  ay  ON s.academic_year_id = ay.id
JOIN classes         c   ON tt.class_id      = c.id
JOIN grades          g   ON c.grade_id       = g.id
JOIN subjects        sub ON tt.subject_id    = sub.id
JOIN teachers        t   ON tt.teacher_id    = t.id
JOIN users           u   ON t.user_id        = u.id
LEFT JOIN rooms      r   ON tt.room_id       = r.id
JOIN time_slots      ts  ON tt.time_slot_id  = ts.id
WHERE tt.is_active = 1;

-- View: Điểm tổng hợp tất cả môn của học sinh (cho GVCN)
CREATE OR REPLACE VIEW v_student_grade_summary AS
SELECT
    st.id                   AS student_id,
    st.student_code,
    u.full_name             AS student_name,
    c.class_code,
    sub.subject_code,
    sub.name                AS subject_name,
    sub.coefficient,
    gr.attendance_score,
    gr.oral_avg,
    gr.min15_avg,
    gr.period1_avg,
    gr.midterm_score,
    gr.final_score,
    gr.average_score,
    gr.classification,
    gr.class_rank,
    sem.id                  AS semester_id,
    sem.name                AS semester_name
FROM grade_results gr
JOIN teaching_assignments ta ON gr.teaching_assignment_id = ta.id
JOIN students          st  ON gr.student_id      = st.id
JOIN users             u   ON st.user_id         = u.id
JOIN classes           c   ON st.class_id        = c.id
JOIN subjects          sub ON ta.subject_id      = sub.id
JOIN semesters         sem ON ta.semester_id     = sem.id;

-- View: Thống kê chuyên cần theo học sinh và môn học
CREATE OR REPLACE VIEW v_attendance_summary AS
SELECT
    a.student_id,
    a.teaching_assignment_id,
    COUNT(*) AS total_sessions,
    SUM(CASE WHEN a.status = 'PRESENT'           THEN 1 ELSE 0 END) AS present_count,
    SUM(CASE WHEN a.status = 'ABSENT_EXCUSED'    THEN 1 ELSE 0 END) AS absent_excused_count,
    SUM(CASE WHEN a.status = 'ABSENT_UNEXCUSED'  THEN 1 ELSE 0 END) AS absent_unexcused_count,
    SUM(CASE WHEN a.status = 'LATE'              THEN 1 ELSE 0 END) AS late_count,
    ROUND(
        SUM(CASE WHEN a.status = 'PRESENT' THEN 1 ELSE 0 END) * 100.0 / COUNT(*),
        2
    ) AS attendance_rate_pct
FROM attendances a
GROUP BY a.student_id, a.teaching_assignment_id;

-- View: Thống kê điểm thái độ theo học sinh và môn học
CREATE OR REPLACE VIEW v_attitude_summary AS
SELECT
    ar.student_id,
    ar.teaching_assignment_id,
    SUM(CASE WHEN at.category = 'POSITIVE' THEN ar.points ELSE 0 END) AS total_positive,
    SUM(CASE WHEN at.category = 'NEGATIVE' THEN ar.points ELSE 0 END) AS total_negative,
    SUM(ar.points) AS net_attitude_points
FROM attitude_records ar
JOIN attitude_types at ON ar.attitude_type_id = at.id
GROUP BY ar.student_id, ar.teaching_assignment_id;

-- View: Xếp hạng lớp tổng hợp
CREATE OR REPLACE VIEW v_class_ranking AS
SELECT
    sr.student_id,
    u.full_name             AS student_name,
    c.class_code,
    sem.name                AS semester_name,
    sr.gpa,
    sr.rank_in_class,
    sr.total_students,
    sr.academic_classification,
    cr.rating               AS conduct_rating
FROM student_rankings sr
JOIN students    st  ON sr.student_id  = st.id
JOIN users       u   ON st.user_id     = u.id
JOIN classes     c   ON st.class_id    = c.id
JOIN semesters   sem ON sr.semester_id = sem.id
LEFT JOIN conduct_ratings cr
    ON cr.student_id  = sr.student_id
    AND cr.semester_id = sr.semester_id;

-- =============================================================================
-- STORED PROCEDURES
-- =============================================================================

DELIMITER $$

-- Tính lại điểm trung bình môn cho 1 học sinh + 1 teaching_assignment
CREATE PROCEDURE sp_recalculate_grade_result(
    IN p_student_id             BIGINT UNSIGNED,
    IN p_teaching_assignment_id BIGINT UNSIGNED
)
BEGIN
    DECLARE v_att_score     DECIMAL(4,2) DEFAULT NULL;
    DECLARE v_oral_avg      DECIMAL(4,2) DEFAULT NULL;
    DECLARE v_min15_avg     DECIMAL(4,2) DEFAULT NULL;
    DECLARE v_period1_avg   DECIMAL(4,2) DEFAULT NULL;
    DECLARE v_midterm       DECIMAL(4,2) DEFAULT NULL;
    DECLARE v_final         DECIMAL(4,2) DEFAULT NULL;
    DECLARE v_average       DECIMAL(4,2) DEFAULT NULL;
    DECLARE v_class         VARCHAR(20)  DEFAULT NULL;

    DECLARE v_w_att    DECIMAL(3,2);
    DECLARE v_w_oral   DECIMAL(3,2);
    DECLARE v_w_15m    DECIMAL(3,2);
    DECLARE v_w_1p     DECIMAL(3,2);
    DECLARE v_w_mid    DECIMAL(3,2);
    DECLARE v_w_fin    DECIMAL(3,2);
    DECLARE v_base_att DECIMAL(4,2);
    DECLARE v_deduct   DECIMAL(3,2);
    DECLARE v_absent_u INT;
    DECLARE v_semester_id INT UNSIGNED;
    DECLARE v_subject_id  INT UNSIGNED;

    -- Lay thong tin phan cong
    SELECT semester_id, subject_id
    INTO v_semester_id, v_subject_id
    FROM teaching_assignments
    WHERE id = p_teaching_assignment_id;

    -- Lay cau hinh he so diem
    SELECT weight_attendance, weight_oral, weight_15min,
           weight_1period, weight_midterm, weight_final,
           base_attendance_score, absence_deduct
    INTO v_w_att, v_w_oral, v_w_15m, v_w_1p, v_w_mid, v_w_fin,
         v_base_att, v_deduct
    FROM score_configs
    WHERE subject_id = v_subject_id AND semester_id = v_semester_id
    LIMIT 1;

    -- Neu khong co config, dung gia tri mac dinh
    IF v_w_att IS NULL THEN
        SET v_w_att=0.10; SET v_w_oral=0.10; SET v_w_15m=0.10;
        SET v_w_1p=0.25;  SET v_w_mid=0.20;  SET v_w_fin=0.25;
        SET v_base_att=10.00; SET v_deduct=0.50;
    END IF;

    -- Dem so buoi vang khong phep
    SELECT COUNT(*) INTO v_absent_u
    FROM attendances
    WHERE student_id = p_student_id
      AND teaching_assignment_id = p_teaching_assignment_id
      AND status = 'ABSENT_UNEXCUSED';

    -- Diem chuyen can = base - (vang khong phep * muc tru) + thai do tich luy
    SELECT GREATEST(0, LEAST(10,
        v_base_att
        - (v_absent_u * v_deduct)
        + COALESCE((
            SELECT SUM(ar.points)
            FROM attitude_records ar
            WHERE ar.student_id = p_student_id
              AND ar.teaching_assignment_id = p_teaching_assignment_id
          ), 0)
    )) INTO v_att_score;

    -- TB diem mien
    SELECT AVG(s.score_value) INTO v_oral_avg
    FROM scores s
    JOIN score_columns sc ON s.score_column_id = sc.id
    WHERE s.student_id = p_student_id
      AND sc.teaching_assignment_id = p_teaching_assignment_id
      AND sc.score_type = 'ORAL'
      AND s.is_exempt = 0
      AND s.score_value IS NOT NULL;

    -- TB diem 15 phut
    SELECT AVG(s.score_value) INTO v_min15_avg
    FROM scores s
    JOIN score_columns sc ON s.score_column_id = sc.id
    WHERE s.student_id = p_student_id
      AND sc.teaching_assignment_id = p_teaching_assignment_id
      AND sc.score_type = 'MIN15'
      AND s.is_exempt = 0
      AND s.score_value IS NOT NULL;

    -- TB diem 1 tiet
    SELECT AVG(s.score_value) INTO v_period1_avg
    FROM scores s
    JOIN score_columns sc ON s.score_column_id = sc.id
    WHERE s.student_id = p_student_id
      AND sc.teaching_assignment_id = p_teaching_assignment_id
      AND sc.score_type = 'PERIOD1'
      AND s.is_exempt = 0
      AND s.score_value IS NOT NULL;

    -- Diem giua ky
    SELECT s.score_value INTO v_midterm
    FROM scores s
    JOIN score_columns sc ON s.score_column_id = sc.id
    WHERE s.student_id = p_student_id
      AND sc.teaching_assignment_id = p_teaching_assignment_id
      AND sc.score_type = 'MIDTERM'
      AND s.is_exempt = 0
    LIMIT 1;

    -- Diem cuoi ky
    SELECT s.score_value INTO v_final
    FROM scores s
    JOIN score_columns sc ON s.score_column_id = sc.id
    WHERE s.student_id = p_student_id
      AND sc.teaching_assignment_id = p_teaching_assignment_id
      AND sc.score_type = 'FINAL'
      AND s.is_exempt = 0
    LIMIT 1;

    -- Tinh DTB mon (lam tron 0.5)
    IF v_att_score IS NOT NULL AND v_final IS NOT NULL THEN
        SET v_average = ROUND(
            (COALESCE(v_att_score, 0)    * v_w_att  +
             COALESCE(v_oral_avg, 0)     * v_w_oral +
             COALESCE(v_min15_avg, 0)    * v_w_15m  +
             COALESCE(v_period1_avg, 0)  * v_w_1p   +
             COALESCE(v_midterm, 0)      * v_w_mid  +
             COALESCE(v_final, 0)        * v_w_fin)
        * 2) / 2;
    END IF;

    -- Xep loai
    IF v_average IS NOT NULL THEN
        SET v_class = CASE
            WHEN v_average >= 8.0 THEN 'EXCELLENT'
            WHEN v_average >= 6.5 THEN 'GOOD'
            WHEN v_average >= 5.0 THEN 'AVERAGE'
            WHEN v_average >= 3.5 THEN 'WEAK'
            ELSE 'POOR'
        END;
    END IF;

    -- Upsert vao grade_results
    INSERT INTO grade_results (
        student_id, teaching_assignment_id,
        attendance_score, oral_avg, min15_avg, period1_avg,
        midterm_score, final_score, average_score, classification
    ) VALUES (
        p_student_id, p_teaching_assignment_id,
        v_att_score, v_oral_avg, v_min15_avg, v_period1_avg,
        v_midterm, v_final, v_average, v_class
    )
    ON DUPLICATE KEY UPDATE
        attendance_score  = v_att_score,
        oral_avg          = v_oral_avg,
        min15_avg         = v_min15_avg,
        period1_avg       = v_period1_avg,
        midterm_score     = v_midterm,
        final_score       = v_final,
        average_score     = v_average,
        classification    = v_class,
        recalculated_at   = CURRENT_TIMESTAMP;
END$$

-- Tính xếp hạng GPA và hạng lớp cho 1 lớp trong 1 học kỳ
CREATE PROCEDURE sp_recalculate_class_ranking(
    IN p_class_id   BIGINT UNSIGNED,
    IN p_semester_id INT UNSIGNED
)
BEGIN
    -- Upsert student_rankings voi GPA co trong so
    INSERT INTO student_rankings (student_id, semester_id, gpa, total_students)
    SELECT
        gr_agg.student_id,
        p_semester_id,
        ROUND(
            SUM(gr_agg.average_score * sub.coefficient)
            / NULLIF(SUM(CASE WHEN gr_agg.average_score IS NOT NULL THEN sub.coefficient ELSE 0 END), 0)
        * 2) / 2 AS gpa,
        COUNT(DISTINCT s2.id) AS total_students
    FROM grade_results gr_agg
    JOIN teaching_assignments ta ON gr_agg.teaching_assignment_id = ta.id
    JOIN subjects sub ON ta.subject_id = sub.id
    JOIN students s2  ON s2.class_id = p_class_id AND s2.is_active = 1
    WHERE ta.class_id   = p_class_id
      AND ta.semester_id = p_semester_id
      AND gr_agg.average_score IS NOT NULL
    GROUP BY gr_agg.student_id
    ON DUPLICATE KEY UPDATE
        gpa            = VALUES(gpa),
        total_students = VALUES(total_students),
        calculated_at  = CURRENT_TIMESTAMP;

    -- Cap nhat xep hang trong lop
    SET @rank = 0;
    UPDATE student_rankings sr
    JOIN (
        SELECT student_id,
               @rank := @rank + 1 AS new_rank
        FROM student_rankings
        WHERE semester_id = p_semester_id
          AND student_id IN (SELECT id FROM students WHERE class_id = p_class_id)
        ORDER BY gpa DESC
    ) ranked ON sr.student_id = ranked.student_id AND sr.semester_id = p_semester_id
    SET sr.rank_in_class = ranked.new_rank;

    -- Cap nhat xep loai hoc luc
    UPDATE student_rankings
    SET academic_classification = CASE
        WHEN gpa >= 8.0 THEN 'EXCELLENT'
        WHEN gpa >= 6.5 THEN 'GOOD'
        WHEN gpa >= 5.0 THEN 'AVERAGE'
        WHEN gpa >= 3.5 THEN 'WEAK'
        ELSE 'POOR'
    END
    WHERE semester_id = p_semester_id
      AND student_id IN (SELECT id FROM students WHERE class_id = p_class_id);
END$$

DELIMITER ;

-- =============================================================================
-- TRIGGERS
-- =============================================================================

DELIMITER $$

-- Trigger: Tu dong tinh lai grade_result khi diem thay doi
CREATE TRIGGER trg_scores_after_insert
AFTER INSERT ON scores
FOR EACH ROW
BEGIN
    DECLARE v_ta_id BIGINT UNSIGNED;
    SELECT teaching_assignment_id INTO v_ta_id
    FROM score_columns WHERE id = NEW.score_column_id;
    CALL sp_recalculate_grade_result(NEW.student_id, v_ta_id);
END$$

CREATE TRIGGER trg_scores_after_update
AFTER UPDATE ON scores
FOR EACH ROW
BEGIN
    DECLARE v_ta_id BIGINT UNSIGNED;
    SELECT teaching_assignment_id INTO v_ta_id
    FROM score_columns WHERE id = NEW.score_column_id;
    CALL sp_recalculate_grade_result(NEW.student_id, v_ta_id);
END$$

-- Trigger: Tu dong tinh lai diem chuyen can khi diem danh thay doi
CREATE TRIGGER trg_attendance_after_insert
AFTER INSERT ON attendances
FOR EACH ROW
BEGIN
    CALL sp_recalculate_grade_result(NEW.student_id, NEW.teaching_assignment_id);
END$$

CREATE TRIGGER trg_attendance_after_update
AFTER UPDATE ON attendances
FOR EACH ROW
BEGIN
    CALL sp_recalculate_grade_result(NEW.student_id, NEW.teaching_assignment_id);
END$$

-- Trigger: Tu dong tinh lai diem thai do khi attitude record thay doi
CREATE TRIGGER trg_attitude_after_insert
AFTER INSERT ON attitude_records
FOR EACH ROW
BEGIN
    CALL sp_recalculate_grade_result(NEW.student_id, NEW.teaching_assignment_id);
END$$

DELIMITER ;

-- =============================================================================
-- DỮ LIỆU MẪU KHỞI TẠO
-- =============================================================================

-- Tài khoản Admin mặc định (password: Admin@123 - BCrypt)
INSERT INTO users (username, password_hash, email, full_name, is_active) VALUES
    ('admin', '$2a$12$LQv3c1yqBWVHxkd0LHAkCOYz6TtxMQJqhN8/LewFNijKg5Q7Vt2ym',
     'admin@thptlms.edu.vn', 'Quan tri vien He thong', 1);

INSERT INTO user_roles (user_id, role_id, assigned_by)
SELECT u.id, r.id, u.id
FROM users u, roles r
WHERE u.username = 'admin' AND r.name = 'ADMIN';

-- Năm học và học kỳ mẫu
INSERT INTO academic_years (name, start_date, end_date, is_active, created_by)
SELECT '2024-2025', '2024-09-02', '2025-05-31', 1, id
FROM users WHERE username = 'admin';

INSERT INTO semesters (academic_year_id, semester_number, name, start_date, end_date)
VALUES
    (1, 1, 'Hoc ky 1 (2024-2025)', '2024-09-02', '2025-01-18'),
    (1, 2, 'Hoc ky 2 (2024-2025)', '2025-01-20', '2025-05-31');

-- Phòng học mẫu
INSERT INTO rooms (room_code, name, capacity, room_type, floor, building) VALUES
    ('A101', 'Phong A101', 45, 'CLASSROOM', 1, 'Toa A'),
    ('A102', 'Phong A102', 45, 'CLASSROOM', 1, 'Toa A'),
    ('A201', 'Phong A201', 45, 'CLASSROOM', 2, 'Toa A'),
    ('B101', 'Phong May tinh B101', 40, 'LAB', 1, 'Toa B'),
    ('B102', 'Phong Thi nghiem B102', 35, 'LAB', 1, 'Toa B');

-- =============================================================================
-- BẬT LẠI FOREIGN KEY CHECKS
-- =============================================================================
SET FOREIGN_KEY_CHECKS = 1;

-- =============================================================================
-- THỐNG KÊ TỔNG QUAN
-- =============================================================================
SELECT 'THPT LMS Database da khoi tao thanh cong!' AS message;
SELECT
    (SELECT COUNT(*) FROM information_schema.TABLES
     WHERE TABLE_SCHEMA = 'thpt_lms' AND TABLE_TYPE = 'BASE TABLE') AS tong_so_bang,
    (SELECT COUNT(*) FROM information_schema.VIEWS
     WHERE TABLE_SCHEMA = 'thpt_lms')                                AS tong_so_view,
    (SELECT COUNT(*) FROM information_schema.ROUTINES
     WHERE ROUTINE_SCHEMA = 'thpt_lms')                              AS tong_stored_procedure,
    (SELECT COUNT(*) FROM information_schema.TRIGGERS
     WHERE TRIGGER_SCHEMA = 'thpt_lms')                              AS tong_so_trigger;