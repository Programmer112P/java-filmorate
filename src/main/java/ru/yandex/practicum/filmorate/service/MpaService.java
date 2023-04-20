package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.entity.Mpa;

import java.util.List;

public interface MpaService {
    Mpa getById(Long id);

    List<Mpa> getAll();
}
