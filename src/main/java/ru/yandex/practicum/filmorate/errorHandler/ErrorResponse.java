package ru.yandex.practicum.filmorate.errorHandler;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class ErrorResponse {
    private final String description;
}
