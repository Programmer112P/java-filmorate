package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import javax.validation.ValidationException;
import java.util.*;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    Map<Integer, Film> films = new HashMap<>();
    private int generatorId = 1;

    @PostMapping
    public ResponseEntity<Film> addFilm(@Valid @RequestBody final Film film) {
        film.setId(generatorId++);
        films.put(film.getId(), film);
        return ResponseEntity.ok(film);
    }

    @GetMapping
    public List<Film> getAllFilms() {
        return new ArrayList<>(films.values());
    }

    @PutMapping
    public ResponseEntity<Film> updateFilm(@Valid @RequestBody final Film film) {
        if (!films.containsKey(film.getId())) {
            throw new ValidationException();
        }
        films.put(film.getId(), film);
        return ResponseEntity.ok(film);
    }
}
