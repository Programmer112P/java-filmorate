package ru.yandex.practicum.filmorate.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.FriendsDao;
import ru.yandex.practicum.filmorate.dao.UserDao;
import ru.yandex.practicum.filmorate.entity.User;
import ru.yandex.practicum.filmorate.exception.DAOException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.List;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserDao userDao;
    private final FriendsDao friendsDao;

    @Autowired
    public UserServiceImpl(@Qualifier("userDaoImpl") UserDao userDao, FriendsDao friendsDao) {
        this.userDao = userDao;
        this.friendsDao = friendsDao;
    }

    @Override
    public List<User> getCommonFriends(long id, long otherId) {
        log.info("UserService addFriend: Запрос на получение списка общих друзей для user с ID {} и user с ID {}", id, otherId);
        try {
            List<User> commonFriends = friendsDao.getCommonFriends(id, otherId);
            log.info("UserService addFriend: Получен список общих друзей для user с ID {} и user с id {} ", id, otherId);
            return commonFriends;
        } catch (DAOException e) {
            log.error("UserService addFriend: Ошибка при получении списка общих друзей для user с ID {} и user с ID {}", id, otherId);
            throw new NotFoundException(String.format(
                    "Ошибка при получении списка общих друзей для user с ID %d и user с ID %d", id, otherId), e);
        }
    }

    @Override
    public List<User> getFriends(long id) {
        log.info("UserService getFriends: Запрос на получение списка друзей от user с ID {} ", id);
        try {
            List<User> friendsList = friendsDao.getFriends(id);
            log.info("UserService getFriends: Получен список друзей user от с ID {} ", id);
            return friendsList;
        } catch (DAOException e) {
            log.error("UserService getFriends: Ошибка при получении списка друзей от user с ID {}", id);
            throw new NotFoundException(String.format("Ошибка при получении списка друзей от user с ID %d", id), e);
        }
    }

    @Override
    public void removeFriend(long id, long friendId) {
        log.info("UserService removeFriend: Запрос на удаление друга с ID {} от user с ID {} ", friendId, id);
        try {
            friendsDao.removeFriend(id, friendId);
            log.info("UserService removeFriend: Удален друг с ID {} у user с ID {} ", friendId, id);
        } catch (DAOException e) {
            log.error("UserService removeFriend: Ошибка при удалении User с ID {} из друзей User с ID {}", friendId, id);
            throw new NotFoundException(String.format("User с ID %d либо %d не найден", id, friendId), e);
        }
    }

    @Override
    public void addFriend(long id, long friendId) {
        log.info("UserService addFriend: Запрос на добавление друга с ID {} от user с ID {} ", friendId, id);
        try {
            friendsDao.addFriend(id, friendId);
            log.info("UserService addFriend: Добавлен друг от id {} к id {} ", id, friendId);
        } catch (DAOException e) {
            log.error("UserService addFriend: Ошибка при добавлении User с ID {} в друзья к User с ID {}", friendId, id);
            throw new NotFoundException(String.format("User с ID %d либо %d не найден", id, friendId), e);
        }
    }

    @Override
    public User getById(long id) {
        log.info("UserService getById: Запрос на получение user с ID {} в сервисном слое", id);
        try {
            User user = userDao.getById(id);
            log.info("UserService getById: User {} получен", user);
            return user;
        } catch (DAOException e) {
            log.error("UserService getById: User с id {} не существует ", id);
            throw new NotFoundException(String.format("User с ID %s не найден", id), e);
        }
    }

    @Override
    public List<User> getAll() {
        log.info("UserService getAll: Запрос на получение всех user");
        List<User> users = userDao.getAll();
        log.info("UserService getAll: Получен список всех user");
        return users;
    }

    @Override
    public User create(final User user) {
        log.info("UserService create: Создается user {}", user);
        if (user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        User created = userDao.create(user);
        log.info("UserService create: Создан user {}", created);
        return created;
    }

    @Override
    public User update(final User user) {
        try {
            log.info("UserService update: User {} обновлется", user);
            if (user.getName().isBlank()) {
                user.setName(user.getLogin());
            }
            User updated = userDao.update(user);
            log.info("UserService update: User {} обновлен", updated);
            return updated;
        } catch (DAOException ex) {
            log.error("UserService update: User с id {} не существует", user.getId());
            throw new NotFoundException((String.format("User с id %s не найден", user.getId())), ex);
        }
    }
}
