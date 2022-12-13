package com.proj.test.salarymanagement.service;

import com.proj.test.salarymanagement.dto.AbstractRequest;
import com.proj.test.salarymanagement.dto.BaseValidation;
import com.proj.test.salarymanagement.dto.ValidationRule;

import java.util.List;

public interface ValidationService<R extends AbstractRequest> {
    default void validate(List<R> inputList, List<ValidationRule> validationRules){

    }
     default void validate(R input, List<ValidationRule> validationRules){

    }

}
