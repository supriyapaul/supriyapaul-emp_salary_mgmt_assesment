package com.proj.test.salarymanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchRequestValidationDto extends BaseValidation{
    private Double minSalary;
    private Double maxSalary;
    private int offset;
    private int limit;
    private List<SortParam> sortParams;
    private List<SearchFilter> filters;
}
