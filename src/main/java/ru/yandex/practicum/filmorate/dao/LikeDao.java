package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.entity.Film;

import java.util.List;

public interface LikeDao {

    void removeLike(long filmId, long userId);

    void addLike(long filmId, long userId);

    List<Film> getMostPopular(int amount);
}
