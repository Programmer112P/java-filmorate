package ru.yandex.practicum.filmorate.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class AfterDateValidator implements ConstraintValidator<DateAfter, LocalDate> {
    private String date;

    @Override
    public void initialize(DateAfter constraintAnnotation) {
        this.date = constraintAnnotation.date();
    }

    @Override
    public boolean isValid(LocalDate localDate, ConstraintValidatorContext constraintValidatorContext) {
        if (!(localDate == null)) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate dateBefore = LocalDate.parse(date, formatter);
            return localDate.isAfter(dateBefore);
        }
        return true;
    }
}
