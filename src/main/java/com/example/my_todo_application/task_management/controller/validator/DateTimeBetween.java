package com.example.my_todo_application.task_management.controller.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import com.example.my_todo_application.task_management.controller.validator.DateTimeBetween.List;

@Documented
@Constraint(validatedBy = { DateTimeBetweenValidator.class })
@Target({ TYPE, ANNOTATION_TYPE, TYPE_USE })
@Retention(RUNTIME)
@Repeatable(List.class)
public @interface DateTimeBetween {

    String message() default "";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    /**
     * startField name
     */
    String startField();

    /**
     * endField name
     */
    String endField();

    @Target({ TYPE, ANNOTATION_TYPE, TYPE_USE })
    @Retention(RUNTIME)
    @Documented
    @interface List {
        DateTimeBetween[] value();
    }
}
