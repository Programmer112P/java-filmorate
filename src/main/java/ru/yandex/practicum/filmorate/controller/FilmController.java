package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.entity.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.*;

@Slf4j
@RestController
@Validated
@RequestMapping("/films")
@RequiredArgsConstructor
public class FilmController {
    private final FilmService filmService;

    @GetMapping("/popular")
    public List<Film> getPopular(@Min(1) @RequestParam(value = "count", required = false, defaultValue = "10") int count) {
        log.info("FilmController: Запрос на getPopular с count = {}", count);
        List<Film> films = filmService.getPopular(count);
        log.info("FilmController: Получен список popularFilms");
        return films;
    }

    @DeleteMapping("/{id}/like/{userId}")
    public Film removeLike(@NotNull @PathVariable long id,
                           @NotNull @PathVariable long userId) {
        log.info("FilmController: Запрос на removeLike от User с ID {} фильму с ID {}", userId, id);
        Film film = filmService.removeLike(id, userId);
        log.info("FilmController: удален like от User с ID {} фильму с ID {}", userId, id);
        return film;
    }

    @PutMapping("/{id}/like/{userId}")
    public Film addLike(@NotNull @PathVariable long id,
                        @NotNull @PathVariable long userId) {
        log.info("Запрос на like от User с ID {} фильму с ID {} в FilmController", userId, id);
        Film film = filmService.addLike(id, userId);
        log.info("Поставлен like от User с ID {} фильму с ID {} в FilmController", userId, id);
        return film;

    }

    @GetMapping
    public List<Film> getAll() {
        log.info("Запрос на получение списка Фильмов в FilmController");
        List<Film> list = filmService.getAll();
        log.info("Получен список allFilms в FilmController");
        return list;
    }

    @GetMapping("/{id}")
    public Film getById(@Min(1) @PathVariable long id) {
        log.info("Запрос на получение Film c id {} в FilmController", id);
        Film film = filmService.getById(id);
        log.info("Получен Film {} в FilmController", film);
        return film;
    }

    @PostMapping
    public Film create(@Valid @RequestBody final Film film) {
        log.info("Фильм {} создается в FilmController", film);
        Film created = filmService.create(film);
        log.info("Фильм {} создан в FilmController", created);
        return created;
    }

    @PutMapping
    public Film update(@Valid @RequestBody final Film film) {
        log.info("Фильм {} обновляется в FilmController", film);
        Film updated = filmService.update(film);
        log.info("Фильм {} обновлен в FilmController", updated);
        return updated;
    }
}
