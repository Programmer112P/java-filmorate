package ru.yandex.practicum.filmorate.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.yandex.practicum.filmorate.validator.NoSpaces;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class User {
    private Long id;
    @Email
    @NotBlank
    private String email;
    @NotBlank
    @NoSpaces
    private String login;
    private String name;
    @PastOrPresent
    private LocalDate birthday;


    public Map<String, Object> toMap() {
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("user_email", email);
        userMap.put("user_login", login);
        userMap.put("user_name", name);
        userMap.put("user_birthday", birthday);
        return userMap;
    }
}
