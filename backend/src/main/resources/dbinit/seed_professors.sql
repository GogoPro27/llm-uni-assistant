BEGIN;

WITH prof_seed(name, surname, email, password_hash, short_name) AS (
    VALUES ('Ѓорѓи', 'Маџаров', 'gjorgji.madzharov@finki.ukim.mk', 'password', 'GM'),
                                                                               ('Љупчо', 'Антовски', 'ljupcho.antovski@finki.ukim.mk', 'password', 'LA'),
                                                                               ('Александар', 'Стојменски', 'aleksandar.stojmenski@finki.ukim.mk', 'password', 'AS'),
                                                                               ('Александра', 'Дединец', 'aleksandra.dedinec@finki.ukim.mk', 'password', 'AD'),
                                                                               ('Александра', 'Митровиќ', 'aleksandra.mitrovikj@finki.ukim.mk', 'password', 'AM'),
                                                                               ('Ана', 'Богданова', 'ana.bogdanova@finki.ukim.mk', 'password', 'AB'),
                                                                               ('Анастас', 'Мишев', 'anastas.mishev@finki.ukim.mk', 'password', 'AMi'),
                                                                               ('Андреа', 'Кулаков', 'andrea.kulakov@finki.ukim.mk', 'password', 'AK'),
                                                                               ('Андреја', 'Наумоски', 'andreja.naumoski@finki.ukim.mk', 'password', 'AN'),
                                                                               ('Билјана', 'Ристеска', 'biljana.risteska@finki.ukim.mk', 'password', 'BR'),
                                                                               ('Билјана', 'Рибарски', 'biljana.ribarski@finki.ukim.mk', 'password', 'BRi'),
                                                                               ('Бобан', 'Јоксимоски', 'boban.joksimoski@finki.ukim.mk', 'password', 'BJ'),
                                                                               ('Боро', 'Јакимовски', 'boro.jakimovski@finki.ukim.mk', 'password', 'BJa'),
                                                                               ('Бојан', 'Илијоски', 'bojan.ilijoski@finki.ukim.mk', 'password', 'BI'),
                                                                               ('Бојана', 'Котеска', 'bojana.koteska@finki.ukim.mk', 'password', 'BK'),
                                                                               ('Вангел', 'Ајановски', 'vangel.ajanovski@finki.ukim.mk', 'password', 'VA'),
                                                                               ('Верица', 'Смиљкова', 'verica.smiljkova@finki.ukim.mk', 'password', 'VS'),
                                                                               ('Весна', 'Ристовска', 'vesna.ristovska@finki.ukim.mk', 'password', 'VR'),
                                                                               ('Весна', 'Димитрова', 'vesna.dimitrova@finki.ukim.mk', 'password', 'VD'),
                                                                               ('Владимир', 'Здравески', 'vladimir.zdraveski@finki.ukim.mk', 'password', 'VZ'),
                                                                               ('Владимир', 'Трајковиќ', 'vladimir.trajkovikj@finki.ukim.mk', 'password', 'VT'),
                                                                               ('Георгина', 'Мирчева', 'georgina.mircheva@finki.ukim.mk', 'password', 'GMi'),
                                                                               ('Горан', 'Велинов', 'goran.velinov@finki.ukim.mk', 'password', 'GV'),
                                                                               ('Гоце', 'Арменски', 'goce.armenski@finki.ukim.mk', 'password', 'GA'),
                                                                               ('Дејан', 'Ѓорѓевиќ', 'dejan.gjorgjevikj@finki.ukim.mk', 'password', 'DG'),
                                                                               ('Дејан', 'Спасов', 'dejan.spasov@finki.ukim.mk', 'password', 'DS'),
                                                                               ('Димитар', 'Трајанов', 'dimitar.trajanov@finki.ukim.mk', 'password', 'DT'),
                                                                               ('Елена', 'Ризов', 'elena.rizov@finki.ukim.mk', 'password', 'ER'),
                                                                               ('Емил', 'Станков', 'emil.stankov@finki.ukim.mk', 'password', 'ES'),
                                                                               ('Ефтим', 'Здравевски', 'eftim.zdravevski@finki.ukim.mk', 'password', 'EZ'),
                                                                               ('Иван', 'Китановски', 'ivan.kitanovski@finki.ukim.mk', 'password', 'IK'),
                                                                               ('Иван', 'Чорбев', 'ivan.chorbev@finki.ukim.mk', 'password', 'IC'),
                                                                               ('Ивица', 'Димитровски', 'ivica.dimitrovski@finki.ukim.mk', 'password', 'ID'),
                                                                               ('Игор', 'Мишковски', 'igor.mishkovski@finki.ukim.mk', 'password', 'IM'),
                                                                               ('Илинка', 'Иваноска', 'ilinka.ivanoska@finki.ukim.mk', 'password', 'II'),
                                                                               ('Калина', 'Сотироска', 'kalina.sotiroska@finki.ukim.mk', 'password', 'KS'),
                                                                               ('Катарина', 'Динева', 'katarina.dineva@finki.ukim.mk', 'password', 'KD'),
                                                                               ('Катерина', 'Здравкова', 'katerina.zdravkova@finki.ukim.mk', 'password', 'KZ'),
                                                                               ('Кире', 'Триводалиев', 'kire.trivodaliev@finki.ukim.mk', 'password', 'KT'),
                                                                               ('Коста', 'Митрески', 'kosta.mitreski@finki.ukim.mk', 'password', 'KM'),
                                                                               ('Костадин', 'Мишев', 'kostadin.mishev@finki.ukim.mk', 'password', 'KMi'),
                                                                               ('Ласко', 'Баснарков', 'lasko.basnarkov@finki.ukim.mk', 'password', 'LB'),
                                                                               ('Магдалена', 'Ѓорчевска', 'magdalena.gjorchevska@finki.ukim.mk', 'password', 'MG'),
                                                                               ('Марија', 'Михова', 'marija.mihova@finki.ukim.mk', 'password', 'MM'),
                                                                               ('Марјан', 'Гушев', 'marjan.gushev@finki.ukim.mk', 'password', 'MGu'),
                                                                               ('Методија', 'Јанчески', 'metodija.jancheski@finki.ukim.mk', 'password', 'MJ'),
                                                                               ('Миле', 'Јованов', 'mile.jovanov@finki.ukim.mk', 'password', 'MJo'),
                                                                               ('Милош', 'Јовановиќ', 'milosh.jovanovikj@finki.ukim.mk', 'password', 'MJ2'),
                                                                               ('Мирослав', 'Мирчев', 'miroslav.mirchev@finki.ukim.mk', 'password', 'MMi'),
                                                                               ('Моника', 'Симјаноска', 'monika.simjanoska@finki.ukim.mk', 'password', 'MS'),
                                                                               ('Наташа', 'Илиевска', 'natasha.ilievska@finki.ukim.mk', 'password', 'NI'),
                                                                               ('Невена', 'Ацковска', 'nevena.ackovska@finki.ukim.mk', 'password', 'nan'),
                                                                               ('Панче', 'Рибарски', 'panche.ribarski@finki.ukim.mk', 'password', 'PR'),
                                                                               ('Петре', 'Ламески', 'petre.lameski@finki.ukim.mk', 'password', 'PL'),
                                                                               ('Ристе', 'Стојанов', 'riste.stojanov@finki.ukim.mk', 'password', 'RS'),
                                                                               ('Сашо', 'Граматиков', 'sasho.gramatikov@finki.ukim.mk', 'password', 'SG'),
                                                                               ('Сите', 'професори', 'site.profesori@finki.ukim.mk', 'password', 'Sp'),
                                                                               ('Слободан', 'Калајџиски', 'slobodan.kalajdzhiski@finki.ukim.mk', 'password', 'SK'),
                                                                               ('Смилка', 'Саркањац', 'smilka.sarkanjac@finki.ukim.mk', 'password', 'SS'),
                                                                               ('Соња', 'Гиевска', 'sonja.gievska@finki.ukim.mk', 'password', 'SGi'),
                                                                               ('Соња', 'Филипоска', 'sonja.filiposka@finki.ukim.mk', 'password', 'SF'),
                                                                               ('Сузана', 'Лошковска', 'suzana.loshkovska@finki.ukim.mk', 'password', 'SL'),
                                                                               ('Христина', 'Трпческа', 'hristina.trpcheska@finki.ukim.mk', 'password', 'HT'),
                                                                               ('Јана', 'Кузманова', 'jana.kuzmanova@finki.ukim.mk', 'password', 'JK'),
                                                                               ('Јована', 'Добрева', 'jovana.dobreva@finki.ukim.mk', 'password', 'JD'),
                                                                               ('Александар', 'Тенев', 'aleksandar.tenev@finki.ukim.mk', 'password', 'AT'),
                                                                               ('Ана', 'Тодоровска', 'ana.todorovska@finki.ukim.mk', 'password', 'ATo'),
                                                                               ('Владислав', 'Бидиков', 'vladislav.bidikov@finki.ukim.mk', 'password', 'VB'),
                                                                               ('Влатко', 'Спасев', 'vlatko.spasev@finki.ukim.mk', 'password', 'VSp'),
                                                                               ('Војдан', 'Ќорвезироски', 'vojdan.kjorveziroski@finki.ukim.mk', 'password', 'VK'),
                                                                               ('Дарко', 'Сасански', 'darko.sasanski@finki.ukim.mk', 'password', 'DSa'),
                                                                               ('Димитар', 'Китановски', 'dimitar.kitanovski@finki.ukim.mk', 'password', 'DK'),
                                                                               ('Димитар', 'Милески', 'dimitar.mileski@finki.ukim.mk', 'password', 'DM'),
                                                                               ('Димитар', 'Пешевски', 'dimitar.peshevski@finki.ukim.mk', 'password', 'DP'),
                                                                               ('Евгенија', 'Крајчевска', 'evgenija.krajchevska@finki.ukim.mk', 'password', 'EK'),
                                                                               ('Елена', 'Атанасоска', 'elena.atanasoska@finki.ukim.mk', 'password', 'EA'),
                                                                               ('Ема', 'Пандилова', 'ema.pandilova@finki.ukim.mk', 'password', 'EP'),
                                                                               ('Живко', 'Атанаскоски', 'zhivko.atanaskoski@finki.ukim.mk', 'password', 'ZA'),
                                                                               ('Зорица', 'Карапанчева', 'zorica.karapancheva@finki.ukim.mk', 'password', 'ZK'),
                                                                               ('Кирил', 'Ќироски', 'kiril.kjiroski@finki.ukim.mk', 'password', 'KK'),
                                                                               ('Марија', 'Стојчева', 'marija.stojcheva@finki.ukim.mk', 'password', 'MSt'),
                                                                               ('Марија', 'Танеска', 'marija.taneska@finki.ukim.mk', 'password', 'MT'),
                                                                               ('Марко', 'Петров', 'marko.petrov@finki.ukim.mk', 'password', 'MP'),
                                                                               ('Мартин', 'Динев', 'martin.dinev@finki.ukim.mk', 'password', 'MD'),
                                                                               ('Мартина', 'Тошевска', 'martina.toshevska@finki.ukim.mk', 'password', 'MTo'),
                                                                               ('Мила', 'Додевска', 'mila.dodevska@finki.ukim.mk', 'password', 'MDo'),
                                                                               ('Милан', 'Тодоровиќ', 'milan.todorovikj@finki.ukim.mk', 'password', 'MT2'),
                                                                               ('Милена', 'Трајаноска', 'milena.trajanoska@finki.ukim.mk', 'password', 'MTr'),
                                                                               ('Ненад', 'Анчев', 'nenad.anchev@finki.ukim.mk', 'password', 'NAn'),
                                                                               ('Петар', 'Секулоски', 'petar.sekuloski@finki.ukim.mk', 'password', 'PS'),
                                                                               ('Сијче', 'Печкова', 'sijche.pechkova@finki.ukim.mk', 'password', 'SP'),
                                                                               ('Славе', 'Темков', 'slave.temkov@finki.ukim.mk', 'password', 'ST'),
                                                                               ('Стефан', 'Андонов', 'stefan.andonov@finki.ukim.mk', 'password', 'SA')
),
inserted_prof_users AS (
    INSERT INTO users (email, password_hash, name, surname)
    SELECT email, password_hash, name, surname
    FROM prof_seed
    ON CONFLICT (email) DO UPDATE
        SET password_hash = EXCLUDED.password_hash,
            name = EXCLUDED.name,
            surname = EXCLUDED.surname
    RETURNING user_id, name, surname, email
),
inserted_prof_roles AS (
    INSERT INTO user_roles (user_id, role)
    SELECT user_id, 'professor'::user_role
    FROM inserted_prof_users
    ON CONFLICT DO NOTHING
    RETURNING user_id
)
INSERT INTO professors (professor_id, short_name)
SELECT u.user_id, ps.short_name
FROM inserted_prof_users u
JOIN prof_seed ps ON ps.email = u.email
ON CONFLICT (professor_id) DO UPDATE
    SET short_name = EXCLUDED.short_name;

COMMIT;
