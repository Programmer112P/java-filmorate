package ru.yandex.practicum.filmorate.dao.mock;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.entity.Film;

import java.util.*;

@Component
public class FilmRepository {
    private final Map<Long, Film> storage = new HashMap<>();
    private long generatorId = 1;

    public Optional<Film> getFilmById(long id) {
        Film film = storage.get(id);
        if (film == null) {
            return Optional.empty();
        }
        return Optional.of(film);
    }

    public List<Film> getAllFilms() {
        return new ArrayList<>(storage.values());
    }

    public Film createFilm(final Film film) {
        film.setId(generatorId++);
        storage.put(film.getId(), film);
        return film;
    }

    public Optional<Film> updateFilm(final Film updatedFilm) {
        Film oldFilm = storage.get(updatedFilm.getId());
        if (oldFilm == null) {
            return Optional.empty();
        }
        storage.put(updatedFilm.getId(), updatedFilm);
        return Optional.of(updatedFilm);
    }
}
