package com.proj.test.salarymanagement.service;

import com.proj.test.salarymanagement.dto.BaseValidation;
import com.proj.test.salarymanagement.dto.ValidationRule;

import java.util.List;

public interface DefaultValidationService<T extends BaseValidation> {
    default void validateDefault(List<T> validationDtoList,List<ValidationRule> validationRuleList){

    }
    default void validateDefault(T validationDto, List<ValidationRule> validationRuleList){

    }
}
