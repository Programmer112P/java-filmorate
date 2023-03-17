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
    public User getById(long id) {
        log.info("Запрос на получение user с ID {} в сервисном слое", id);
        try {
            User user =  userDao.getById(id);
            log.info("User {} получен в сервисном слое", user);
            return user;
        } catch (DAOException e) {
            log.error("User с id {} не существует в сервисном слое", id);
            throw new ServiceException(String.format("User с ID %s не найден в сервисном слое", id), e);
        }

    }

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
            log.info("User {} обновлется в сервисном слое", user);
            User updated = userDao.update(user);
            log.info("User {} обновлен в сервисном слое", updated);
            return updated;
        } catch (DAOException ex) {
            log.error("User с id {} не существует в сервисном слое", user.getId());
            throw new ServiceException((String.format("User с id %s не существует", user.getId())), ex);
        }
    }
}
