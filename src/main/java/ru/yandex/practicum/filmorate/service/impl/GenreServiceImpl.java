package ru.yandex.practicum.filmorate.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.GenreDao;
import ru.yandex.practicum.filmorate.entity.Genre;
import ru.yandex.practicum.filmorate.exception.DAOException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.service.GenreService;

import java.util.List;

@Slf4j
@Service
public class GenreServiceImpl implements GenreService {
    private final GenreDao genreDao;

    @Autowired
    public GenreServiceImpl(GenreDao genreDao) {
        this.genreDao = genreDao;
    }

    @Override
    public List<Genre> getAll() {
        log.info("GenreService getAll: Запрос на получение всех жанров");
        List<Genre> genres = genreDao.getAll();
        log.info("GenreService getAll: Выполнен запрос на получение всех жанров");
        return genres;
    }

    @Override
    public Genre getById(long id) {
        log.info("GenreService getById: Запрос на получение жанра с id {}", id);
        try {
            Genre genre = genreDao.getById(id);
            log.info("GenreService getById: Получен жанр с id {}", id);
            return genre;
        } catch (DAOException e) {
            log.error("GenreService getById: Не существует жанра с id {}", id);
            throw new NotFoundException(e);
        }
    }
}
