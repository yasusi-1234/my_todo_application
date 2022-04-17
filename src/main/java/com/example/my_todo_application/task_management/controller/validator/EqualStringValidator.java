package com.example.my_todo_application.task_management.controller.validator;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.util.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Objects;

public class EqualStringValidator implements ConstraintValidator<EqualString, Object> {
    /** 検証する対象 */
    private String target;
    /** 検証の値 */
    private String check;

    private String message;
    @Override
    public void initialize(EqualString constraintAnnotation) {
        target = constraintAnnotation.target();
        check = constraintAnnotation.check();
        message = constraintAnnotation.message();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        BeanWrapper beanWrapper = new BeanWrapperImpl(value);
        String targetStr = (String) beanWrapper.getPropertyValue(target);
        String checkStr = (String) beanWrapper.getPropertyValue(check);

        context.buildConstraintViolationWithTemplate(message)
                .addPropertyNode(target).addConstraintViolation();

        boolean through = StringUtils.hasText(targetStr) && StringUtils.hasText(checkStr);

        if(through) {
            return Objects.equals(targetStr, checkStr);
        }else {
            return true;
        }
    }
}
