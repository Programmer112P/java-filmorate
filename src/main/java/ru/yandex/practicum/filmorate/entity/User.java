package ru.yandex.practicum.filmorate.entity;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.yandex.practicum.filmorate.validator.NoSpaces;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
public class User {
    @EqualsAndHashCode.Exclude
    private final Set<Long> friends = new HashSet<>();//Почему это поле не создается в @Data????
    @EqualsAndHashCode.Include
    private long id;
    @Email
    @NotBlank
    @EqualsAndHashCode.Exclude
    private String email;
    @NotBlank
    @NoSpaces
    @EqualsAndHashCode.Exclude
    private String login;
    @EqualsAndHashCode.Exclude
    private String name;
    @PastOrPresent
    @EqualsAndHashCode.Exclude
    private LocalDate birthday;

    public boolean addFriend(long friendId) {
        return friends.add(friendId);
    }

    public boolean removeFriend(long friendId) {
        return friends.remove(friendId);
    }
}
