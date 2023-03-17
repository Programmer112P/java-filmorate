package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.mock.FilmRepository;
import ru.yandex.practicum.filmorate.entity.Film;
import ru.yandex.practicum.filmorate.exception.DAOException;

import java.util.List;
import java.util.Optional;

@Component
@Slf4j
@RequiredArgsConstructor
public class MapFilmDao implements FilmDao {
    private final FilmRepository repository;

    @Override
    public Film getById(long id) {
        log.info("Запрос на получение фильма с ID {} в DAO", id);
        Optional<Film> optional = repository.getFilmById(id);
        if (optional.isEmpty()) {
            log.error("Фильма с id {} не существует в Storage", id);
            throw new DAOException(String.format("Фильма с id %s не существует в Storage", id));
        }
        Film film = optional.get();
        log.info("Получен фильм{} в DAO", film);
        return film;
    }

    @Override
    public List<Film> getAll() {
        log.info("Список фильмов получен из DAO");
        return repository.getAllFilms();
    }

    @Override
    public Film create(final Film film) {
        log.info("Фильм {} создается в DAO", film);
        Film created = repository.createFilm(film);
        log.info("Фильм {} создан в DAO", created);
        return created;
    }

    @Override
    public Film update(final Film film) {
        log.info("Фильм {} обновляется в DAO", film);
        Optional<Film> optional = repository.updateFilm(film);
        if (optional.isEmpty()) {
            log.error("Фильма с id {} не существует в Storage", film.getId());
            throw new DAOException(String.format("Фильма с id %s не существует в Storage", film.getId()));
        }
        Film updated = optional.get();
        log.info("Фильм {} обновлен в DAO", updated);
        return updated;
    }
}
