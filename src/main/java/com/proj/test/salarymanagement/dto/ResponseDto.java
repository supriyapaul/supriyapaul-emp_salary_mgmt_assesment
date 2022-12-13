package com.proj.test.salarymanagement.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseDto<T extends ResponseBody> extends BaseResponse{
    private List<T> results;
    private T result;

    public ResponseDto(){

    }
    public ResponseDto(String code, String message, List<T> results){
        super(code,message);
        this.results = new ArrayList<>();
        this.results.addAll(results);
    }

    public ResponseDto(String code, String message, T result){
        super(code,message);
        this.result = result;
    }

    public ResponseDto(String code, String message){
        super(code,message);
    }
}
