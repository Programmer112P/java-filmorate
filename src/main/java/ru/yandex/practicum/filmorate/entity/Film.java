package ru.yandex.practicum.filmorate.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.yandex.practicum.filmorate.validator.DateAfter;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Film {
    private Set<Genre> genres;
    private Long id;
    @NotNull
    private Mpa mpa;
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

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("mpa_id", mpa.getId());
        map.put("film_name", name);
        map.put("film_description", description);
        map.put("film_release_date", releaseDate);
        map.put("film_duration", duration);
        return map;
    }
}
