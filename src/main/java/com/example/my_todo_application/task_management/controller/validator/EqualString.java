package com.example.my_todo_application.task_management.controller.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = { EqualStringValidator.class })
@Target({ TYPE, ANNOTATION_TYPE, TYPE_USE })
@Retention(RUNTIME)
@Repeatable(EqualString.List.class)
public @interface EqualString {

    String message() default "";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    /** 検証する対象 */
    String check();

    /** 検証の値 */
    String target();

    @Target({ TYPE, ANNOTATION_TYPE, TYPE_USE })
    @Retention(RUNTIME)
    @Documented
    @interface List {

        EqualString[] value();
    }
}
