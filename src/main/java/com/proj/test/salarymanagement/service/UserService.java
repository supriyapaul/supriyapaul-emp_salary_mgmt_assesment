package com.proj.test.salarymanagement.service;

import com.proj.test.salarymanagement.dto.ResponseDto;
import com.proj.test.salarymanagement.dto.SearchRequestDto;
import com.proj.test.salarymanagement.dto.UserRecordDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface UserService {
    ResponseDto upload(MultipartFile file) throws IOException;
    ResponseDto create(UserRecordDto recordDto);
    ResponseDto fetch(SearchRequestDto searchRequestDto);
    ResponseDto update(UserRecordDto userRecordDto);
    ResponseDto delete(String id);
}
