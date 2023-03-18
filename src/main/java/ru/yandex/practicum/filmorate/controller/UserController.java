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
import javax.validation.constraints.NotNull;
import java.util.*;

//Логирование занимает больше места, чем сама логика, может добавить LogBook?
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

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getCommonFriends(@NotNull @Min(1) @PathVariable long id,
                                       @NotNull @Min(1) @PathVariable long otherId) {
        log.info("Запрос в UserController на получение общих друзей user с ID {} и user с ID {}", id, otherId);
        try {
            List<User> commonFriends = userService.getCommonFriends(id, otherId);
            log.info("Получены общие друзья в UserController: user с ID {} и user с ID {}", id, otherId);
            return commonFriends;
        } catch (ServiceException e) {
            log.error("Ошибка при поиске общих друзей у user с ID {} и user с ID {}", id, otherId);
            throw new NotFoundException(
                    String.format("Ошибка при поиске общих друзей у user с ID %d и user с ID %d", id, otherId), e);
        }
    }

    @GetMapping("/{id}/friends")
    public List<User> getFriends(@NotNull @PathVariable long id) {
        log.info("Запрос на получение Friends от user с ID {} в контроллере", id);
        try {
            List<User> friends = userService.getFriends(id);
            log.info("Получены Friends от user с ID {} в контроллере", id);
            return friends;
        } catch (ServiceException e) {
            log.error("User с id {} не существует в UserService", id);
            throw new NotFoundException("Неверный id пользователя", e);
        }
    }

    @GetMapping("/{id}")
    public User getById(@NotNull @Min(1) @PathVariable long id) {
        log.info("Запрос на получение User c id {} в контроллере", id);
        try {
            User user = userService.getById(id);
            log.info("Получен User {} в контроллере", user);
            return user;
        } catch (ServiceException e) {
            log.error("User с id {} не существует в UserService", id);
            throw new NotFoundException("Неверный id пользователя", e);
        }

    }

    @PutMapping("/{id}/friends/{friendId}")
    public User addFriend(@NotNull @PathVariable long id,
                          @NotNull @PathVariable long friendId) {
        log.info("В UserController addFriend от ID {} на добавление друга с ID {}", id, friendId);
        try {
            return userService.addFriend(id, friendId);
        } catch (ServiceException e) {
            log.error("При addFriend произошла ошибка в UserService: {}", e.getMessage());
            throw new NotFoundException("При addFriend произошла ошибка в UserService", e);
        }
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public User removeFriend(@NotNull @PathVariable long id,
                             @NotNull @PathVariable long friendId) {
        log.info("В UserController запрос removeFriend от ID {} на удаление друга с ID {}", id, friendId);
        try {
            User user = userService.removeFriend(id, friendId);
            log.info("В UserController удален у user ID {} друг с ID {}", id, friendId);
            return user;
        } catch (ServiceException e) {
            log.error("При deleteFriend произошла ошибка в UserService: {}", e.getMessage());
            throw new NotFoundException("При removeFriend произошла ошибка в UserService", e);
        }
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
        try {
            User updated = userService.update(user);
            log.info("User {} обновлен в контроллере", updated);
            return updated;
        } catch (ServiceException e) {
            log.error("User с id {} не существует в UserService", user.getId());
            throw new NotFoundException("Неверный id пользователя", e);
        }
    }
}
