package com.proj.test.salarymanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class ValidationRule {
    private String fieldName;

    private List<String> fieldNames;
    private String ruleName;
    private String operation;
    private String leftOperand;
    private String rightOperand;

    public ValidationRule(String fieldName,String ruleName){
        this.fieldName = fieldName;
        this.ruleName = ruleName;
    }

    public ValidationRule(List<String> fieldNames,String ruleName){
        this.fieldNames = new ArrayList<>();
        this.fieldNames.addAll(fieldNames);
        this.ruleName = ruleName;
    }
    public ValidationRule(String fieldName, String operation, String rightOperand) {
        this.fieldName = fieldName;
        this.ruleName = ruleName;
        this.operation = operation;
        this.rightOperand = rightOperand;
    }

    public ValidationRule(String fieldName, String operation, String leftOperand, String rightOperand) {
        this.fieldName = fieldName;
        this.operation = operation;
        this.leftOperand = leftOperand;
        this.rightOperand = rightOperand;
    }
}
