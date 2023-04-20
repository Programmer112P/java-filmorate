package ru.yandex.practicum.filmorate.dao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.dao.impl.GenreDaoImpl;
import ru.yandex.practicum.filmorate.entity.Genre;
import ru.yandex.practicum.filmorate.exception.DAOException;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@DataJdbcTest
@Sql({"/testschema.sql", "/testdata.sql"})
class GenreDaoTest {

    private final JdbcTemplate jdbcTemplate;
    private final GenreDao genreDao;

    @Autowired
    public GenreDaoTest(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        genreDao = new GenreDaoImpl(jdbcTemplate);
    }

    @Test
    void getGenresOfFilm_shouldReturnAllGenres() {
        List<Genre> genres = genreDao.getGenresOfFilm(1);
        assertThat(genres.size()).isEqualTo(1);
        assertThat(genres.get(0).getId()).isEqualTo(1);
        assertThat(genres.get(0).getName()).isEqualTo("Комедия");
    }

    @Test
    void getAll_shouldReturnAllGenres() {
        List<Genre> genres = genreDao.getAll();
        assertThat(genres.size()).isEqualTo(6);
    }

    @Test
    void getById_shouldReturnGenre() {
        Genre genre = genreDao.getById(1);
        assertThat(genre.getId()).isEqualTo(1);
        assertThat(genre.getName()).isEqualTo("Комедия");
        Genre genre1 = genreDao.getById(5);
        assertThat(genre1.getId()).isEqualTo(5);
        assertThat(genre1.getName()).isEqualTo("Документальный");
    }

    @Test
    void getById_shouldThrowExceptionWhenIncorrectId() {
        assertThrows(DAOException.class, () -> genreDao.getById(222));
    }
}