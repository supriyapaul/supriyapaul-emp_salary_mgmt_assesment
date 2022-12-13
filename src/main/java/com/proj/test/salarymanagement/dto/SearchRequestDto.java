package com.proj.test.salarymanagement.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel
public class SearchRequestDto extends AbstractRequest{
    @ApiModelProperty(notes = "Min Salary", required = true)
    private double minSalary;
    @ApiModelProperty(notes = "Max Salary", required = true)
    private double maxSalary;
    @ApiModelProperty(notes = "Offset", required = true)
    private int offset;
    @ApiModelProperty(notes = "Limit", required = true)
    private int limit;
    @ApiModelProperty(notes = "Product ID")
    private List<SortParam> sortParams;
    @ApiModelProperty(notes = "Product ID")
    private List<SearchFilter> filters;
}
