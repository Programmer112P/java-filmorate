package ru.yandex.practicum.filmorate.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class StringSpacesValidator implements ConstraintValidator<NoSpaces, String> {
    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        return !s.contains("\\s");
    }

    @Override
    public void initialize(NoSpaces constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }
}
