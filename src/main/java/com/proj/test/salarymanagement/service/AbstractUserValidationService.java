package com.proj.test.salarymanagement.service;

import com.proj.test.salarymanagement.dto.UserRecordValidationDto;
import com.proj.test.salarymanagement.dto.ValidationRule;
import com.proj.test.salarymanagement.exception.UserException;
import com.proj.test.salarymanagement.exception.ValidationException;
import com.proj.test.salarymanagement.service.DefaultValidationService;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Objects;

public abstract class AbstractUserValidationService  implements DefaultValidationService<UserRecordValidationDto> {

    @Override
    public void validateDefault(List<UserRecordValidationDto> validationDtoList, List<ValidationRule> validationRuleList){
        validationDtoList.stream().forEach(userRecordValidationDto -> {
            this.validateDefault(userRecordValidationDto,validationRuleList);
        });

    }
    // We can add more common functionalities like these such lesser than(lt) , lesser than equal to (lteq), range(bw) etc.
    // For now, I have only added for greater than equal (gteq)
    @Override
    public void validateDefault(UserRecordValidationDto validationDto, List<ValidationRule> validationRuleList){
        validationRuleList.stream().forEach(validationRule -> {
            checkCommonOperation(validationRule,validationDto);
            checkOtherCommonOperation(validationRule,validationDto);
        });
    }

    private void checkOtherCommonOperation(final ValidationRule validationRule, final UserRecordValidationDto validationDto){
        if(Objects.nonNull(validationRule.getRuleName())) {
            switch (validationRule.getRuleName()) {
                case "notBlank":
                    if (validationRule.getFieldName().equals("all")) {
                        if (validationDto.isEmpty())
                            throw new ValidationException("All columns must not be blank");
                    }
                    break;
            }
        }
    }

    private void checkCommonOperation(final ValidationRule validationRule, final UserRecordValidationDto validationDto){
        if(Objects.nonNull(validationRule.getOperation())) {
            switch (validationRule.getOperation()) {
                case "gteq":
                    try {
                        final String name = StringUtils.capitalize(validationRule.getFieldName());
                        final Method method = UserRecordValidationDto.class.getDeclaredMethod("get" + name);
                        final Class returnType = method.getReturnType();
                        if (returnType.getName().contains("Double")) {
                            Double operand = (Double) method.invoke(validationDto);
                            Double rightOperand = Double.parseDouble(validationRule.getRightOperand());
                            if (operand < rightOperand)
                                throw new ValidationException(name + "must me greater than" + validationRule.getRightOperand());
                        }
                    } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException ex) {
                        throw new UserException(" Calling Method using reflection failed ..Please check logs.");
                    }
                    break;
            }
        }
    }
}
