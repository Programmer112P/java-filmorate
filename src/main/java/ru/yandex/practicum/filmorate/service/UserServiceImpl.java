package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.UserDao;
import ru.yandex.practicum.filmorate.entity.User;
import ru.yandex.practicum.filmorate.exception.DAOException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserDao userDao;

    @Override
    public List<User> getCommonFriends(long id, long otherId) {
        log.info("Запрос в UserService на получение списка общих друзей для user с ID {} и user с ID {}", id, otherId);
        try {
            User user1 = userDao.getById(id);
            User user2 = userDao.getById(otherId);
            List<Long> commonFriendsIds = new ArrayList<>(user1.getFriends());
            commonFriendsIds.retainAll(user2.getFriends());
            List<User> commonFriends = userDao.getListOfIds(commonFriendsIds);
            log.info("Получен список общих друзей в UserService для user с ID {} и user с id {} ", id, otherId);
            return commonFriends;
        } catch (DAOException e) {
            log.error("Ошибка при получении списка общих друзей в UserService для user с ID {} и user с ID {}", id, otherId);
            throw new NotFoundException(String.format(
                    "Ошибка при получении списка общих друзей в UserService для user с ID %d и user с ID %d", id, otherId), e);
        }
    }

    @Override
    public List<User> getFriends(long id) {
        log.info("Запрос в UserService на получение списка друзей от user с ID {} ", id);
        try {
            User user = getById(id);
            List<Long> friendsIds = new ArrayList<>(user.getFriends());
            List<User> friendsList = userDao.getListOfIds(friendsIds);
            log.info("Получен список друзей в UserService user с ID {} ", id);
            return friendsList;
        } catch (DAOException e) {
            log.error("Ошибка при получении списка друзей от user с ID {}", id);
            throw new NotFoundException(String.format("Ошибка при получении списка друзей от user с ID %d", id), e);
        }
    }

    @Override
    public User removeFriend(long id, long friendId) {
        log.info("Запрос в UserService на удаление друга с ID {} от user с ID {} ", friendId, id);
        try {
            User user = userDao.getById(id);
            User friend = userDao.getById(friendId);
            boolean isFriendExist = user.removeFriend(friendId);
            boolean isUserExist = friend.removeFriend(id);
            if (!isUserExist || !isFriendExist) {
                log.error("Ошибка при удалении User с ID {} из друзей User с ID {}", friendId, id);
                throw new NotFoundException(String.format("User с ID %d не был другом user с ID %d", id, friendId));
            }
            log.info("Удален друг с ID {} у user с ID {} ", friendId, id);
            return user;
        } catch (DAOException e) {
            log.error("Ошибка при удалении User с ID {} из друзей User с ID {}", friendId, id);
            throw new NotFoundException(String.format("User с ID %d либо %d не найден в DAO слое", id, friendId), e);
        }
    }

    @Override
    public User addFriend(long id, long friendId) {
        log.info("Запрос в UserService на добавление друга с ID {} от user с ID {} ", friendId, id);
        try {
            User user = userDao.getById(id);
            User friend = userDao.getById(friendId);
            user.addFriend(friendId);
            friend.addFriend(id);
            return user;
        } catch (DAOException e) {
            log.error("Ошибка при добавлении User с ID {} в друзья к User с ID {}", friendId, id);
            throw new NotFoundException(String.format("User с ID %d либо %d не найден в DAO слое", id, friendId), e);
        }
    }

    @Override
    public User getById(long id) {
        log.info("Запрос в UserService на получение user с ID {} в сервисном слое", id);
        try {
            User user = userDao.getById(id);
            log.info("User {} получен в сервисном слое", user);
            return user;
        } catch (DAOException e) {
            log.error("User с id {} не существует в DAO слое", id);
            throw new NotFoundException(String.format("User с ID %s не найден в DAO слое", id), e);
        }

    }

    @Override
    public List<User> getAll() {
        log.info("Запрос в UserService на получение всех user");
        List<User> users = userDao.getAll();
        log.info("Получен список всех user в сервисном слое");
        return users;
    }

    @Override
    public User create(final User user) {
        log.info("Создается user {} в сервисном слое", user);
        User created = userDao.create(user);
        log.info("Создан user {} в сервисном слое", created);
        return created;
    }

    @Override
    public User update(final User user) {
        try {
            log.info("User {} обновлется в сервисном слое", user);
            User updated = userDao.update(user);
            log.info("User {} обновлен в сервисном слое", updated);
            return updated;
        } catch (DAOException ex) {
            log.error("User с id {} не существует в DAO слое", user.getId());
            throw new NotFoundException((String.format("User с id %s не существует в DAO слое", user.getId())), ex);
        }
    }
}
