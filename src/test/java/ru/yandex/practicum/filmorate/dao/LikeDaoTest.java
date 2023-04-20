package ru.yandex.practicum.filmorate.dao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.dao.impl.FilmDaoImpl;
import ru.yandex.practicum.filmorate.dao.impl.GenreDaoImpl;
import ru.yandex.practicum.filmorate.dao.impl.LikeDaoImplement;
import ru.yandex.practicum.filmorate.dao.impl.mapper.FilmRowMapper;
import ru.yandex.practicum.filmorate.entity.Film;
import ru.yandex.practicum.filmorate.entity.Genre;
import ru.yandex.practicum.filmorate.entity.Mpa;
import ru.yandex.practicum.filmorate.entity.User;
import ru.yandex.practicum.filmorate.exception.DAOException;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJdbcTest
@Sql({"/testschema.sql", "/testdata.sql"})
class LikeDaoTest {

    private final JdbcTemplate jdbcTemplate;
    private final FilmRowMapper filmRowMapper;
    private final GenreDao genreDao;
    private final FilmDao filmDao;
    private final LikeDao likeDao;
    private Mpa mpa;
    private Genre genreOne;
    private Genre genreTwo;
    private Film testFilm;
    private User user;

    @Autowired
    public LikeDaoTest(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        genreDao = new GenreDaoImpl(jdbcTemplate);
        filmRowMapper = new FilmRowMapper(genreDao);
        likeDao = new LikeDaoImplement(jdbcTemplate, filmRowMapper);
        filmDao = new FilmDaoImpl(jdbcTemplate, filmRowMapper);
        mpa = Mpa.builder().id(1L).build();
        genreOne = Genre.builder().id(1L).name("Комедия").build();
        genreTwo = Genre.builder().id(2L).name("Драма").build();
        user = User.builder().name("Test User Name").email("test@email").login("Test User Login")
                .birthday(LocalDate.of(1999, 1, 1)).build();
        testFilm = Film.builder().mpa(mpa).releaseDate(LocalDate.of(1999, 1, 1))
                .description("Test Film Description").duration(120L).name("Test Film Name")
                .genres(Set.of(genreOne, genreTwo)).build();
    }

    @Test
    void removeLike_shouldRemoveLikeIfFilmAndUserExist() {
        filmDao.create(testFilm);
        likeDao.addLike(2, 1);
        likeDao.addLike(2, 2);
        List<Film> mostPopular = likeDao.getMostPopular(1);
        assertThat(mostPopular.get(0).getId()).isEqualTo(2);
        likeDao.removeLike(2, 1);
        likeDao.removeLike(2, 2);
        mostPopular = likeDao.getMostPopular(1);
        assertThat(mostPopular.get(0).getId()).isEqualTo(1);
    }

    @Test
    void removeLike_shouldThrowExceptionIfUserOrFilmNotExist() {
        assertThrows(DAOException.class, () -> likeDao.removeLike(44, 1));
        assertThrows(DAOException.class, () -> likeDao.removeLike(1, 44));
    }

    @Test
    void addLike_shouldAddLikeIfUserAndFilmExist() {
        filmDao.create(testFilm);
        System.out.println(filmDao.getAll());
        likeDao.addLike(2, 1);
        likeDao.addLike(2, 2);
        List<Film> mostPopular = likeDao.getMostPopular(1);
        assertThat(mostPopular.get(0).getId()).isEqualTo(2);
    }

    @Test
    void addLike_shouldThrowExceptionIfUserOrFilmNotExist() {
        assertThrows(DAOException.class, () -> likeDao.addLike(44, 1));
        assertThrows(DAOException.class, () -> likeDao.addLike(1, 44));
    }

    @Test
    void getMostPopulars() {
        List<Film> films = likeDao.getMostPopular(10);
        assertThat(films.size()).isEqualTo(1);
        filmDao.create(testFilm);
        likeDao.addLike(2, 1);
        likeDao.addLike(2, 2);
        List<Film> mostPopular = likeDao.getMostPopular(10);
        assertThat(mostPopular.get(0).getId()).isEqualTo(2);
    }
}