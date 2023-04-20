package ru.yandex.practicum.filmorate.dao.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.FilmDao;
import ru.yandex.practicum.filmorate.dao.impl.mapper.FilmRowMapper;
import ru.yandex.practicum.filmorate.entity.Film;
import ru.yandex.practicum.filmorate.entity.Genre;
import ru.yandex.practicum.filmorate.exception.DAOException;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;
import java.sql.Date;

@Repository
@Slf4j
public class FilmDaoImpl implements FilmDao {
    private final JdbcTemplate jdbcTemplate;
    private final FilmRowMapper filmRowMapper;

    @Autowired
    public FilmDaoImpl(JdbcTemplate jdbcTemplate, FilmRowMapper filmRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.filmRowMapper = filmRowMapper;
    }

    @Override
    public Film getById(final long id) {
        log.info("FilmDao getById: Запрос на получение фильма с id {}", id);
        String sql = "SELECT f.film_id, f.mpa_id, f.name, f.description, f.release_date, f.duration, f.rate, MPA.name AS mpa_name " +
                " FROM films AS f INNER JOIN mpa ON f.mpa_id = mpa.mpa_id WHERE f.film_id = ?;";
        try {
            Film film = jdbcTemplate.queryForObject(sql, filmRowMapper, id);
            log.info("FilmDao getById: Получен фильма с id {}", id);
            return film;
        } catch (IncorrectResultSizeDataAccessException e) {
            log.error("FilmDao getById: Не существует фильма с id {}", id);
            throw new DAOException(e.getMessage(), e);
        }
    }

    @Override
    public List<Film> getAll() {
        log.info("FilmDao getAll: Запрос на получение всех фильмов");
        String sql = "SELECT f.film_id, f.mpa_id, f.name, f.description, f.release_date, f.duration, f.rate, MPA.name AS mpa_name " +
                "FROM films AS f INNER JOIN mpa ON f.mpa_id = mpa.mpa_id;";
        List<Film> films = jdbcTemplate.query(sql, filmRowMapper);
        log.info("FilmDao getAll: Получены все фильмы");
        return films;
    }

    @Override
    public Film create(Film film) {
        log.info("FilmDao create: Запрос на создание фильма {}", film);
        String sqlQuery = "INSERT INTO films (mpa_id, name, description, release_date, duration, rate) " +
                "VALUES (?, ?, ?, ?, ?, ?);";
        LocalDate releaseDate = film.getReleaseDate();
        Instant instant = releaseDate.atStartOfDay(ZoneId.systemDefault()).toInstant();
        long timeInMillis = instant.toEpochMilli();
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"film_id"});
            stmt.setLong(1, film.getMpa().getId());
            stmt.setString(2, film.getName());
            stmt.setString(3, film.getDescription());
            stmt.setDate(4, new Date(timeInMillis));
            stmt.setLong(5, film.getDuration());
            stmt.setDouble(6, film.getRate());
            return stmt;
        }, keyHolder);
        long filmId = keyHolder.getKey().longValue();
        if (film.getGenres() != null) {
            saveGenres(film, filmId);
        }
        log.info("FilmDao create: Создан фильм с id {}", filmId);
        film.setId(filmId);
        return film;
    }

    @Override
    public Film update(final Film film) {
        log.info("FilmDao update: Запрос на обновление фильма {}", film);
        long filmId = film.getId();
        String updateFilmSql = "UPDATE films " +
                "SET name = ?, description = ?, release_date = ?, duration = ?, mpa_id = ?, rate = ? " +
                "WHERE film_id = ?;";
        if (film.getGenres() != null) {
            clearGenres(filmId);
            saveGenres(film, filmId);
        }
        int amount = jdbcTemplate.update(updateFilmSql, film.getName(), film.getDescription(), film.getReleaseDate(),
                film.getDuration(), film.getMpa().getId(), film.getRate(), filmId);
        if (amount == 0) {
            log.error("FilmDao update: Не существует фильма с id: {}", filmId);
            throw new DAOException(String.format("FilmDao update: Не существует пользователя с id: %d", filmId));
        }
        log.info("FilmDao update: Обновление фильма с id {}", filmId);
        return film;
    }

    private void clearGenres(long filmId) {
        String deleteOldGenresSql = "DELETE FROM film_genre WHERE film_id = ?;";
        jdbcTemplate.update(deleteOldGenresSql, filmId);//Если id не существует, то БД не изменится
    }

    private void saveGenres(Film film, long filmId) {
        List<Long> genresId = film.getGenres().stream().map(Genre::getId).collect(Collectors.toList());
        String addNewGenresSql = "INSERT INTO film_genre (film_id, genre_id) VALUES (?,?);";
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
}
