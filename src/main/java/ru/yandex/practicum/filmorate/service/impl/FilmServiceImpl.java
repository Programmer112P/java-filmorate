package ru.yandex.practicum.filmorate.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.FilmDao;
import ru.yandex.practicum.filmorate.dao.LikeDao;
import ru.yandex.practicum.filmorate.entity.Film;
import ru.yandex.practicum.filmorate.exception.DAOException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.List;

@Slf4j
@Service
public class FilmServiceImpl implements FilmService {
    private final FilmDao filmDao;
    private final LikeDao likeDao;

    @Autowired
    public FilmServiceImpl(@Qualifier("filmDaoImpl") FilmDao filmDao, LikeDao likeDao) {
        this.filmDao = filmDao;
        this.likeDao = likeDao;
    }

    @Override
    public List<Film> getPopular(int amount) {
        log.info("FilmService getPopular: Запрос c количеством = {}", amount);
        List<Film> films = likeDao.getMostPopular(amount);
        log.info("FilmService getPopular: получены фильмы количеством {}", amount);
        return films;
    }

    @Override
    public void removeLike(long filmId, long userId) {
        log.info("FilmService removeLike: от User с ID {} фильму с ID {}", userId, filmId);
        try {
            likeDao.removeLike(filmId, userId);
            log.info("FilmService: удален Like от User с ID {} фильму с ID {}", userId, filmId);
        } catch (DAOException e) {
            log.error("FilmService removeLike: Фильма с id {} не существует", filmId);
            throw new NotFoundException(String.format(
                    "Фильма с id %d не существует", filmId), e);
        }
    }

    @Override
    public void addLike(long filmId, long userId) {
        log.info("FilmService: Запрос на addLike от User с ID {} фильму с ID {}", userId, filmId);
        try {
            likeDao.addLike(filmId, userId);
            log.info("FilmService: like от User с ID {} поставлен фильму с ID {}", userId, filmId);
        } catch (DAOException e) {
            log.error("FilmService addLike: Фильма с id {} или User с ID {} не существует ", filmId, userId);
            throw new NotFoundException(String.format(
                    "Фильма с id %d или User с ID %d не существует", filmId, userId), e);
        }
    }

    @Override
    public Film getById(long id) {
        log.info("FilmService getById: Запрос на получение фильма с ID {}", id);
        try {
            Film film = filmDao.getById(id);
            log.info("FilmService getById: Получен фильм {}", film);
            return film;
        } catch (DAOException e) {
            log.error("FilmService getById: Фильма с id {} не существует", id);
            throw new NotFoundException(String.format("Фильма с id %d не существует", id), e);
        }
    }

    @Override
    public List<Film> getAll() {
        log.info("FilmService getAll: Запрос на получение всех фильмов");
        List<Film> list = filmDao.getAll();
        log.info("FilmService getAll: Получены все фильмы");
        return list;
    }

    @Override
    public Film create(final Film film) {
        log.info("FilmService create: Фильм {} создется", film);
        Film created = filmDao.create(film);
        log.info("FilmService create: Фильм {} создан", created);
        return created;
    }

    @Override
    public Film update(final Film film) {
        try {
            log.info("FilmService update: Фильм {} обновляется", film);
            Film updated = filmDao.update(film);
            log.info("FilmService update: Фильм c id {} обновлен", updated);
            return updated;
        } catch (DAOException e) {
            log.error("FilmService update: Фильма с id {} не существует", film.getId());
            throw new NotFoundException((String.format("Фильма с id %s не существует", film.getId())), e);
        }
    }
}
