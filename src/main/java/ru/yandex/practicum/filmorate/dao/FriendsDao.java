package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.entity.User;

import java.util.List;

public interface FriendsDao {

    List<User> getCommonFriends(long id, long otherId);

    List<User> getFriends(long id);

    void removeFriend(long id, long friendId);

    void addFriend(long id, long friendId);
}
