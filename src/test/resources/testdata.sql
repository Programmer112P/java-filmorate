INSERT INTO genre (name)
VALUES ('Комедия'),
       ('Драма'),
       ('Мультфильм'),
       ('Триллер'),
       ('Документальный'),
       ('Боевик');

INSERT INTO mpa (name)
VALUES ('G'),
       ('PG'),
       ('PG-13'),
       ('R'),
       ('NC-17');

INSERT INTO USERS (EMAIL, LOGIN, NAME, BIRTHDAY)
VALUES ('1', '1', '1', '2000-01-10'),
       ('2', '2', '2', '2000-02-20');

INSERT INTO FILMS (MPA_ID, NAME, DESCRIPTION, RELEASE_DATE, DURATION, RATE)
VALUES (1, 'Name', 'Description', '2000-10-10', 120, 2);

INSERT INTO FILM_GENRE (FILM_ID, GENRE_ID)
VALUES (1, 1);

INSERT INTO FILM_USER_LIKE (USER_ID, FILM_ID)
VALUES (1, 1)
