package ru.yandex.practicum.filmorate.dao.mock;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.entity.User;

import java.util.*;

@Component
public class UserRepository {
    private final Map<Long, User> storage = new HashMap<>();
    private long generatorId = 1;

    public List<User> getAllUsers() {
        return new ArrayList<>(storage.values());
    }

    public User createUser(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        user.setId(generatorId++);
        storage.put(user.getId(), user);
        return user;
    }

    public Optional<User> updateUser(User updatedUser) {
        User oldUser = storage.get(updatedUser.getId());
        if (oldUser == null) {
            return Optional.empty();
        }
        if (updatedUser.getName() == null || updatedUser.getName().isBlank()) {
            updatedUser.setName(updatedUser.getLogin());
        }
        storage.put(updatedUser.getId(), updatedUser);
        return Optional.of(updatedUser);
    }

    public Optional<User> getUserById(long id) {
        User user = storage.get(id);
        if (user == null) {
            return Optional.empty();
        }
        return Optional.of(user);
    }
}
