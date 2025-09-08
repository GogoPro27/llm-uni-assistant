BEGIN;

WITH stud_seed(name, surname, email, password_hash, student_index) AS (
    VALUES ('Александар', 'Илиевски', 'aleksandar.ilievski@finki.ukim.mk', 'password', 223000),
                                                                              ('Марија', 'Стојановска', 'marija.stojanovska@finki.ukim.mk', 'password', 223001),
                                                                              ('Бојан', 'Петровски', 'bojan.petrovski@finki.ukim.mk', 'password', 223002),
                                                                              ('Елена', 'Димитрова', 'elena.dimitrova@finki.ukim.mk', 'password', 223003),
                                                                              ('Горан', 'Николовски', 'goran.nikolovski@finki.ukim.mk', 'password', 223004),
                                                                              ('Ивана', 'Трајковска', 'ivana.trajkovska@finki.ukim.mk', 'password', 223005),
                                                                              ('Даниел', 'Андреевски', 'daniel.andreevski@finki.ukim.mk', 'password', 223006),
                                                                              ('Кристина', 'Јовановска', 'kristina.jovanovska@finki.ukim.mk', 'password', 223007),
                                                                              ('Марко', 'Георгиевски', 'marko.georgievski@finki.ukim.mk', 'password', 223008),
                                                                              ('Сара', 'Костадиновска', 'sara.kostadinovska@finki.ukim.mk', 'password', 223009),
                                                                              ('Филип', 'Ангеловски', 'filip.angelovski@finki.ukim.mk', 'password', 223010),
                                                                              ('Анна', 'Ристеска', 'anna.risteska@finki.ukim.mk', 'password', 223011),
                                                                              ('Виктор', 'Митревски', 'viktor.mitrevski@finki.ukim.mk', 'password', 223012),
                                                                              ('Наташа', 'Павловска', 'natasha.pavlovska@finki.ukim.mk', 'password', 223013),
                                                                              ('Игор', 'Симоновски', 'igor.simonovski@finki.ukim.mk', 'password', 223014),
                                                                              ('Катарина', 'Петковска', 'katarina.petkovska@finki.ukim.mk', 'password', 223015),
                                                                              ('Стефан', 'Лазаревски', 'stefan.lazarevski@finki.ukim.mk', 'password', 223016),
                                                                              ('Тамара', 'Василевска', 'tamara.vasilevska@finki.ukim.mk', 'password', 223017),
                                                                              ('Дејан', 'Поповски', 'dejan.popovski@finki.ukim.mk', 'password', 223018),
                                                                              ('Јована', 'Глигорова', 'jovana.gligorova@finki.ukim.mk', 'password', 223019)
),
inserted_stud_users AS (
    INSERT INTO users (email, password_hash, name, surname)
    SELECT email, password_hash, name, surname
    FROM stud_seed
    ON CONFLICT (email) DO UPDATE
        SET password_hash = EXCLUDED.password_hash,
            name = EXCLUDED.name,
            surname = EXCLUDED.surname
    RETURNING user_id, email
),
inserted_stud_roles AS (
    INSERT INTO user_roles (user_id, role)
    SELECT user_id, 'student'::user_role
    FROM inserted_stud_users
    ON CONFLICT DO NOTHING
    RETURNING user_id
)
INSERT INTO students (student_id, student_index)
SELECT u.user_id, ss.student_index
FROM inserted_stud_users u
JOIN stud_seed ss ON ss.email = u.email
ON CONFLICT (student_id) DO UPDATE
    SET student_index = EXCLUDED.student_index;

COMMIT;
