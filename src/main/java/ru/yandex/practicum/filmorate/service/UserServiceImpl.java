package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.UserDao;
import ru.yandex.practicum.filmorate.entity.User;
import ru.yandex.practicum.filmorate.exception.DAOException;
import ru.yandex.practicum.filmorate.exception.ServiceException;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserDao userDao;

    @Override
    public List<User> getAll() {
        log.info("Получен список всех user в сервисном слое");
        return userDao.getAll();
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
            log.info("User с id {} обновлется в сервисном слое", user);
            User updated = userDao.update(user);
            log.info("User с id {} обновлен в сервисном слое", updated);
            return updated;
        } catch (DAOException ex) {
            log.error("User с id {} не существует в сервисном слое", user.getId());
            throw new ServiceException((String.format("User с id %s не существует", user.getId())), ex);
        }
    }
}
