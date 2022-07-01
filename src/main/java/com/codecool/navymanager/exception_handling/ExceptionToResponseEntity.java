package com.codecool.navymanager.exception_handling;

import com.codecool.navymanager.response.JsonResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.sql.SQLException;
import java.util.NoSuchElementException;

@ControllerAdvice
public class ExceptionToResponseEntity {
    @ExceptionHandler({SQLException.class})
    public ResponseEntity<?> handleSqlException(Exception e) {
        JsonResponse jsonResponse = JsonResponse.builder().build();
        jsonResponse.setErrorDescription(e.getMessage());
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(jsonResponse);
    }

    @ExceptionHandler({NoSuchElementException.class, IllegalArgumentException.class})
    public ResponseEntity<?> handleInvalidIds(Exception e) {
        JsonResponse jsonResponse = JsonResponse.builder().build();
        jsonResponse.setErrorDescription(e.getMessage());
        return ResponseEntity
                .status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(jsonResponse);
    }

  @ExceptionHandler({HttpMessageNotReadableException.class})
  public ResponseEntity<?> handleJsonSerializationErrors(Exception e) {
      JsonResponse jsonResponse = JsonResponse.builder().build();
      jsonResponse.setErrorDescription(e.getMessage());
      return ResponseEntity
              .status(HttpStatus.BAD_REQUEST)
              .body(jsonResponse);
  }
}

