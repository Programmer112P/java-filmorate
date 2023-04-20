package ru.yandex.practicum.filmorate.dao.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.FilmDao;
import ru.yandex.practicum.filmorate.entity.Film;
import ru.yandex.practicum.filmorate.exception.DAOException;

import java.util.*;

@Component
@Slf4j
@RequiredArgsConstructor
public class InMemoryFilmDao implements FilmDao {
    private final Map<Long, Film> storage = new HashMap<>();
    private long generatorId = 1;

    @Override
    public Film getById(long id) {
        log.info("Запрос на получение фильма с ID {} в DAO", id);
        Film film = storage.get(id);
        if (film == null) {
            log.error("Фильма с id {} не существует в Storage", id);
            throw new DAOException(String.format("Фильма с id %s не существует в Storage", id));
        }
        log.info("Получен фильм{} в DAO", film);
        return film;
    }

    @Override
    public List<Film> getAll() {
        log.info("FilmDao: запрос на получение всех фильмов из DAO");
        List<Film> films = new ArrayList<>(storage.values());
        log.info("Список фильмов получен из DAO");
        return films;
    }

    @Override
    public Film create(final Film film) {
        log.info("Фильм {} создается в DAO", film);
        film.setId(generatorId++);
        storage.put(film.getId(), film);
        log.info("Фильм {} создан в DAO", film);
        return film;
    }

    @Override
    public Film update(final Film film) {
        log.info("Фильм {} обновляется в DAO", film);
        if (!storage.containsKey(film.getId())) {
            log.error("Фильма с id {} не существует в Storage", film.getId());
            throw new DAOException(String.format("Фильма с id %s не существует в Storage", film.getId()));
        }
        storage.put(film.getId(), film);
        log.info("Фильм {} обновлен в DAO", film);
        return film;
    }
}
