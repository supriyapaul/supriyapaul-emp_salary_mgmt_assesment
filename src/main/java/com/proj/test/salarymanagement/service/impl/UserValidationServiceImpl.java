package com.proj.test.salarymanagement.service.impl;

import com.proj.test.salarymanagement.dto.UserRecordDto;
import com.proj.test.salarymanagement.dto.UserRecordValidationDto;
import com.proj.test.salarymanagement.dto.ValidationRule;
import com.proj.test.salarymanagement.exception.ValidationException;
import com.proj.test.salarymanagement.service.AbstractUserValidationService;
import com.proj.test.salarymanagement.service.UserValidationService;
import org.apache.commons.validator.GenericValidator;
import org.springframework.beans.BeanUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

public class UserValidationServiceImpl extends AbstractUserValidationService implements UserValidationService{
    @Override
    public void validate(final List<UserRecordDto> recordDtoList, final List<ValidationRule> validationRules) {
        final List<UserRecordValidationDto> userRecordValidationDtos = this.mapToUserRecordValidationDtoList(recordDtoList);
        super.validateDefault(userRecordValidationDtos,validationRules);
        // Validation for Duplicate ID's/login
        this.applyOtherValidation(userRecordValidationDtos,validationRules);

        // we can apply other validation applicable for each row, If needed
        userRecordValidationDtos.stream().forEach(userRecordValidationDto -> {
            this.applyValidation(userRecordValidationDto,validationRules);
        });
    }

    private void applyOtherValidation(final List<UserRecordValidationDto> userRecordValidationDtos, final List<ValidationRule> validationRules){
        validationRules.stream().filter(validationRule -> Objects.nonNull(validationRule.getRuleName())).forEach(validationRule -> {
            switch (validationRule.getRuleName()){
                case "unique":
                    this.checkUniqueFields(userRecordValidationDtos);
                    break;
            }
        });
    }
    private void applyValidation(final UserRecordValidationDto userRecordValidationDto, final List<ValidationRule> validationRules){
        validationRules.stream().filter(validationRule -> Objects.nonNull(validationRule.getRuleName())).forEach(validationRule -> {
            switch (validationRule.getRuleName()){
                case "date_format":
                    final List<String> validDateFormats = Arrays.asList("yyyy-mm-dd","dd-MMM-yy");
                    this.checkStartDate(userRecordValidationDto,validDateFormats);
                    break;
            }
        });
    }
    @Override
    public void validate(final UserRecordDto userRecordDto, final List<ValidationRule> validationRules){
        final UserRecordValidationDto userRecordValidationDto = mapToUserRecordValidationDto(userRecordDto);
        super.validateDefault(userRecordValidationDto,validationRules);
        this.applyValidation(userRecordValidationDto,validationRules);
        this.applyOtherValidation(Arrays.asList(userRecordValidationDto),validationRules);
    }

    private UserRecordValidationDto mapToUserRecordValidationDto(final UserRecordDto recordDto){
        final UserRecordValidationDto userRecordValidationDto = new UserRecordValidationDto();
        BeanUtils.copyProperties(recordDto,userRecordValidationDto);
        return userRecordValidationDto;
    }

    private void checkStartDate(final UserRecordValidationDto recordDto, final List<String> validDateFormats){
        int count = 0;
            for (String validDateFormat : validDateFormats) {
                if(!GenericValidator.isDate(recordDto.getStartDate(), validDateFormat, true))
                    count ++;
            }
        if(count == validDateFormats.size())
           throw new ValidationException("Date of start of employment in one of two formats yyyy-mm-dd or dd-mmm-yy");
    }
    private List<UserRecordValidationDto> mapToUserRecordValidationDtoList(final List<UserRecordDto> recordDtoList){
        // Create a Copy for Validation
        final List<UserRecordValidationDto> userRecordValidationDtos = recordDtoList.stream().map(userRecordDto -> {
            final UserRecordValidationDto userRecordValidationDto = new UserRecordValidationDto();
            BeanUtils.copyProperties(userRecordDto,userRecordValidationDto);
            return userRecordValidationDto;
        }).collect(Collectors.toList());
        return userRecordValidationDtos;
    }
    private void checkUniqueFields(final List<UserRecordValidationDto> userRecordValidationDtos){
        // Currently I have handled Unique Constraint Validation overriding equal method but for
        // other fields we can have custom logic separately.
        final List<String> duplicateIdList =  userRecordValidationDtos.stream()
                .collect(Collectors.groupingBy(Function.identity()
                        , Collectors.counting()))
                .entrySet().stream()
                .filter(m -> m.getValue() > 1)
                .map(Map.Entry::getKey)
                .map(userRecordValidationDto -> userRecordValidationDto.getId())
                .collect(Collectors.toList());
        if(Objects.nonNull(duplicateIdList) && !duplicateIdList.isEmpty()){
            throw new ValidationException("Input file contains Duplicate ID/login " + duplicateIdList);
        }
    }
}
