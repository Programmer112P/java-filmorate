package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.mock.UserRepository;
import ru.yandex.practicum.filmorate.entity.User;
import ru.yandex.practicum.filmorate.exception.DAOException;

import java.util.List;
import java.util.Optional;

@Component
@Slf4j
@RequiredArgsConstructor
public class MapUserDao implements UserDao {
    private final UserRepository repository;

    @Override
    public List<User> getAll() {
        log.info("Список пользователей получен из DAO");
        return repository.getAllUsers();
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
            log.error("User с id {} не существует в Storage", user.getId());
            throw new DAOException(String.format("User с id %s не существует", user.getId()));
        }
        log.info("User {} обновлен в DAO", user);
        return optional.get();
    }

    @Override
    public User getById(long id) {
        log.info("Запрос на получение User с ID {} в DAO", id);
        Optional<User> optional = repository.getUserById(id);
        if (optional.isEmpty()) {
            log.error("User с id {} не существует в DAO", id);
            throw new DAOException(String.format("User с id %d не существует в Storage", id));
        }
        User user = optional.get();
        log.info("User {} получен в DAO", user);
        return user;
    }
}
