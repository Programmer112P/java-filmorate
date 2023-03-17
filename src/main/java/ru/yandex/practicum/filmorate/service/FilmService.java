package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.entity.Film;

import java.util.List;

public interface FilmService {

    Film getById(long id);

    List<Film> getAll();

    Film create(final Film film);

    Film update(final Film film);
}
