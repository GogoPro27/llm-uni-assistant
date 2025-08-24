BEGIN;

-- =========================
-- 15 PROFESSORS
-- =========================
WITH prof_seed(name, surname, email, password_hash, short_name) AS (VALUES ('John', 'Doe', 'john.doe@univ.example',
                                                                            '$2b$12$examplehashProf01', 'J. Doe'),
                                                                           ('Emma', 'Stone', 'emma.stone@univ.example',
                                                                            '$2b$12$examplehashProf02', 'E. Stone'),
                                                                           ('Victor', 'Hughes',
                                                                            'victor.hughes@univ.example',
                                                                            '$2b$12$examplehashProf03', 'V. Hughes'),
                                                                           ('Alice', 'Ng', 'alice.ng@univ.example',
                                                                            '$2b$12$examplehashProf04', 'A. Ng'),
                                                                           ('Brian', 'Kim', 'brian.kim@univ.example',
                                                                            '$2b$12$examplehashProf05', 'B. Kim'),
                                                                           ('Clara', 'Mendez',
                                                                            'clara.mendez@univ.example',
                                                                            '$2b$12$examplehashProf06', 'C. Mendez'),
                                                                           ('Dmitri', 'Ivanov',
                                                                            'dmitri.ivanov@univ.example',
                                                                            '$2b$12$examplehashProf07', 'D. Ivanov'),
                                                                           ('Elena', 'Garcia',
                                                                            'elena.garcia@univ.example',
                                                                            '$2b$12$examplehashProf08', 'E. Garcia'),
                                                                           ('Farid', 'Rahman',
                                                                            'farid.rahman@univ.example',
                                                                            '$2b$12$examplehashProf09', 'F. Rahman'),
                                                                           ('Grace', 'Li', 'grace.li@univ.example',
                                                                            '$2b$12$examplehashProf10', 'G. Li'),
                                                                           ('Hannah', 'Schmidt',
                                                                            'hannah.schmidt@univ.example',
                                                                            '$2b$12$examplehashProf11', 'H. Schmidt'),
                                                                           ('Ivan', 'Petrov',
                                                                            'ivan.petrov@univ.example',
                                                                            '$2b$12$examplehashProf12', 'I. Petrov'),
                                                                           ('Julia', 'Rossi',
                                                                            'julia.rossi@univ.example',
                                                                            '$2b$12$examplehashProf13', 'J. Rossi'),
                                                                           ('Kenji', 'Sato', 'kenji.sato@univ.example',
                                                                            '$2b$12$examplehashProf14', 'K. Sato'),
                                                                           ('Lina', 'Haddad',
                                                                            'lina.haddad@univ.example',
                                                                            '$2b$12$examplehashProf15', 'L. Haddad')),
     inserted_prof_users AS (
INSERT
INTO users (email, password_hash, name, surname)
SELECT email, password_hash, name, surname
FROM prof_seed RETURNING user_id, name, surname, email
),
inserted_prof_roles AS (
INSERT
INTO user_roles (user_id, role)
SELECT user_id, 'professor'::user_role
FROM inserted_prof_users
    RETURNING user_id
    )
INSERT
INTO professors (professor_id, short_name)
SELECT u.user_id,
       ps.short_name
FROM inserted_prof_users u
         JOIN prof_seed ps ON ps.email = u.email;

-- =========================
-- 15 STUDENTS
-- =========================
WITH stud_seed(name, surname, email, password_hash, student_index) AS (VALUES ('Dan', 'Brown',
                                                                               'dan.brown@student.example',
                                                                               '$2b$12$examplehashStud01', 100001),
                                                                              ('Riley', 'Adams',
                                                                               'riley.adams@student.example',
                                                                               '$2b$12$examplehashStud02', 100002),
                                                                              ('Peter', 'Young',
                                                                               'peter.young@student.example',
                                                                               '$2b$12$examplehashStud03', 100003),
                                                                              ('Sara', 'Lopez',
                                                                               'sara.lopez@student.example',
                                                                               '$2b$12$examplehashStud04', 100004),
                                                                              ('Tom', 'Baker',
                                                                               'tom.baker@student.example',
                                                                               '$2b$12$examplehashStud05', 100005),
                                                                              ('Uma', 'Khan',
                                                                               'uma.khan@student.example',
                                                                               '$2b$12$examplehashStud06', 100006),
                                                                              ('Viktor', 'Novak',
                                                                               'viktor.novak@student.example',
                                                                               '$2b$12$examplehashStud07', 100007),
                                                                              ('Wendy', 'Clark',
                                                                               'wendy.clark@student.example',
                                                                               '$2b$12$examplehashStud08', 100008),
                                                                              ('Xavier', 'Nguyen',
                                                                               'xavier.nguyen@student.example',
                                                                               '$2b$12$examplehashStud09', 100009),
                                                                              ('Yara', 'Silva',
                                                                               'yara.silva@student.example',
                                                                               '$2b$12$examplehashStud10', 100010),
                                                                              ('Zane', 'Carter',
                                                                               'zane.carter@student.example',
                                                                               '$2b$12$examplehashStud11', 100011),
                                                                              ('Noah', 'Fischer',
                                                                               'noah.fischer@student.example',
                                                                               '$2b$12$examplehashStud12', 100012),
                                                                              ('Ava', 'Moretti',
                                                                               'ava.moretti@student.example',
                                                                               '$2b$12$examplehashStud13', 100013),
                                                                              ('Leo', 'Martins',
                                                                               'leo.martins@student.example',
                                                                               '$2b$12$examplehashStud14', 100014),
                                                                              ('Mia', 'Kowalski',
                                                                               'mia.kowalski@student.example',
                                                                               '$2b$12$examplehashStud15', 100015)),
     inserted_stud_users AS (
INSERT
INTO users (email, password_hash, name, surname)
SELECT email, password_hash, name, surname
FROM stud_seed RETURNING user_id, email
),
inserted_stud_roles AS (
INSERT
INTO user_roles (user_id, role)
SELECT user_id, 'student'::user_role
FROM inserted_stud_users
    RETURNING user_id
    )
INSERT
INTO students (student_id, student_index)
SELECT u.user_id,
       ss.student_index
FROM inserted_stud_users u
         JOIN stud_seed ss ON ss.email = u.email;

-- =========================
-- 15 SUBJECTS
-- =========================
INSERT INTO subjects (name, short_name, code)
VALUES ('Calculus I', 'Calc I', 'CALC101'),
       ('Calculus II', 'Calc II', 'CALC102'),
       ('Linear Algebra', 'LinAlg', 'MATH201'),
       ('Discrete Mathematics', 'Discrete', 'MATH210'),
       ('Probability', 'Prob', 'STAT220'),
       ('Statistics', 'Stats', 'STAT230'),
       ('Algorithms', 'Algo', 'CS301'),
       ('Data Structures', 'DS', 'CS302'),
       ('Operating Systems', 'OS', 'CS310'),
       ('Databases', 'DB', 'CS320'),
       ('Computer Networks', 'Networks', 'CS330'),
       ('Numerical Methods', 'Numerical', 'MATH340'),
       ('Differential Equations', 'DiffEq', 'MATH350'),
       ('Machine Learning', 'ML', 'CS360'),
       ('Software Engineering', 'SWE', 'CS370');

COMMIT;