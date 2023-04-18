package ru.yandex.practicum.filmorate.dao.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.UserDao;
import ru.yandex.practicum.filmorate.entity.User;
import ru.yandex.practicum.filmorate.exception.DAOException;

import java.util.*;

@Component
@Slf4j
@RequiredArgsConstructor
public class InMemoryUserDao implements UserDao {
    private final Map<Long, User> storage = new HashMap<>();
    private long generatorId = 1;

    @Override
    public List<User> getAll() {
        log.info("Запрос на получение списка пользователей из DAO");
        List<User> users = new ArrayList<>(storage.values());
        log.info("Список пользователей получен из DAO");
        return users;
    }

    @Override
    public User create(final User user) {
        log.info("User {} создается в DAO", user);
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        user.setId(generatorId++);
        //user.setFriends(new HashSet<>());//Я не придумал другого решения
        storage.put(user.getId(), user);
        log.info("User {} создан в DAO", user);
        return user;
    }

    @Override
    public User update(final User user) {
        log.info("User {} обновляется в DAO", user);
        if (!storage.containsKey(user.getId())) {
            log.error("User с id {} не существует в репозитории", user.getId());
            throw new DAOException(String.format("User с id %s не существует в репозитории", user.getId()));
        }
        //if (user.getFriends() == null){//Через костыль. Когда приходит json с null - обнуляется поле friends
      //      user.setFriends(new HashSet<>());
        //}
        storage.put(user.getId(), user);
        log.info("User {} обновлен в DAO", user);
        return user;
    }

    @Override
    public User getById(long id) {
        log.info("Запрос на получение User с ID {} в репозитории", id);
        User user = storage.get(id);
        if (user == null) {
            log.error("User с id {} не существует в DAO", id);
            throw new DAOException(String.format("В репозитории не существует User с id %d", id));
        }
        log.info("User {} получен в DAO", user);
        return user;
    }

    @Override
    public List<User> getCommonFriends(long id, long otherId) {
        return null;
    }

    @Override
    public List<User> getFriends(long id) {
        return null;
    }

    @Override
    public void removeFriend(long id, long friendId) {
    }

    @Override
    public void addFriend(long id, long friendId) {
    }
}
