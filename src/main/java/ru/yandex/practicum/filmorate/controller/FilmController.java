package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.entity.Film;
import ru.yandex.practicum.filmorate.exception.ServiceException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.*;

@Slf4j
@RestController
@RequestMapping("/films")
@RequiredArgsConstructor
public class FilmController {
    private final FilmService service;

    @GetMapping
    public List<Film> getAll() {
        log.info("Список фильмов получен в контроллере");
        return service.getAll();
    }

    @PostMapping
    public Film create(@Valid @RequestBody final Film film) {
        log.info("Фильм {} создается в контроллере", film);
        Film created = service.create(film);
        log.info("Фильм {} создан в контроллере", created);
        return created;
    }

    @PutMapping
    public Film update(@Valid @RequestBody final Film film) {
        try {
            log.info("Фильм {} обновляется в контроллере", film);
            Film updated = service.update(film);
            log.info("Фильм {} обновлен в контроллере", updated);
            return updated;
        } catch (ServiceException ex) {
            log.error("Фильма с id {} не существует в контроллере", film.getId());
            throw new NotFoundException("Неверный id фильма", ex);
        }
    }
}
