package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.entity.Film;
import ru.yandex.practicum.filmorate.exception.ServiceException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.*;

@Slf4j
@RestController
@Validated
@RequestMapping("/films")
@RequiredArgsConstructor
public class FilmController {
    private final FilmService filmService;

    @GetMapping
    public List<Film> getAll() {
        log.info("Запрос на получение списка Фильмов в контроллере");
        List<Film> list = filmService.getAll();
        log.info("Получен список Films {} в контроллере", list);
        return list;
    }

    @GetMapping("/{id}")
    public Film getById(@Min(1) @PathVariable long id) {
        log.info("Запрос на получение Film c id {} в контроллере", id);
        try {
            Film film = filmService.getById(id);
            log.info("Получен Film {} в контроллере", film);
            return film;
        } catch (ServiceException ex) {
            log.error("Фильма с id {} не существует в FilmService", id);
            throw new NotFoundException("Неверный id фильма", ex);
        }
    }


    @PostMapping
    public Film create(@Valid @RequestBody final Film film) {
        log.info("Фильм {} создается в контроллере", film);
        Film created = filmService.create(film);
        log.info("Фильм {} создан в контроллере", created);
        return created;
    }

    @PutMapping
    public Film update(@Valid @RequestBody final Film film) {
        try {
            log.info("Фильм {} обновляется в контроллере", film);
            Film updated = filmService.update(film);
            log.info("Фильм {} обновлен в контроллере", updated);
            return updated;
        } catch (ServiceException ex) {
            log.error("Фильма с id {} не существует в FilmService", film.getId());
            throw new NotFoundException("Неверный id фильма", ex);
        }
    }
}
