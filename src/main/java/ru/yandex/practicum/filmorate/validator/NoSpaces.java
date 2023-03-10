package ru.yandex.practicum.filmorate.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = StringSpacesValidator.class)
@Documented
public @interface NoSpaces {
    String message() default "The login is invalid";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
