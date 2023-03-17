package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.FilmDao;
import ru.yandex.practicum.filmorate.entity.Film;
import ru.yandex.practicum.filmorate.exception.DAOException;
import ru.yandex.practicum.filmorate.exception.ServiceException;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilmServiceImpl implements FilmService {
    private final FilmDao filmDao;

    @Override
    public Film getById(long id) {
        log.info("Запрос на получение фильма с ID {} в сервисном слое", id);
        try {
            Film film = filmDao.getById(id);
            log.info("Получен фильм {} в сервисном слое", film);
            return film;
        } catch (DAOException ex) {
            log.error("Фильма с id {} не существует в DAO слое", id);
            throw new ServiceException(String.format("Фильма с id %d не существует в DAO слое", id), ex);
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
        } catch (DAOException ex) {
            log.error("Фильма с id {} не существует в DAO слое", film.getId());
            throw new ServiceException((String.format("Фильма с id %s не существует", film.getId())), ex);
        }
    }
}
