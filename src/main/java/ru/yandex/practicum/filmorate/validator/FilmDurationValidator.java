package ru.yandex.practicum.filmorate.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.Duration;

public class FilmDurationValidator implements ConstraintValidator<PositiveDuration, Duration> {
    @Override
    public boolean isValid(Duration duration, ConstraintValidatorContext constraintValidatorContext) {
        if (duration != null) {
            return !duration.isNegative();
        }
        return true;
    }

    @Override
    public void initialize(PositiveDuration constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }
}
