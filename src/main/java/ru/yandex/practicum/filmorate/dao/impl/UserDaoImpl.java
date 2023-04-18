package ru.yandex.practicum.filmorate.dao.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.UserDao;
import ru.yandex.practicum.filmorate.dao.insert.UserSimpleJdbcInsert;
import ru.yandex.practicum.filmorate.entity.User;
import ru.yandex.practicum.filmorate.exception.DAOException;
import ru.yandex.practicum.filmorate.exception.DbConnectionException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
@Slf4j
public class UserDaoImpl implements UserDao {
    private final JdbcTemplate jdbcTemplate;
    private final UserSimpleJdbcInsert userSimpleJdbcInsert;

    @Autowired
    public UserDaoImpl(JdbcTemplate jdbcTemplate, UserSimpleJdbcInsert userSimpleJdbcInsert) {
        this.jdbcTemplate = jdbcTemplate;
        this.userSimpleJdbcInsert = userSimpleJdbcInsert;
    }

    //Сделал через 2 обращения к спискам друзей, иначе очень много вложенных запросов получалось
    @Override
    public List<User> getCommonFriends(long id, long otherId) {
        log.info("UserDao getCommonFriends: Запрос на получение общих друзей id1: {}, id2: {}", id, otherId);
        List<User> user1Friends = getFriends(id);
        List<User> user2Friends = getFriends(otherId);
        user1Friends.retainAll(user2Friends);
        log.info("UserDao getCommonFriends: Получен список общих друзей id1: {}, id2: {}", id, otherId);
        return new ArrayList<>(user1Friends);
    }

    @Override
    public void removeFriend(long id, long friendId) {
        log.info("UserDao removeFriend: Запрос на удаление друга от id {} к {}", id, friendId);
        String sql = "DELETE FROM FRIENDSHIP WHERE USER_FROM  = ? AND USER_TO = ?;";
        try {
            int amount = jdbcTemplate.update(sql, id, friendId);//Вместо 1 должно быть id статуса request, но пока ничего нет
            if (amount == 0) {
                log.error("UserDao removeFriend: Не существует пользователя с id {} или с id {}, или они не были друзьями", id, friendId);
                throw new DAOException(String.format("UserDao update: Не существует пользователя с id %d или с id %d, " +
                        "или они не были друзьями", id, friendId));
            }
            log.info("UserDao removeFriend: Удален друг от id {} к {}", id, friendId);
        } catch (DataAccessException e) {
            log.error("UserDao removeFriend: Ошибка при выполнении запроса {}", sql);
            throw new DbConnectionException(e);
        }
    }

    @Override
    public void addFriend(long id, long friendId) {
        log.info("UserDao addFriend: Запрос на добавление друга от id {} к id{}", id, friendId);
        String sql = "MERGE INTO FRIENDSHIP AS target " +
                "USING (VALUES (?, ?, ?)) AS source (USER_FROM_S, USER_TO_S, FRIENDSHIP_STATUS_ID_S) " +
                "ON (target.USER_FROM = source.USER_FROM_S AND target.USER_TO = source.USER_TO_S) " +
                "WHEN NOT MATCHED THEN " +
                "INSERT (USER_FROM, USER_TO, FRIENDSHIP_STATUS_ID) " +
                "VALUES (source.USER_FROM_S, source.USER_TO_S, source.FRIENDSHIP_STATUS_ID_S);";
        try {
            jdbcTemplate.update(sql, id, friendId, 1);//Вместо 1 должно быть id статуса request, но пока ничего нет
            log.info("UserDao addFriend: Добавлен друг от id {} к id{}", id, friendId);
        } catch (DataAccessException e) {
            log.error("UserDao addFriend: Одного из пользователей не существует id {} или id {}", id, friendId);
            throw new DAOException(e);
        }
    }

    @Override
    public List<User> getAll() {
        log.info("UserDao getAll: Запрос на получение всех пользователей getAll");
        String sql = "SELECT * FROM users;";
        try {
            List<User> allUsers = jdbcTemplate.query(sql, this::mapRowToUser);
            log.info("UserDao getAll: Отправлены все пользователи getAll");
            return allUsers;
        } catch (DataAccessException e) {
            log.error("UserDao getAll: Ошибка при выполнении запроса {}", sql);
            throw new DbConnectionException(e);
        }
    }

    @Override
    public User getById(long id) {
        log.info("UserDao getById: запрос на получение пользователя c id {}", id);
        String sql = "SELECT * FROM users WHERE user_id = ?;";
        try {
            User user = jdbcTemplate.queryForObject(sql, this::mapRowToUser, id);
            log.info("UserDao getById: отправлен пользователь c id {}", id);
            return user;
        } catch (IncorrectResultSizeDataAccessException e) {
            log.error("UserDao getById: Пользователя с id {} не существует", id);
            throw new DAOException(e);
        } catch (DataAccessException e) {
            log.error("UserDao getById: Ошибка при выполнении запроса {}", sql);
            throw new DbConnectionException(e);
        }
    }

    //Потенциально могут быть статусы дружбы accepted, declined, black list
    //Пока что я оставил такую реализацию добавления дружбы, которая работает на тестах postman, без статуса
    @Override
    public List<User> getFriends(long id) {
//        Это я написал свою реализацию, но она не работает на тестах, поэтому пришлось все поменять
//        String sql = "SELECT DISTINCT u.user_id " +
//                "FROM users AS u " +
//                "INNER JOIN friendship AS f ON u.user_id = f.user_from OR u.user_id = f.user_to " +
//                "INNER JOIN friendship_status AS fs ON f.friendship_status_id = fs.friendship_status_id " +
//                "WHERE u.user_id != ? AND (f.user_to = ? OR f.user_from = ?) " +
//                "AND fs.friendship_status_name = 'accepted';";

        //Как проверить на то, что пользователя не существует не через получение его по id, а внутри запроса?
        log.info("UserDao getFriends: Запрос на получение друзей пользователя с id {}", id);
        String sql = "SELECT * FROM users " +
                "WHERE user_id IN (SELECT DISTINCT USER_TO FROM FRIENDSHIP WHERE USER_FROM = ?);";
        try {
            getById(id);//Проверка на существование
            List<User> friends = jdbcTemplate.query(sql, this::mapRowToUser, id);
            log.info("UserDao getFriends: Получен список друзей пользователя с id {}", id);
            return friends;
        } catch (DataAccessException e) {
            log.error("UserDao getFriends: ошибка при выполнении запроса: {}", sql);
            throw new DbConnectionException(e);
        }
    }

    private User mapRowToUser(ResultSet rs, int rowNum) throws SQLException {
        return User.builder()
                .id(rs.getLong("user_id"))
                .email(rs.getString("user_email"))
                .login(rs.getString("user_login"))
                .name(rs.getString("user_name"))
                .birthday(rs.getDate("user_birthday").toLocalDate())
                .build();
    }

    @Override
    public User create(User user) {
        try {
            log.info("UserDao create: Запрос на создание пользователя: {}", user);
            long id = userSimpleJdbcInsert.executeAndReturnKey(user.toMap()).longValue();
            user.setId(id);
            log.info("UserDao create: Создан пользователь: {}", user);
            return user;
        } catch (DataAccessException e) {
            log.error("UserDao create: Ошибка при создании пользователя {}", user);
            throw new DbConnectionException(e);
        }
    }

    @Override
    public User update(User user) {
        log.info("UserDao update: Запрос на обновление пользователя с id: {}", user.getId());
        String sql = "UPDATE users SET user_email = ?, user_login = ?, user_name = ?, user_birthday = ? WHERE user_id = ?;";
        try {
            int amount = jdbcTemplate.update(sql, user.getEmail(), user.getLogin(), user.getName(), user.getBirthday(), user.getId());
            if (amount == 0) {
                log.error("UserDao update: Не существует пользователя с id: {}", user.getId());
                throw new DAOException(String.format("UserDao update: Не существует пользователя с id: %d", user.getId()));
            }
            log.info("UserDao update: Обновлен пользователь: {}", user);
            return user;
        } catch (DataAccessException e) {
            log.error("UserDao update: Ошибка при выполнении запроса {}", sql);
            throw new DbConnectionException(e);
        }
    }
}
