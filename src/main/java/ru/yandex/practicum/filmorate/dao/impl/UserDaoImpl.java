package ru.yandex.practicum.filmorate.dao.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.UserDao;
import ru.yandex.practicum.filmorate.dao.impl.mapper.UserRowMapper;
import ru.yandex.practicum.filmorate.entity.User;
import ru.yandex.practicum.filmorate.exception.DAOException;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

@Repository
@Slf4j
public class UserDaoImpl implements UserDao {
    private final JdbcTemplate jdbcTemplate;
    private final UserRowMapper userRowMapper;

    @Autowired
    public UserDaoImpl(JdbcTemplate jdbcTemplate, UserRowMapper userRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.userRowMapper = userRowMapper;
    }

    @Override
    public List<User> getAll() {
        log.info("UserDao getAll: Запрос на получение всех пользователей getAll");
        String sql = "SELECT * FROM users;";
        List<User> allUsers = jdbcTemplate.query(sql, userRowMapper);
        log.info("UserDao getAll: Отправлены все пользователи getAll");
        return allUsers;
    }

    @Override
    public User getById(long id) {
        log.info("UserDao getById: запрос на получение пользователя c id {}", id);
        String sql = "SELECT * FROM users WHERE user_id = ?;";
        try {
            User user = jdbcTemplate.queryForObject(sql, userRowMapper, id);
            log.info("UserDao getById: отправлен пользователь c id {}", id);
            return user;
        } catch (IncorrectResultSizeDataAccessException e) {
            log.error("UserDao getById: Пользователя с id {} не существует", id);
            throw new DAOException(e);
        }
    }

    @Override
    public User create(User user) {
        log.info("UserDao create: Запрос на создание пользователя: {}", user);
        LocalDate birthday = user.getBirthday();
        Instant instant = birthday.atStartOfDay(ZoneId.systemDefault()).toInstant();
        long timeInMillis = instant.toEpochMilli();
        String sqlQuery = "INSERT INTO users (EMAIL, LOGIN, NAME, BIRTHDAY) " +
                "VALUES (?, ?, ?, ?);";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"user_id"});
            stmt.setString(1, user.getEmail());
            stmt.setString(2, user.getLogin());
            stmt.setString(3, user.getName());
            stmt.setDate(4, new Date(timeInMillis));
            return stmt;
        }, keyHolder);
        long userId = keyHolder.getKey().longValue();
        user.setId(userId);
        log.info("UserDao create: Создан пользователь: {}", user);
        return user;
    }

    @Override
    public User update(User user) {
        log.info("UserDao update: Запрос на обновление пользователя с id: {}", user.getId());
        String sql = "UPDATE users SET email = ?, login = ?, name = ?, birthday = ? WHERE user_id = ?;";
        int amount = jdbcTemplate.update(sql, user.getEmail(), user.getLogin(), user.getName(), user.getBirthday(), user.getId());
        if (amount == 0) {
            log.error("UserDao update: Не существует пользователя с id: {}", user.getId());
            throw new DAOException(String.format("UserDao update: Не существует пользователя с id: %d", user.getId()));
        }
        log.info("UserDao update: Обновлен пользователь: {}", user);
        return user;
    }
}
