package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.mock.InMemoryUserRepository;
import ru.yandex.practicum.filmorate.entity.User;
import ru.yandex.practicum.filmorate.exception.DAOException;

import java.util.List;
import java.util.Optional;

@Component
@Slf4j
@RequiredArgsConstructor
public class InMemoryUserDao implements UserDao {
    private final InMemoryUserRepository repository;

    @Override
    public List<User> getListOfIds(List<Long> ids) {
        log.info("Запрос на получение списка Users по id из DAO");
        Optional<List<User>> optional = repository.getList(ids);
        if (optional.isEmpty()) {
            log.error("Одного из пользователей списка нет в репозитории");
            throw new DAOException("Одного из пользователей списка нет в репозитории");
        }
        log.info("Список Users получен в DAO");
        return optional.get();
    }

    @Override
    public List<User> getAll() {
        log.info("Запрос на получение списка пользователей из DAO");
        List<User> users = repository.getAllUsers();
        log.info("Список пользователей получен из DAO");
        return users;
    }

    @Override
    public User create(final User user) {
        log.info("User {} создается в DAO", user);
        User created = repository.createUser(user);
        log.info("User {} создан в DAO", created);
        return created;
    }

    @Override
    public User update(final User user) {
        log.info("User {} обновляется в DAO", user);
        Optional<User> optional = repository.updateUser(user);
        if (optional.isEmpty()) {
            log.error("User с id {} не существует в репозитории", user.getId());
            throw new DAOException(String.format("User с id %s не существует в репозитории", user.getId()));
        }
        log.info("User {} обновлен в DAO", user);
        return optional.get();
    }

    @Override
    public User getById(long id) {
        log.info("Запрос на получение User с ID {} в репозитории", id);
        Optional<User> optional = repository.getUserById(id);
        if (optional.isEmpty()) {
            log.error("User с id {} не существует в DAO", id);
            throw new DAOException(String.format("В репозитории не существует User с id %d", id));
        }
        User user = optional.get();
        log.info("User {} получен в DAO", user);
        return user;
    }

}
