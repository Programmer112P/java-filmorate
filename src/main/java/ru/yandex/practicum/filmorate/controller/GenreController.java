package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.entity.Genre;
import ru.yandex.practicum.filmorate.service.GenreService;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.List;

@Slf4j
@RestController
@Validated
@RequestMapping("/genres")
public class GenreController {
    private final GenreService genreService;

    @Autowired
    public GenreController(GenreService genreService) {
        this.genreService = genreService;
    }

    @GetMapping
    public List<Genre> getAll() {
        log.info("GenreController getAll: Запрос на получение всех жанров");
        List<Genre> genres = genreService.getAll();
        log.info("GenreController getAll: Получены все жанры");
        return genres;
    }

    @GetMapping("/{id}")
    public Genre getById(@NotNull @Positive @PathVariable("id") long id) {
        log.info("GenreController getById: Запрос на получение жанра с id {}", id);
        Genre genre = genreService.getById(id);
        log.info("GenreController getById: Получен жанр с id {}", id);
        return genre;
    }

}
