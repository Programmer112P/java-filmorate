package ru.yandex.practicum.filmorate.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class StringSpacesValidator implements ConstraintValidator<NoSpaces, String> {
    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if (!(s == null)) {
            return !s.contains("\\s");
        }
        return true;
    }

    @Override
    public void initialize(NoSpaces constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }
}
