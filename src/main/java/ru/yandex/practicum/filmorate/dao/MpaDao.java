package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.entity.Mpa;

import java.util.List;

public interface MpaDao {

    Mpa getById(long id);

    List<Mpa> getAll();
}