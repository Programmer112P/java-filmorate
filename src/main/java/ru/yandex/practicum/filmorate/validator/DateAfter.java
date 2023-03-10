package ru.yandex.practicum.filmorate.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = AfterDateValidator.class)
@Documented
public @interface DateAfter {

    String date();

    String message() default "The date is invalid";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default { };

}
