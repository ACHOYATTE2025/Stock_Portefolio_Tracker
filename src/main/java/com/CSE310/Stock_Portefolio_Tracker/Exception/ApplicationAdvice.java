package com.CSE310.Stock_Portefolio_Tracker.Exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import com.CSE310.Stock_Portefolio_Tracker.Dto.ErroResponseDto;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class ApplicationAdvice {
    
    //manage all exception !!!!!
@ExceptionHandler(MethodArgumentNotValidException.class)
public ResponseEntity<ErroResponseDto> handleValidationException(
        MethodArgumentNotValidException ex,
        WebRequest request) {

    String message = ex.getBindingResult()
            .getFieldErrors()
            .stream()
            .map(err -> err.getField() + ": " + err.getDefaultMessage())
            .findFirst()
            .orElse("Validation error");

    ErroResponseDto errorDto = new ErroResponseDto(
            request.getDescription(false),
            HttpStatus.BAD_REQUEST,
            message,
            LocalDateTime.now()
    );

    return ResponseEntity.badRequest().body(errorDto);
}


}
