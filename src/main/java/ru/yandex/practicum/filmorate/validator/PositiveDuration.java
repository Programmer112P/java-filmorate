package ru.yandex.practicum.filmorate.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = FilmDurationValidator.class)
@Documented
public @interface PositiveDuration {
    String message() default "{releaseDate.invalid}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default { };
}
