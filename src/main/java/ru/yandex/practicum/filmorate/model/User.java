package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.yandex.practicum.filmorate.validator.NoSpaces;

import javax.validation.constraints.*;
import java.time.LocalDate;

//Кажется я стану собачником к концу обучения
@Data
@Builder
public class User {
    @EqualsAndHashCode.Include
    private int id;
    @NotNull
    @Email
    @NotBlank
    @EqualsAndHashCode.Exclude
    private String email;
    @NotNull
    @NotBlank
    @NoSpaces
    @EqualsAndHashCode.Exclude
    private String login;
    @EqualsAndHashCode.Exclude
    private String name;
    @PastOrPresent
    @EqualsAndHashCode.Exclude
    private LocalDate birthday;
}
