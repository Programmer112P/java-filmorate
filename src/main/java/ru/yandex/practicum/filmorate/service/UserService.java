package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.entity.User;

import java.util.List;

public interface UserService {

    User getById(long id);

    List<User> getAll();

    User create(final User user);

    User update(final User user);
}
