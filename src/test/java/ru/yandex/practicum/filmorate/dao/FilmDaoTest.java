package ru.yandex.practicum.filmorate.dao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.dao.impl.FilmDaoImpl;
import ru.yandex.practicum.filmorate.dao.impl.GenreDaoImpl;
import ru.yandex.practicum.filmorate.dao.insert.FilmSimpleJdbcInsert;
import ru.yandex.practicum.filmorate.entity.Film;
import ru.yandex.practicum.filmorate.entity.Genre;
import ru.yandex.practicum.filmorate.entity.Mpa;
import ru.yandex.practicum.filmorate.entity.User;
import ru.yandex.practicum.filmorate.exception.DAOException;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@DataJdbcTest
@Sql({"/testschema.sql", "/testdata.sql"})
class FilmDaoTest {

    private final JdbcTemplate jdbcTemplate;
    private final FilmDao filmDao;
    private final GenreDao genreDao;
    private final FilmSimpleJdbcInsert filmSimpleJdbcInsert;
    private Mpa mpa;
    private Genre genreOne;
    private Genre genreTwo;
    private Film testFilm;
    private User user;

    @Autowired
    public FilmDaoTest(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        filmSimpleJdbcInsert = new FilmSimpleJdbcInsert(jdbcTemplate);
        genreDao = new GenreDaoImpl(jdbcTemplate);
        filmDao = new FilmDaoImpl(genreDao, jdbcTemplate, filmSimpleJdbcInsert);
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
        filmDao.addLike(2, 1);
        filmDao.addLike(2, 2);
        List<Film> mostPopular = filmDao.getMostPopulars(1);
        assertThat(mostPopular.get(0).getId()).isEqualTo(2);
        filmDao.removeLike(2, 1);
        filmDao.removeLike(2, 2);
        mostPopular = filmDao.getMostPopulars(1);
        assertThat(mostPopular.get(0).getId()).isEqualTo(1);
    }

    @Test
    void removeLike_shouldThrowExceptionIfUserOrFilmNotExist() {
        assertThrows(DAOException.class, () -> filmDao.removeLike(44, 1));
        assertThrows(DAOException.class, () -> filmDao.removeLike(1, 44));
    }

    @Test
    void addLike_shouldAddLikeIfUserAndFilmExist() {
        filmDao.create(testFilm);
        System.out.println(filmDao.getAll());
        filmDao.addLike(2, 1);
        filmDao.addLike(2, 2);
        List<Film> mostPopular = filmDao.getMostPopulars(1);
        assertThat(mostPopular.get(0).getId()).isEqualTo(2);
    }

    @Test
    void addLike_shouldThrowExceptionIfUserOrFilmNotExist() {
        assertThrows(DAOException.class, () -> filmDao.addLike(44, 1));
        assertThrows(DAOException.class, () -> filmDao.addLike(1, 44));
    }

    @Test
    void getMostPopulars() {
        List<Film> films = filmDao.getMostPopulars(10);
        assertThat(films.size()).isEqualTo(1);
        filmDao.create(testFilm);
        filmDao.addLike(2, 1);
        filmDao.addLike(2, 2);
        List<Film> mostPopular = filmDao.getMostPopulars(10);
        assertThat(mostPopular.get(0).getId()).isEqualTo(2);
    }

    @Test
    void getById_shouldReturnFilmById() {
        Film returned = filmDao.getById(1);
        assertThat(returned.getId()).isEqualTo(1);
        assertThat(returned.getName()).isEqualTo("Name");
        assertThat(returned.getDescription()).isEqualTo("Description");
        assertThat(returned.getDuration()).isEqualTo(120);
        assertThat(returned.getGenres()).isEqualTo(Set.of(genreOne));
        assertThat(returned.getReleaseDate()).isEqualTo(LocalDate.of(2000, 10, 10));
    }

    @Test
    void getById_shouldThrowExceptionWhenNotFound() {
        assertThrows(DAOException.class, () -> filmDao.getById(-1));
    }

    @Test
    void getAll_shouldReturnAll() {
        List<Film> all = filmDao.getAll();
        assertThat(all.size()).isEqualTo(1);
        filmDao.create(testFilm);
        all = filmDao.getAll();
        assertThat(all.size()).isEqualTo(2);
        assertThat(all.get(1)).isEqualTo(testFilm);
    }

    @Test
    void create_shouldAddOneWithNewId() {
        Film created = filmDao.create(testFilm);
        assertThat(created.getId()).isEqualTo(2);
        assertThat(filmDao.getById(2)).isEqualTo(created);
    }

    @Test
    void update_shouldUpdateWithCorrectId() {
        testFilm.setId(1L);
        filmDao.update(testFilm);
        Film updated = filmDao.getById(1);
        assertThat(updated.getId()).isEqualTo(1);
        assertThat(updated.getName()).isEqualTo(testFilm.getName());
        assertThat(updated.getDescription()).isEqualTo(testFilm.getDescription());
        assertThat(updated.getDuration()).isEqualTo(testFilm.getDuration());
        assertThat(updated.getReleaseDate()).isEqualTo(testFilm.getReleaseDate());
        assertThat(updated.getMpa()).isEqualTo(testFilm.getMpa());
        assertThat(updated.getGenres()).isEqualTo(testFilm.getGenres());
    }

    @Test
    void update_shouldThrowExceptionWithIncorrectId() {
        testFilm.setId(123L);
        assertThrows(DAOException.class, () -> filmDao.update(testFilm));
        Film filmWithoutGenres = Film.builder().mpa(mpa).releaseDate(LocalDate.of(1999, 1, 1))
                .description("Test Film Description").duration(120L).name("Test Film Name").build();
        filmWithoutGenres.setId(1233L);
        assertThrows(DAOException.class, () -> filmDao.update(filmWithoutGenres));
    }
}