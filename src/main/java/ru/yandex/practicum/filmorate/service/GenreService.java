package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.entity.Genre;

import java.util.List;

public interface GenreService {

    List<Genre> getAll();

    Genre getById(long id);
}
