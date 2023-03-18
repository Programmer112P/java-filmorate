package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.entity.Film;

import java.util.List;

public interface FilmDao {

    List<Film> getPopular(int count);

    Film getById(long id);

    List<Film> getAll();

    Film create(final Film film);

    Film update(final Film film);
}
