package ru.yandex.practicum.filmorate.dao.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.FilmDao;
import ru.yandex.practicum.filmorate.dao.GenreDao;
import ru.yandex.practicum.filmorate.dao.insert.FilmSimpleJdbcInsert;
import ru.yandex.practicum.filmorate.entity.Film;
import ru.yandex.practicum.filmorate.entity.Genre;
import ru.yandex.practicum.filmorate.entity.Mpa;
import ru.yandex.practicum.filmorate.exception.DAOException;
import ru.yandex.practicum.filmorate.exception.DbConnectionException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Repository
@Slf4j
public class FilmDaoImpl implements FilmDao {
    private final GenreDao genreDao;
    private final JdbcTemplate jdbcTemplate;
    private final FilmSimpleJdbcInsert filmSimpleJdbcInsert;

    @Autowired
    public FilmDaoImpl(GenreDao genreDao, JdbcTemplate jdbcTemplate, FilmSimpleJdbcInsert filmSimpleJdbcInsert) {
        this.genreDao = genreDao;
        this.jdbcTemplate = jdbcTemplate;
        this.filmSimpleJdbcInsert = filmSimpleJdbcInsert;
    }

    @Override
    public void removeLike(final long filmId, final long userId) {
        log.info("FilmDao removeLike: Запрос на удаление лайка от пользователя с id {} фильму с id {}", userId, filmId);
        String sql = "DELETE FROM film_user_likes WHERE film_id = ? AND user_id = ?";
        try {
            int amount = jdbcTemplate.update(sql, filmId, userId);
            if (amount == 0) {
                log.error("FilmDao removeLike: Не существует пользователя с id {} или фильма с id {}. " +
                        "Либо не было такого лайка", userId, filmId);
                String message = String.format("FilmDao removeLike: Не существует пользователя с id %d или фильма с id %d. " +
                        "Либо не было такого лайка", userId, filmId);
                throw new DAOException(message);
            }
            log.info("FilmDao removeLike: Удален лайк от пользователя с id {} фильму с id {}", userId, filmId);
        } catch (DataAccessException e) {
            log.info("FilmDao removeLike: Ошибка при выполнении запроса {}. Id пользователя {}, id фильма {}", sql, userId, filmId);
            throw new DbConnectionException(e.getMessage(), e);
        }
    }

    @Override
    public void addLike(final long filmId, final long userId) {
        log.info("FilmDao addLike: Запрос на добавление лайка от пользователя с id {} фильму с id {}", userId, filmId);
        String sql = "INSERT INTO film_user_likes (user_id, film_id) VALUES (?,?);";
        try {
            jdbcTemplate.update(sql, userId, filmId);
            log.info("FilmDao addLike: Добавлен лайк от пользователя с id {} фильму с id {}", userId, filmId);
        } catch (DataAccessException e) {
            log.error("FilmDao addLike: Ошибка при выполнении запроса {}. " +
                    "Не существует пользователя с id {} или фильма с id {}", sql, userId, filmId);
            throw new DAOException(e.getMessage(), e);
        }
    }

    @Override
    public List<Film> getMostPopulars(final int amount) {
        log.info("FilmDao getMostPopulars: Запрос на получение {} самых популярных фильмов", amount);
        String sql = "SELECT f.film_id, f.mpa_id, mpa.mpa_name, f.film_name, f.film_description, " +
                "f.film_release_date, f.film_duration, COUNT(fl.user_id) AS likes_amount " +
                "FROM films AS f " +
                "LEFT JOIN film_user_likes AS fl ON f.film_id = fl.film_id " +
                "LEFT JOIN mpa ON f.mpa_id = mpa.mpa_id " +
                "GROUP BY f.film_id ORDER BY likes_amount DESC " +
                "LIMIT ?;";
        try {
            List<Film> popularFilms = jdbcTemplate.query(sql, this::mapRowToFilm, amount);
            log.info("FilmDao getMostPopulars: Получено {} самых популярных фильмов", amount);
            return popularFilms;
        } catch (DataAccessException e) {
            log.error("FilmDao getMostPopulars: Ошибка при выполнении запроса {}", sql);
            throw new DbConnectionException(e.getMessage(), e);
        }
    }

    @Override
    public Film getById(final long id) {
        log.info("FilmDao getById: Запрос на получение фильма с id {}", id);
        String sql = "SELECT * FROM films AS f INNER JOIN mpa ON f.mpa_id = mpa.mpa_id WHERE f.film_id = ?;";
        try {
            Film film = jdbcTemplate.queryForObject(sql, this::mapRowToFilm, id);
            log.info("FilmDao getById: Получен фильма с id {}", id);
            return film;
        } catch (IncorrectResultSizeDataAccessException e) {
            log.error("FilmDao getById: Не существует фильма с id {}", id);
            throw new DAOException(e.getMessage(), e);
        } catch (DataAccessException e) {
            log.error("FilmDao getById: Ошибка при выполнении запроса {}", sql);
            throw new DbConnectionException(e.getMessage(), e);
        }
    }

    @Override
    public List<Film> getAll() {
        log.info("FilmDao getAll: Запрос на получение всех фильмов");
        String sql = "SELECT * FROM films AS f INNER JOIN mpa ON f.mpa_id = mpa.mpa_id;";
        try {
            List<Film> films = jdbcTemplate.query(sql, this::mapRowToFilm);
            log.info("FilmDao getAll: Получены все фильмы");
            return films;
        } catch (DataAccessException e) {
            log.error("FilmDao getAll: Ошибка при доступе к базе данных");
            throw new DbConnectionException(e.getMessage(), e);
        }
    }

    @Override
    public Film create(Film film) {
        log.info("FilmDao create: Запрос на создание фильма {}", film);
        try {
            long filmId = filmSimpleJdbcInsert.executeAndReturnKey(film.toMap()).longValue();
            if (film.getGenres() != null) {
                insertGenres(film, filmId);
            }
            log.info("FilmDao create: Создан фильм с id {}", filmId);
            film.setId(filmId);
            return film;
        } catch (DataAccessException e) {
            log.error("FilmDao create: Ошибка при создании фильма {}", film);
            throw new DbConnectionException(e.getMessage(), e);
        }
    }

    @Override
    public Film update(final Film film) {
        log.info("FilmDao update: Запрос на обновление фильма {}", film);
        long filmId = film.getId();
        String deleteOldGenresSql = "DELETE FROM film_genres WHERE film_id = ?;";
        String updateFilmSql = "UPDATE films " +
                "SET film_name = ?, film_description = ?, film_release_date = ?, film_duration = ?, mpa_id = ? " +
                "WHERE film_id = ?;";
        if (film.getGenres() != null) {
            try {
                jdbcTemplate.update(deleteOldGenresSql, filmId);//Если id не существует, то БД не изменится
            } catch (DataAccessException e) {
                log.error("FilmDao update: Ошибка при выполнении запроса {} с id {}", deleteOldGenresSql, filmId);
                throw new DAOException(e.getMessage(), e);
            }
            insertGenres(film, filmId);//Тут уже вылетит исключение, если id не существует
        }
        try {
            int amount = jdbcTemplate.update(updateFilmSql, film.getName(), film.getDescription(), film.getReleaseDate(),
                    film.getDuration(), film.getMpa().getId(), filmId);
            if (amount == 0) {
                log.error("FilmDao update: Не существует фильма с id: {}", filmId);
                throw new DAOException(String.format("FilmDao update: Не существует пользователя с id: %d", filmId));
            }
            log.info("FilmDao update: Обновление фильма с id {}", filmId);
            return film;
        } catch (DataAccessException e) {
            log.error("FilmDao update: Ошибка при выполнении запроса {}", updateFilmSql);
            throw new DbConnectionException(e.getMessage(), e);
        }
    }

    private void insertGenres(Film film, long filmId) {
        List<Long> genresId = film.getGenres().stream().map(Genre::getId).collect(Collectors.toList());
        String addNewGenresSql = "INSERT INTO film_genres (film_id, genre_id) VALUES (?,?);";
        try {
            jdbcTemplate.batchUpdate(addNewGenresSql, new BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement ps, int i) throws SQLException {
                    ps.setLong(1, filmId);
                    ps.setLong(2, genresId.get(i));
                }

                @Override
                public int getBatchSize() {
                    return genresId.size();
                }
            });
        } catch (DataAccessException e) {
            log.error("FilmDao insertGenres: Ошибка при выполнении запроса {}. Фильма с id {} не существует. " +
                    "Либо одного жанров не существует ids: {}", addNewGenresSql, filmId, genresId);
            throw new DAOException(e.getMessage(), e);
        }
    }

    private Film mapRowToFilm(final ResultSet rs, final int rowNum) throws SQLException {
        long filmId = rs.getLong("film_id");
        return Film.builder()
                .id(filmId)
                .genres(new HashSet<>(genreDao.getGenresOfFilm(filmId)))
                .name(rs.getString("film_name"))
                .mpa(Mpa.builder().id(rs.getLong("mpa_id")).name(rs.getString("mpa_name")).build())
                .description(rs.getString("film_description"))
                .duration(rs.getLong("film_duration"))
                .releaseDate(rs.getDate("film_release_date").toLocalDate())
                .build();
    }
}
