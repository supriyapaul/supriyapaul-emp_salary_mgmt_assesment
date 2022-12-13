package com.proj.test.salarymanagement.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel
public class SortParam {
    @ApiModelProperty(notes = "Name", required = true)
    private String name;
    @ApiModelProperty(notes = "Order", required = true)
    private String order;
}
