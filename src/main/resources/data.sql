-- INSERT INTO USERS (USER_ID, USER_EMAIL, USER_LOGIN, USER_NAME, USER_BIRTHDAY)
-- VALUES (1, '1', '1', '1', '1111-11-11'),
--        (2, '2', '2', '2', '1112-11-12'),
--        (3, '3', '3', '3', '1113-11-13'),
--        (4, '4', '4', '4', '1114-11-14'),
--        (5, '5', '5', '5', '1115-11-15');
--

-- INSERT INTO FRIENDSHIP (USER_FROM, USER_TO, FRIENDSHIP_STATUS_ID)
-- VALUES (1, 2, 1),
--        (2, 3, 1),
--        (3, 1, 1),
--        (1, 4, 1);
INSERT INTO FRIENDSHIP_STATUS (FRIENDSHIP_STATUS_ID, FRIENDSHIP_STATUS_NAME)
VALUES (1, 'request');

INSERT INTO genre (genre_name)
VALUES ('Комедия'),
       ('Драма'),
       ('Мультфильм'),
       ('Триллер'),
       ('Документальный'),
       ('Боевик');

INSERT INTO mpa (mpa_name)
VALUES ('G'),
       ('PG'),
       ('PG-13'),
       ('R'),
       ('NC-17');

