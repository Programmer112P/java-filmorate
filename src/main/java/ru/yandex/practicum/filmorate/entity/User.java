package ru.yandex.practicum.filmorate.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.yandex.practicum.filmorate.validator.NoSpaces;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.Set;

@Data
@Builder
@EqualsAndHashCode(of = "id")
public class User {
    @JsonIgnore// Когда приходит новый пользователь с null, null в поле и устанавливается
    private Set<Long> friends;
    private long id;
    @Email
    @NotBlank
    private String email;
    @NotBlank
    @NoSpaces
    private String login;
    private String name;
    @PastOrPresent
    private LocalDate birthday;

    public boolean addFriend(long friendId) {
        return friends.add(friendId);
    }

    public boolean removeFriend(long friendId) {
        return friends.remove(friendId);
    }
}
