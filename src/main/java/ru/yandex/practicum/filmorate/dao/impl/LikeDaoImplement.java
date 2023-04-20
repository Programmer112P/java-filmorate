package ru.yandex.practicum.filmorate.dao.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.LikeDao;
import ru.yandex.practicum.filmorate.dao.impl.mapper.FilmRowMapper;
import ru.yandex.practicum.filmorate.entity.Film;
import ru.yandex.practicum.filmorate.exception.DAOException;

import java.util.List;

@Slf4j
@Repository
public class LikeDaoImplement implements LikeDao {

    private final JdbcTemplate jdbcTemplate;
    private final FilmRowMapper filmRowMapper;

    @Autowired
    public LikeDaoImplement(JdbcTemplate jdbcTemplate, FilmRowMapper filmRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.filmRowMapper = filmRowMapper;
    }

    @Override
    public void removeLike(final long filmId, final long userId) {
        log.info("LikeDao removeLike: Запрос на удаление лайка от пользователя с id {} фильму с id {}", userId, filmId);
        String sql = "DELETE FROM film_user_like WHERE film_id = ? AND user_id = ?";
        int amount = jdbcTemplate.update(sql, filmId, userId);
        //Удаление должно быть с проверкой, такие тесты в postman
        //Да и в принципе, если мне приходит запрос на удаление друзей, которых нет - это ошибка в логике программы
        if (amount == 0) {
            log.error("LikeDao removeLike: Не существует пользователя с id {} или фильма с id {}. " +
                    "Либо не было такого лайка", userId, filmId);
            String message = String.format("Не существует пользователя с id %d или фильма с id %d. " +
                    "Либо не было такого лайка", userId, filmId);
            throw new DAOException(message);
        }
        log.info("LikeDao removeLike: Удален лайк от пользователя с id {} фильму с id {}", userId, filmId);
    }

    @Override
    public void addLike(final long filmId, final long userId) {
        log.info("FilmDao addLike: Запрос на добавление лайка от пользователя с id {} фильму с id {}", userId, filmId);
        String sql = "INSERT INTO film_user_like (user_id, film_id) VALUES (?,?);";
        try {
            jdbcTemplate.update(sql, userId, filmId);
            log.info("LikeDao addLike: Добавлен лайк от пользователя с id {} фильму с id {}", userId, filmId);
        } catch (DataAccessException e) {
            log.error("LikeDao addLike: Ошибка при выполнении запроса {}. " +
                    "Не существует пользователя с id {} или фильма с id {}", sql, userId, filmId);
            throw new DAOException(e.getMessage(), e);
        }
    }

    @Override
    public List<Film> getMostPopular(final int amount) {
        log.info("LikeDao getMostPopulars: Запрос на получение {} самых популярных фильмов", amount);
        String sql = "SELECT f.film_id, f.mpa_id, mpa.name AS mpa_name, f.name, f.description, f.rate, " +
                "f.release_date, f.duration, COUNT(fl.user_id) AS likes_amount " +
                "FROM films AS f " +
                "LEFT JOIN film_user_like AS fl ON f.film_id = fl.film_id " +
                "LEFT JOIN mpa ON f.mpa_id = mpa.mpa_id " +
                "GROUP BY f.film_id ORDER BY likes_amount DESC " +
                "LIMIT ?;";
        List<Film> popularFilms = jdbcTemplate.query(sql, filmRowMapper, amount);
        log.info("LikeDao getMostPopulars: Получено {} самых популярных фильмов", amount);
        return popularFilms;
    }
}
