package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.entity.User;
import ru.yandex.practicum.filmorate.exception.ServiceException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.*;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public List<User> getAllUsers() {
        log.info("Запрос на получение списка Users в контроллере");
        List<User> list = userService.getAll();
        log.info("Получен список Users {} в контроллере", list);
        return list;
    }

    @PostMapping
    public User createUser(@Valid @RequestBody final User user) {
        log.info("User {} создается в контроллере", user);
        User created = userService.create(user);
        log.info("User {} создан в контроллере", created);
        return created;
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody final User user) {
        try {
            log.info("User {} обновляется в контроллере", user);
            User updated = userService.update(user);
            log.info("User {} обновлен в контроллере", updated);
            return updated;
        } catch (ServiceException ex) {
            log.error("User с id {} не существует в контроллере", user.getId());
            throw new NotFoundException("Неверный id пользователя", ex);
        }
    }
}
