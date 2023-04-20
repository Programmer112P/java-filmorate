package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.entity.Mpa;
import ru.yandex.practicum.filmorate.service.MpaService;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.List;

@RestController
@RequestMapping("/mpa")
@Validated
@Slf4j
public class MpaController {

    private final MpaService mpaService;

    @Autowired
    public MpaController(MpaService mpaService) {
        this.mpaService = mpaService;
    }

    @GetMapping("/{id}")
    public Mpa getById(@Positive @NotNull @PathVariable("id") Long id) {
        log.info("MpaController getById: Запрос на получение жанра с id {}", id);
        Mpa mpa = mpaService.getById(id);
        log.info("MpaController getById: Получен жанр с id {}", id);
        return mpa;
    }

    @GetMapping
    public List<Mpa> getAll() {
        log.info("MpaController getAll: Запрос на получение всех жанров");
        List<Mpa> mpas = mpaService.getAll();
        log.info("MpaController getAll: Получены все жанры");
        return mpas;
    }
}
