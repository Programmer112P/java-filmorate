package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.entity.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.*;

@Slf4j
@RestController
@Validated
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping()
    public List<User> getAllUsers() {
        log.info("Запрос на получение списка Users в контроллере");
        List<User> list = userService.getAll();
        log.info("Получен список Users {} в контроллере", list);
        return list;
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getCommonFriends(@NotNull @Min(1) @PathVariable long id,
                                       @NotNull @Min(1) @PathVariable long otherId) {
        log.info("Запрос в UserController на получение общих друзей user с ID {} и user с ID {}", id, otherId);
        List<User> commonFriends = userService.getCommonFriends(id, otherId);
        log.info("Получены общие друзья в UserController: user с ID {} и user с ID {}", id, otherId);
        return commonFriends;
    }

    @GetMapping("/{id}/friends")
    public List<User> getFriends(@NotNull @PathVariable long id) {
        log.info("Запрос на получение Friends от user с ID {} в контроллере", id);
        List<User> friends = userService.getFriends(id);
        log.info("Получены Friends от user с ID {} в контроллере", id);
        return friends;
    }

    @GetMapping("/{id}")
    public User getById(@NotNull @Min(1) @PathVariable long id) {
        log.info("Запрос на получение User c id {} в контроллере", id);
        User user = userService.getById(id);
        log.info("Получен User {} в контроллере", user);
        return user;
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@NotNull @PathVariable long id,
                          @NotNull @PathVariable long friendId) {
        log.info("UserController addFriend: запрос от ID {} на добавление друга с ID {}", id, friendId);
        userService.addFriend(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void removeFriend(@NotNull @PathVariable long id,
                             @NotNull @PathVariable long friendId) {
        log.info("UserController removeFriend: запрос от ID {} на удаление друга с ID {}", id, friendId);
        userService.removeFriend(id, friendId);
        log.info("UserController removeFriend: удален у user ID {} друг с ID {}", id, friendId);
    }

    @PostMapping
    public User create(@Valid @RequestBody final User user) {
        log.info("User {} create запрос в контроллере", user);
        User created = userService.create(user);
        log.info("User {} create результат в контроллере", created);
        return created;
    }

    @PutMapping
    public User update(@Valid @RequestBody final User user) {
        log.info("User {} обновляется в контроллере", user);
        User updated = userService.update(user);
        log.info("User {} обновлен в контроллере", updated);
        return updated;
    }
}
