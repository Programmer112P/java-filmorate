package ru.yandex.practicum.filmorate.dao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.dao.impl.FriendsDaoImpl;
import ru.yandex.practicum.filmorate.dao.impl.UserDaoImpl;
import ru.yandex.practicum.filmorate.dao.impl.mapper.UserRowMapper;
import ru.yandex.practicum.filmorate.entity.User;
import ru.yandex.practicum.filmorate.exception.DAOException;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJdbcTest
@Sql({"/testschema.sql", "/testdata.sql"})
class FriendsDaoTest {


    private final JdbcTemplate jdbcTemplate;
    private final UserDao userDao;
    private final FriendsDao friendsDao;
    private final UserRowMapper userRowMapper;
    private User user1;
    private User user2;

    @Autowired
    public FriendsDaoTest(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        userRowMapper = new UserRowMapper();
        userDao = new UserDaoImpl(jdbcTemplate, userRowMapper);
        friendsDao = new FriendsDaoImpl(jdbcTemplate, userRowMapper, userDao);
        user1 = User.builder().name("TestUser1 Name").email("test1@email").login("TestUser1 Login")
                .birthday(LocalDate.of(1999, 1, 1)).build();
        user2 = User.builder().name("TestUser2 Name").email("test2@email").login("TestUser2 Login")
                .birthday(LocalDate.of(1999, 2, 2)).build();
    }

    @Test
    void getCommonFriends_shouldReturnCommonFriends() {
        userDao.create(user1);
        userDao.create(user2);
        friendsDao.addFriend(1, 2);
        friendsDao.addFriend(3, 4);
        friendsDao.addFriend(1, 4);
        List<User> friends = friendsDao.getCommonFriends(1, 3);
        assertThat(friends.size()).isEqualTo(1);
        friends = friendsDao.getCommonFriends(1, 2);
        assertThat(friends.size()).isEqualTo(0);
    }

    @Test
    void getCommonFriends_shouldThrowExceptionWhenIncorrectId() {
        assertThrows(DAOException.class, () -> friendsDao.getCommonFriends(1, 222));
        assertThrows(DAOException.class, () -> friendsDao.getCommonFriends(222, 1));
    }

    @Test
    void getFriends_shouldReturnFriendsOfUser() {
        userDao.create(user1);
        userDao.create(user2);
        friendsDao.addFriend(1, 2);
        friendsDao.addFriend(1, 3);
        friendsDao.addFriend(1, 4);
        List<User> friends = friendsDao.getFriends(1);
        assertThat(friends.size()).isEqualTo(3);
    }

    @Test
    void getFriends_shouldThrowExceptionWhenIncorrectId() {
        assertThrows(DAOException.class, () -> friendsDao.getFriends(222));
    }

    @Test
    void removeFriend_shouldRemoveOneFriend() {
        userDao.create(user1);
        userDao.create(user2);
        friendsDao.addFriend(1, 2);
        friendsDao.addFriend(1, 3);
        friendsDao.addFriend(1, 4);
        List<User> friends = friendsDao.getFriends(1);
        assertThat(friends.size()).isEqualTo(3);
        friendsDao.removeFriend(1, 2);
        friends = friendsDao.getFriends(1);
        assertThat(friends.size()).isEqualTo(2);
        friendsDao.removeFriend(1, 3);
        friends = friendsDao.getFriends(1);
        assertThat(friends.size()).isEqualTo(1);
    }

    @Test
    void removeFriend_shouldThrowExceptionWhenIncorrectId() {
        userDao.create(user1);
        userDao.create(user2);
        friendsDao.addFriend(1, 2);
        assertThrows(DAOException.class, () -> friendsDao.removeFriend(222, 1));
        assertThrows(DAOException.class, () -> friendsDao.removeFriend(1, 222));
        assertThrows(DAOException.class, () -> friendsDao.removeFriend(1, 3));
    }

    @Test
    void addFriend_shouldAddOneFriend() {
        userDao.create(user1);
        userDao.create(user2);
        friendsDao.addFriend(1, 2);
        friendsDao.addFriend(3, 4);
        friendsDao.addFriend(1, 4);
        List<User> friends1 = friendsDao.getFriends(1);
        assertThat(friends1.size()).isEqualTo(2);
        List<User> friends3 = friendsDao.getFriends(3);
        assertThat(friends3.size()).isEqualTo(1);
        List<User> friends2 = friendsDao.getFriends(2);
        assertThat(friends2.size()).isEqualTo(0);
    }

    @Test
    void addFriend_shouldThrowExceptionWhenIncorrectId() {
        assertThrows(DAOException.class, () -> friendsDao.addFriend(1, -1));
        assertThrows(DAOException.class, () -> friendsDao.addFriend(-1, 1));
    }
}