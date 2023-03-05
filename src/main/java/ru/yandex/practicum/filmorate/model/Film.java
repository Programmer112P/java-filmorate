package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.yandex.practicum.filmorate.validator.CinemaDate;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
@Builder
public class Film {
    @EqualsAndHashCode.Include
    private int id;
    @NotNull
    @NotBlank
    @EqualsAndHashCode.Exclude
    private String name;
    @Size(max = 200)
    @EqualsAndHashCode.Exclude
    private String description;
    @CinemaDate
    @EqualsAndHashCode.Exclude
    private LocalDate releaseDate;
    @Positive
    @EqualsAndHashCode.Exclude
    private int duration;//В тестах это видимо целочисленный тип, а не Duration, а я уже свою аннотацию написал
}
