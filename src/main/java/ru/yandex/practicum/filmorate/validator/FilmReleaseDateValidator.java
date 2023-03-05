package ru.yandex.practicum.filmorate.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class FilmReleaseDateValidator implements ConstraintValidator<CinemaDate, LocalDate> {
    @Override
    public void initialize(CinemaDate constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(LocalDate localDate, ConstraintValidatorContext constraintValidatorContext) {
        if (!(localDate == null)) {
            return localDate.isAfter(LocalDate.of(1895, 12, 27));
        }
        return true;
    }

}
