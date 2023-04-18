package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.entity.User;

import java.util.List;

public interface UserService {

    List<User> getCommonFriends(long id, long otherId);

    List<User> getFriends(long id);

    void addFriend(long id, long friendId);

    void removeFriend(long id, long friendId);

    User getById(long id);

    List<User> getAll();

    User create(final User user);

    User update(final User user);
}
