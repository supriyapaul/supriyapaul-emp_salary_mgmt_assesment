package com.proj.test.salarymanagement.advice;

import com.proj.test.salarymanagement.dto.ResponseDto;
import com.proj.test.salarymanagement.exception.UserException;
import com.proj.test.salarymanagement.exception.ValidationException;
import com.proj.test.salarymanagement.service.impl.UserServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import static com.proj.test.salarymanagement.utils.Constants.CODE_400;
import static com.proj.test.salarymanagement.utils.Constants.CODE_500;

@ControllerAdvice
public class ErrorHandler extends ResponseEntityExceptionHandler {

    final Logger logger = LoggerFactory.getLogger(ErrorHandler.class);

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ResponseDto> handleValidationException(ValidationException ex, WebRequest webRequest){
        final ResponseDto responseDto = new ResponseDto();
        responseDto.setResponseStatus(CODE_400);
        responseDto.setMessage(ex.getMessage());
        return ResponseEntity.ok(responseDto);
    }

    @ExceptionHandler(UserException.class)
    public ResponseEntity<ResponseDto> handleUserException(UserException ex, WebRequest webRequest){
        final ResponseDto responseDto = new ResponseDto();
        responseDto.setResponseStatus(CODE_500);
        responseDto.setMessage(ex.getMessage());
        return ResponseEntity.ok(responseDto);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseDto> handleUserException(Exception ex, WebRequest webRequest){
        logger.error("Exception occurred : {}" , ex.getMessage());
        final ResponseDto responseDto = new ResponseDto();
        responseDto.setResponseStatus(CODE_500);
        responseDto.setMessage(ex.getMessage());
        return ResponseEntity.ok(responseDto);
    }
}
