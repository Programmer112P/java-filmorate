package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.entity.Genre;

import java.util.List;

public interface GenreDao {

    List<Genre> getGenresOfFilm(long filmId);

    List<Genre> getAll();

    Genre getById(long id);
}
