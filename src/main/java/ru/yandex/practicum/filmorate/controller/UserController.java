package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.entity.User;
import ru.yandex.practicum.filmorate.exception.ServiceException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.*;

@Slf4j
@RestController
@Validated
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping()
    public List<User> getAllUsers() {
        log.info("Запрос на получение списка Users в контроллере");
        List<User> list = userService.getAll();
        log.info("Получен список Users {} в контроллере", list);
        return list;
    }

    @GetMapping("/{id}")
    public User getById(@Min(1) @PathVariable long id) {
        log.info("Запрос на получение User c id {} в контроллере", id);
        try {
            User user = userService.getById(id);
            log.info("Получен User {} в контроллере", user);
            return user;
        } catch (ServiceException ex) {
            log.error("User с id {} не существует в UserService", id);
            throw new NotFoundException("Неверный id пользователя", ex);
        }

    }

    @PostMapping
    public User create(@Valid @RequestBody final User user) {
        log.info("User {} создается в контроллере", user);
        User created = userService.create(user);
        log.info("User {} создан в контроллере", created);
        return created;
    }

    @PutMapping
    public User update(@Valid @RequestBody final User user) {
        log.info("User {} обновляется в контроллере", user);
        try {
            User updated = userService.update(user);
            log.info("User {} обновлен в контроллере", updated);
            return updated;
        } catch (ServiceException ex) {
            log.error("User с id {} не существует в UserService", user.getId());
            throw new NotFoundException("Неверный id пользователя", ex);
        }
    }
}
