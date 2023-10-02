package com.task.user.controllers;

import com.task.user.exceptions.UserNotAcceptableAge;
import com.task.user.exceptions.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ControllerAdvice
public class UserExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<?> handleUserNotFoundException(UserNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(UserNotAcceptableAge.class)
    public ResponseEntity<?> handleUserNotAcceptableAge(UserNotAcceptableAge ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

}
