package ru.yandex.practicum.filmorate.entity;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.yandex.practicum.filmorate.validator.DateAfter;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
@Builder
public class Film {
    @EqualsAndHashCode.Include
    private long id;
    @NotBlank
    @EqualsAndHashCode.Exclude
    private String name;
    @Size(max = 200)
    @EqualsAndHashCode.Exclude
    private String description;
    @NotNull
    @DateAfter(date = "1895-12-27")//Надо в правильном формате вводить дату
    @EqualsAndHashCode.Exclude
    private LocalDate releaseDate;
    @Positive
    @NotNull
    @EqualsAndHashCode.Exclude
    private long duration;
}
