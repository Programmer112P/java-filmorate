package ru.yandex.practicum.filmorate.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;

@Data
@Builder
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Genre {
    private Long id;
    @NotBlank
    private String name;
}
