package com.proj.test.salarymanagement.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@ApiModel
public class SearchFilter {
    @ApiModelProperty(notes = "Name", required = true)
    private String filterName;
    @ApiModelProperty(notes = "Values")
    private List<String> filterValues;
    @ApiModelProperty(notes = "Value1")
    private String filterValue1;
    @ApiModelProperty(notes = "Value2")
    private String filterValue2;
    @ApiModelProperty(notes = "Operation", required = true)
    private String filterOperation;

    public SearchFilter(String filterName,String filterOperation,String filterValue1,String filterValue2){
        this.filterName = filterName;
        this.filterOperation = filterOperation;
        this.filterValue1 = filterValue1;
        this.filterValue2 = filterValue2;
    }

    public SearchFilter(String filterName, String filterOperation, String filterValue1) {
        this.filterName = filterName;
        this.filterOperation = filterOperation;
        this.filterValue1 = filterValue1;
    }
}
