package ru.yandex.practicum.filmorate.dao.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.FriendsDao;
import ru.yandex.practicum.filmorate.dao.UserDao;
import ru.yandex.practicum.filmorate.dao.impl.mapper.UserRowMapper;
import ru.yandex.practicum.filmorate.entity.User;
import ru.yandex.practicum.filmorate.exception.DAOException;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Repository
public class FriendsDaoImpl implements FriendsDao {

    private final JdbcTemplate jdbcTemplate;
    private final UserRowMapper userRowMapper;
    private final UserDao userDao;

    @Autowired
    public FriendsDaoImpl(JdbcTemplate jdbcTemplate, UserRowMapper userRowMapper, @Qualifier("userDaoImpl") UserDao userDao) {
        this.jdbcTemplate = jdbcTemplate;
        this.userRowMapper = userRowMapper;
        this.userDao = userDao;
    }

    /*
    * Если делать такой запрос
    * "select * from USERS u, FRIENDS f, FRIENDS o " +
				"where u.USER_ID = f.FRIEND_ID AND u.USER_ID = o.FRIEND_ID AND f.USER_ID = ? AND o.USER_ID = ?";
	* То не будет ошибки при несуществующем пользователе
	* Внутрь WHERE можно подставить любое значение, там нет проверки на целостность
	* Поэтому сделал через 2 запроса, так точно будут только существующие
    * */
    @Override
    public List<User> getCommonFriends(long id, long otherId) {
        log.info("FriendsDao getCommonFriends: Запрос на получение общих друзей id1: {}, id2: {}", id, otherId);
        List<User> user1Friends = getFriends(id);
        List<User> user2Friends = getFriends(otherId);
        user1Friends.retainAll(user2Friends);
        log.info("FriendsDao getCommonFriends: Получен список общих друзей id1: {}, id2: {}", id, otherId);
        return new ArrayList<>(user1Friends);
    }

    @Override
    public void removeFriend(long id, long friendId) {
        log.info("UserDao removeFriend: Запрос на удаление друга от id {} к {}", id, friendId);
        String sql = "DELETE FROM friends WHERE USER_FROM  = ? AND USER_TO = ?;";
        int amount = jdbcTemplate.update(sql, id, friendId);//Вместо 1 должно быть id статуса request, но пока ничего нет
        if (amount == 0) {
            log.error("FriendsDao removeFriend: Не существует пользователя с id {} или с id {}, или они не были друзьями", id, friendId);
            throw new DAOException(String.format("Не существует пользователя с id %d или с id %d, " +
                    "или они не были друзьями", id, friendId));
        }
        log.info("FriendsDao removeFriend: Удален друг от id {} к {}", id, friendId);
    }

    @Override
    public void addFriend(long id, long friendId) {
        log.info("UserDao addFriend: Запрос на добавление друга от id {} к id{}", id, friendId);
        String sql = "INSERT INTO FRIENDS (USER_FROM, USER_TO) values (?, ?)";
        try {
            jdbcTemplate.update(sql, id, friendId);
            log.info("FriendsDao addFriend: Добавлен друг от id {} к id{}", id, friendId);
        } catch (DataAccessException e) {
            log.error("FriendsDao addFriend: Одного из пользователей не существует id {} или id {}", id, friendId);
            throw new DAOException(e);
        }
    }

    @Override
    public List<User> getFriends(long id) {
        log.info("FriendsDao getFriends: Запрос на получение друзей пользователя с id {}", id);
        String sql = "SELECT * FROM users " +
                "WHERE user_id IN (SELECT DISTINCT USER_TO FROM FRIENDS WHERE USER_FROM = ?);";
        userDao.getById(id);//Проверка на существование
        List<User> friends = jdbcTemplate.query(sql, userRowMapper, id);
        log.info("FriendsDao getFriends: Получен список друзей пользователя с id {}", id);
        return friends;
    }
}
