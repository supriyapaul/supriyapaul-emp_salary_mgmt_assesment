package com.proj.test.salarymanagement.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.proj.test.salarymanagement.dto.ResponseDto;
import com.proj.test.salarymanagement.dto.SearchRequestDto;
import com.proj.test.salarymanagement.dto.UserRecordDto;
import com.proj.test.salarymanagement.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import javafx.application.Application;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Objects;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping(value = "/upload")
    public ResponseEntity<ResponseDto> upload(@RequestParam("file") MultipartFile file) throws IOException {
        final ResponseDto uploadResponseDto = userService.upload(file);
        return ResponseEntity.ok(uploadResponseDto);
    }

    @PostMapping
    public ResponseEntity<ResponseDto> create(@RequestBody UserRecordDto recordDto){
        final ResponseDto responseDto = userService.create(recordDto);
        return ResponseEntity.ok(responseDto);
    }

    @PutMapping
    public ResponseEntity<ResponseDto> update(@RequestBody UserRecordDto recordDto){
        final ResponseDto responseDto = userService.update(recordDto);
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping()
    public ResponseEntity<ResponseDto> fetch(@RequestParam String searchRequestDto) throws JsonProcessingException {
        final ObjectMapper Obj = new ObjectMapper();
        final SearchRequestDto searchRequestDtoNew = Objects.isNull(searchRequestDto) || "".equals(searchRequestDto) ? new SearchRequestDto() : Obj.readValue(searchRequestDto,SearchRequestDto.class);
        final ResponseDto responseDto = userService.fetch(searchRequestDtoNew);
        return ResponseEntity.ok(responseDto);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<ResponseDto> delete(@PathVariable String id){
        final ResponseDto responseDto = userService.delete(id);
        return ResponseEntity.ok(responseDto);
    }
}
