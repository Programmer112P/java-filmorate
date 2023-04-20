package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.entity.Film;

import java.util.List;

public interface FilmService {

    List<Film> getPopular(int amount);

    void removeLike(long filmId, long userId);

    void addLike(long filmId, long userId);

    Film getById(long id);

    List<Film> getAll();

    Film create(final Film film);

    Film update(final Film film);
}
