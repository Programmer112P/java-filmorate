package ru.yandex.practicum.filmorate.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.yandex.practicum.filmorate.validator.DateAfter;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.Set;

@Data
@Builder
@EqualsAndHashCode(of = "id")
public class Film {
    @JsonIgnore
    private Set<Long> usersLike;
    private long id;
    @NotBlank
    private String name;
    @Size(max = 200)
    private String description;
    @NotNull
    @DateAfter(date = "1895-12-27")//Надо в правильном формате вводить дату
    private LocalDate releaseDate;
    @Positive
    @NotNull
    private long duration;

    public boolean addLike(long userId) {
        return usersLike.add(userId);
    }

    public boolean removeLike(long userId) {
        return usersLike.remove(userId);
    }
}
