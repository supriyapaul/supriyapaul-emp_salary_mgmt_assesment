package com.proj.test.salarymanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRecordValidationDto extends BaseValidation{
    private String id;
    private String login;
    private String name;
    private Double salary;
    private String startDate;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        UserRecordValidationDto that = (UserRecordValidationDto) o;
        return id.equals(that.id) && login.equals(that.login);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id, login);
    }

    public boolean isEmpty(){
        return Objects.isNull(id) || Objects.isNull(login) || Objects.isNull(name)
                || Objects.isNull(salary) || Objects.isNull(startDate);
    }
}
