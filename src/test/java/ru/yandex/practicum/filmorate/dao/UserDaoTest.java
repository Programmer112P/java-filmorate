package ru.yandex.practicum.filmorate.dao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.dao.impl.UserDaoImpl;
import ru.yandex.practicum.filmorate.dao.insert.UserSimpleJdbcInsert;
import ru.yandex.practicum.filmorate.entity.User;
import ru.yandex.practicum.filmorate.exception.DAOException;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;

@DataJdbcTest
@Sql({"/testschema.sql", "/testdata.sql"})
class UserDaoTest {

    private final JdbcTemplate jdbcTemplate;
    private final UserDao userDao;
    private final UserSimpleJdbcInsert userSimpleJdbcInsert;
    private User user1;
    private User user2;

    @Autowired
    public UserDaoTest(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        userSimpleJdbcInsert = new UserSimpleJdbcInsert(jdbcTemplate);
        userDao = new UserDaoImpl(jdbcTemplate, userSimpleJdbcInsert);
        user1 = User.builder().name("TestUser1 Name").email("test1@email").login("TestUser1 Login")
                .birthday(LocalDate.of(1999, 1, 1)).build();
        user2 = User.builder().name("TestUser2 Name").email("test2@email").login("TestUser2 Login")
                .birthday(LocalDate.of(1999, 2, 2)).build();
    }


    @Test
    void getCommonFriends_shouldReturnCommonFriends() {
        userDao.create(user1);
        userDao.create(user2);
        userDao.addFriend(1, 2);
        userDao.addFriend(3, 4);
        userDao.addFriend(1, 4);
        List<User> friends = userDao.getCommonFriends(1, 3);
        assertThat(friends.size()).isEqualTo(1);
        friends = userDao.getCommonFriends(1, 2);
        assertThat(friends.size()).isEqualTo(0);
    }

    @Test
    void getCommonFriends_shouldThrowExceptionWhenIncorrectId() {
        assertThrows(DAOException.class, () -> userDao.getCommonFriends(1, 222));
        assertThrows(DAOException.class, () -> userDao.getCommonFriends(222, 1));
    }

    @Test
    void getFriends_shouldReturnFriendsOfUser() {
        userDao.create(user1);
        userDao.create(user2);
        userDao.addFriend(1, 2);
        userDao.addFriend(1, 3);
        userDao.addFriend(1, 4);
        List<User> friends = userDao.getFriends(1);
        assertThat(friends.size()).isEqualTo(3);
    }

    @Test
    void getFriends_shouldThrowExceptionWhenIncorrectId() {
        assertThrows(DAOException.class, () -> userDao.getFriends(222));
    }

    @Test
    void removeFriend_shouldRemoveOneFriend() {
        userDao.create(user1);
        userDao.create(user2);
        userDao.addFriend(1, 2);
        userDao.addFriend(1, 3);
        userDao.addFriend(1, 4);
        List<User> friends = userDao.getFriends(1);
        assertThat(friends.size()).isEqualTo(3);
        userDao.removeFriend(1, 2);
        friends = userDao.getFriends(1);
        assertThat(friends.size()).isEqualTo(2);
        userDao.removeFriend(1, 3);
        friends = userDao.getFriends(1);
        assertThat(friends.size()).isEqualTo(1);
    }

    @Test
    void removeFriend_shouldThrowExceptionWhenIncorrectId() {
        userDao.create(user1);
        userDao.create(user2);
        userDao.addFriend(1, 2);
        assertThrows(DAOException.class, () -> userDao.removeFriend(222, 1));
        assertThrows(DAOException.class, () -> userDao.removeFriend(1, 222));
        assertThrows(DAOException.class, () -> userDao.removeFriend(1, 3));
    }

    @Test
    void addFriend_shouldAddOneFriend() {
        userDao.create(user1);
        userDao.create(user2);
        userDao.addFriend(1, 2);
        userDao.addFriend(3, 4);
        userDao.addFriend(1, 4);
        List<User> friends1 = userDao.getFriends(1);
        assertThat(friends1.size()).isEqualTo(2);
        List<User> friends3 = userDao.getFriends(3);
        assertThat(friends3.size()).isEqualTo(1);
        List<User> friends2 = userDao.getFriends(2);
        assertThat(friends2.size()).isEqualTo(0);
    }

    @Test
    void addFriend_shouldThrowExceptionWhenIncorrectId() {
        assertThrows(DAOException.class, () -> userDao.addFriend(1, -1));
        assertThrows(DAOException.class, () -> userDao.addFriend(-1, 1));
    }

    @Test
    void getAll_shouldReturnAllUsers() {
        List<User> users = userDao.getAll();
        assertThat(users.size()).isEqualTo(2);
        userDao.create(user1);
        users = userDao.getAll();
        assertThat(users.size()).isEqualTo(3);
    }

    @Test
    void getById_shouldReturnCorrectUser() {
        User user = userDao.getById(1);
        assertThat(user.getId()).isEqualTo(1);
        assertThat(user.getName()).isEqualTo("1");
        assertThat(user.getEmail()).isEqualTo("1");
        assertThat(user.getLogin()).isEqualTo("1");
        assertThat(user.getBirthday()).isEqualTo(LocalDate.of(2000, 1, 10));
    }

    @Test
    void getById_shouldThrowExceptionWhenIncorrectUser() {
        assertThrows(DAOException.class, () -> userDao.getById(-1));
    }

    @Test
    void create_shouldCreateUserWithCorrectParameters() {
        userDao.create(user1);
        User created = userDao.getById(3);
        assertThat(created.getId()).isEqualTo(3);
        assertThat(created.getName()).isEqualTo(user1.getName());
        assertThat(created.getEmail()).isEqualTo(user1.getEmail());
        assertThat(created.getLogin()).isEqualTo(user1.getLogin());
        assertThat(created.getBirthday()).isEqualTo(user1.getBirthday());
    }

    @Test
    void update_shouldUpdateAllFields() {
        user1.setId(1L);
        User updated = userDao.update(user1);
        assertThat(updated.getId()).isEqualTo(1);
        assertThat(updated.getName()).isEqualTo(user1.getName());
        assertThat(updated.getEmail()).isEqualTo(user1.getEmail());
        assertThat(updated.getLogin()).isEqualTo(user1.getLogin());
        assertThat(updated.getBirthday()).isEqualTo(user1.getBirthday());
    }

    @Test
    void update_shouldThrowExceptionWhenIncorrectId() {
        user1.setId(222L);
        assertThrows(DAOException.class, () -> userDao.update(user1));
    }
}