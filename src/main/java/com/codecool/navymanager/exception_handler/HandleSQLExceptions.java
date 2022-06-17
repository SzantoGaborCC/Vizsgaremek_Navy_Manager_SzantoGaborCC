package com.codecool.navymanager.exception_handler;

import com.codecool.navymanager.response.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.sql.SQLException;

@ControllerAdvice
public class HandleSQLExceptions {
    @ExceptionHandler({SQLException.class})
    public ResponseEntity<?> handleException(Exception e) {
        Response response = new Response();
        response.setErrorDescription(e.getMessage());
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(response);
    }
}

