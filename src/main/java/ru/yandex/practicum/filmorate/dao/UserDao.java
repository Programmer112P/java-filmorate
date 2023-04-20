package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.entity.User;

import java.util.List;

public interface UserDao {

    List<User> getAll();

    User getById(long id);

    User create(final User user);

    User update(final User user);
}
