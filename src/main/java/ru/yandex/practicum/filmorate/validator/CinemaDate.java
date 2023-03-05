package ru.yandex.practicum.filmorate.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = FilmReleaseDateValidator.class)
@Documented
public @interface CinemaDate {
    String message() default "{filmDuration.invalid}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default { };

}
