package com.example.my_todo_application.task_management.controller.validator;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;

public class DateTimeBetweenValidator implements ConstraintValidator<DateTimeBetween, Object> {

    private String startField;

    private String endField;

    private String message;

    @Override
    public void initialize(DateTimeBetween constraintAnnotation) {
        startField = constraintAnnotation.startField();
        endField = constraintAnnotation.endField();
        message = constraintAnnotation.message();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        BeanWrapper beanWrapper = new BeanWrapperImpl(value);
        LocalDateTime startTime = (LocalDateTime) beanWrapper.getPropertyValue(startField);
        LocalDateTime endTime = (LocalDateTime) beanWrapper.getPropertyValue(endField);

        return startTime.isBefore(endTime);

    }
}
