package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.entity.User;

import java.util.List;

public interface UserDao {
    //read
    List<User> getAll();

    User getById(long id);

    //create
    User create(final User user);

    //update
    User update(final User user);

    //delete
    //...
}
