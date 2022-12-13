package com.proj.test.salarymanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponseDto extends ResponseBody{
    private String id;
    private String login;
    private String name;
    private Double salary;
    private String startDate;
}
