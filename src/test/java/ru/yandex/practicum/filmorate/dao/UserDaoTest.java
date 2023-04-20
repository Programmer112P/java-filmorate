package ru.yandex.practicum.filmorate.dao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.dao.impl.UserDaoImpl;
import ru.yandex.practicum.filmorate.dao.impl.mapper.UserRowMapper;
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
    private final UserRowMapper userRowMapper;
    private User user1;
    private User user2;

    @Autowired
    public UserDaoTest(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        userRowMapper = new UserRowMapper();
        userDao = new UserDaoImpl(jdbcTemplate, userRowMapper);
        user1 = User.builder().name("TestUser1 Name").email("test1@email").login("TestUser1 Login")
                .birthday(LocalDate.of(1999, 1, 1)).build();
        user2 = User.builder().name("TestUser2 Name").email("test2@email").login("TestUser2 Login")
                .birthday(LocalDate.of(1999, 2, 2)).build();
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