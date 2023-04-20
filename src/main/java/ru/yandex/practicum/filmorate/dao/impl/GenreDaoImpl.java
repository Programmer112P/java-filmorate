package ru.yandex.practicum.filmorate.dao.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.GenreDao;
import ru.yandex.practicum.filmorate.entity.Genre;
import ru.yandex.practicum.filmorate.exception.DAOException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Slf4j
@Repository
public class GenreDaoImpl implements GenreDao {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public GenreDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Genre> getGenresOfFilm(long filmId) {
        log.info("GenreDao getGenresOfFilm: Запрос на получение жанров фильма с id {}", filmId);
        String sql = "SELECT * FROM genre AS g " +
                "INNER JOIN film_genre AS fg ON g.genre_id = fg.genre_id " +
                "WHERE fg.film_id = ?;";
        List<Genre> genresOfFilm = jdbcTemplate.query(sql, this::mapRowToGenre, filmId);
        log.info("GenreDao getGenresOfFilm: Получены жанры фильма с id {}", filmId);
        return genresOfFilm;
    }

    @Override
    public List<Genre> getAll() {
        log.info("GenreDao getAll: Запрос на получение всех жанров");
        String sql = "SELECT * FROM genre;";
        List<Genre> genres = jdbcTemplate.query(sql, this::mapRowToGenre);
        log.info("GenreDao getAll: Получены все жанры");
        return genres;
    }

    @Override
    public Genre getById(long id) {
        log.info("GenreDao getById: Запрос на получение жанра с id {}", id);
        String sql = "SELECT * FROM genre WHERE genre_id = ?;";
        try {
            Genre genre = jdbcTemplate.queryForObject(sql, this::mapRowToGenre, id);
            log.info("GenreDao getById: Получен жанр с id {}", id);
            return genre;
        } catch (IncorrectResultSizeDataAccessException e) {
            log.error("GenreDao getById: Не существует жанра с id {}", id);
            throw new DAOException(e.getMessage(), e);
        }
    }

    private Genre mapRowToGenre(ResultSet rs, int rowNum) throws SQLException {
        return Genre.builder()
                .id(rs.getLong("genre_id"))
                .name(rs.getString("name"))
                .build();
    }
}
