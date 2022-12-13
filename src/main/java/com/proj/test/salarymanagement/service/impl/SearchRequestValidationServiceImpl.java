package com.proj.test.salarymanagement.service.impl;

import com.proj.test.salarymanagement.dto.SearchRequestDto;
import com.proj.test.salarymanagement.dto.SearchRequestValidationDto;
import com.proj.test.salarymanagement.dto.ValidationRule;
import com.proj.test.salarymanagement.exception.ValidationException;
import com.proj.test.salarymanagement.service.AbstractSearchRequestValidationService;
import com.proj.test.salarymanagement.service.SearchRequestValidationService;
import org.springframework.beans.BeanUtils;

import java.util.List;

public class SearchRequestValidationServiceImpl extends AbstractSearchRequestValidationService implements SearchRequestValidationService {
    @Override
    public void validate(SearchRequestDto input, List<ValidationRule> validationRules) {
        validationRules.stream().forEach(validationRule -> {
            switch (validationRule.getRuleName()){
                case "salary":
                    if(input.getMaxSalary() < 0 || input.getMinSalary() < 0)
                        throw new ValidationException("Salary can not be negative");
                    if(input.getMaxSalary() == input.getMinSalary())
                        throw new ValidationException("Maximum Salary can not be same as Minimum Salary");
                    if(input.getMaxSalary() < input.getMinSalary())
                        throw new ValidationException("Maximum Salary can not be less than Minimum Salary");
            }
        });
    }

    private SearchRequestValidationDto maptoSearchRequestValidationDto(final SearchRequestDto searchRequestDto){
        final SearchRequestValidationDto searchRequestValidationDto = new SearchRequestValidationDto();
        BeanUtils.copyProperties(searchRequestDto,searchRequestValidationDto);
        return searchRequestValidationDto;
    }

}
