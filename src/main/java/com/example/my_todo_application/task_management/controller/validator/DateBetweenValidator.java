package com.example.my_todo_application.task_management.controller.validator;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class DateBetweenValidator implements ConstraintValidator<DateBetween, Object> {

    /** 開始日付のフィールド名 */
    private String endField;
    /** 終了日付のフィールド名 */
    private String startField;
    /** エラーメッセージ */
    private String message;
    /** フォームの形式 */
    private FormAction formType;

    @Override
    public void initialize(DateBetween constraintAnnotation) {
        startField = constraintAnnotation.startField();
        endField = constraintAnnotation.endField();
        formType = constraintAnnotation.formType();
        message = constraintAnnotation.message();

    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        BeanWrapper beanWrapper = new BeanWrapperImpl(value);
        LocalDate startDate = (LocalDate) beanWrapper.getPropertyValue(startField);
        LocalDate endDate = (LocalDate) beanWrapper.getPropertyValue(endField);

        context.buildConstraintViolationWithTemplate(message)
                .addPropertyNode(startField).addConstraintViolation();

        switch(formType){
            case REGISTER:
                return startDate != null && endDate != null && startDate.isBefore(endDate);
            case READ:
                return (startDate == null || endDate == null) || startDate.isBefore(endDate);
            default:
                return false;
        }

    }
}
