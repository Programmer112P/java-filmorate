package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.FilmDao;
import ru.yandex.practicum.filmorate.dao.UserDao;
import ru.yandex.practicum.filmorate.entity.Film;
import ru.yandex.practicum.filmorate.entity.User;
import ru.yandex.practicum.filmorate.exception.DAOException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilmServiceImpl implements FilmService {
    private final FilmDao filmDao;
    private final UserDao userDao;

    @Override
    public List<Film> getPopular(int count) {
        log.info("FilmService: Запрос на getPopular с count = {}", count);
        List<Film> films = getAll()
                .stream()
                .sorted(((o1, o2) -> o2.getUsersLike().size() - o1.getUsersLike().size()))
                .limit(count)
                .collect(Collectors.toList());
        log.info("FilmService: получены фильмы от getPopular с count = {}", count);
        return films;
    }

    @Override
    public Film removeLike(long id, long userId) {
        log.info("FilmService: Запрос на removeLike от User с ID {} фильму с ID {}", userId, id);
        try {
            Film film = filmDao.getById(id);
            boolean isDeletionHappened = film.removeLike(userId);
            if (!isDeletionHappened) {
                log.error("FilmService: User с ID {} не лайкал фильм с ID {} при removeLike", userId, id);
                throw new NotFoundException(String.format(
                        "FilmService: User с ID %d не лайкал фильм с ID %d при removeLike", userId, id));
            }
            log.info("FilmService: удален Like от User с ID {} фильму с ID {}", userId, id);
            return film;
        } catch (DAOException e) {
            log.error("FilmService: Фильма с id {} не существует в DAO слое при removeLike", id);
            throw new NotFoundException(String.format(
                    "FilmService: Фильма с id %d не существует в DAO слое при removeLike в DAO", id), e);
        }
    }

    @Override
    public Film addLike(long id, long userId) {
        log.info("FilmService: Запрос на addLike от User с ID {} фильму с ID {}", userId, id);
        try {
            User user = userDao.getById(userId);//проверяю на существование User
            Film film = filmDao.getById(id);
            film.addLike(userId);
            log.info("FilmService: like от User с ID {} поставлен фильму с ID {}", userId, id);
            return film;
        } catch (DAOException e) {
            log.error("FilmService: Фильма с id {} или User с ID {} не существует в DAO слое при addLike", id, userId);
            throw new NotFoundException(String.format(
                    "FilmService: Фильма с id %d или User с ID %d не существует в DAO слое при addLike в DAO", id, userId), e);
        }
    }

    @Override
    public Film getById(long id) {
        log.info("Запрос на получение фильма с ID {} в сервисном слое", id);
        try {
            Film film = filmDao.getById(id);
            log.info("Получен фильм {} в сервисном слое", film);
            return film;
        } catch (DAOException e) {
            log.error("Фильма с id {} не существует в DAO слое", id);
            throw new NotFoundException(String.format("Фильма с id %d не существует в DAO слое", id), e);
        }
    }

    @Override
    public List<Film> getAll() {
        log.info("Запрос на получение всех фильмов в сервисном слое");
        List<Film> list = filmDao.getAll();
        log.info("Получены все фильмы в сервисном слое");
        return list;
    }

    @Override
    public Film create(final Film film) {
        log.info("Фильм {} создется в сервисном слое", film);
        Film created = filmDao.create(film);
        log.info("Фильм {} создан в сервисном слое", created);
        return created;
    }

    @Override
    public Film update(final Film film) {
        try {
            log.info("Фильм {} обновляется в сервисном слое", film);
            Film updated = filmDao.update(film);
            log.info("Фильм {} обновлен в сервисном слое", updated);
            return updated;
        } catch (DAOException e) {
            log.error("Фильма с id {} не существует в DAO слое", film.getId());
            throw new NotFoundException((String.format("Фильма с id %s не существует", film.getId())), e);
        }
    }
}
