package com.sawah.sawah_backend.exceptions;

import com.sawah.sawah_backend.response.ApiResponse;
import lombok.AllArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@RestControllerAdvice
@AllArgsConstructor
public class GlobalExceptionHandler  {

    private final MessageSource messageSource;

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse> resourceNotFoundException(ResourceNotFoundException e , Locale locale){

        String errorMessage = translate(e.getMessage(),locale);

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiResponse(errorMessage, null, LocalDateTime.now()));

    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiResponse> handleGeneric(AuthenticationException e, Locale locale){

        String errorMessage = translate("auth.failed",locale);

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ApiResponse(errorMessage, null, LocalDateTime.now()));

    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse> handleGeneric(Exception e, Locale locale){

        String errorMessage = translate(e.getMessage(),locale);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse(errorMessage, null, LocalDateTime.now()));

    }




    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationException(MethodArgumentNotValidException  validationException , Locale locale){
        Map<String,String> responseMessages = new HashMap<>();

        validationException.getBindingResult().getFieldErrors()
                .forEach(error -> responseMessages.put(
                        error.getField(),
                        translate(error.getDefaultMessage(),locale))
                );

        String primaryMessage = translate("common.validation.error",locale);


        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponse(primaryMessage, responseMessages, LocalDateTime.now()));

    }


    private String translate(String messageKey, Locale locale) {
        try {
            return messageSource.getMessage(messageKey, null, locale);
        } catch (NoSuchMessageException ex) {
            return messageKey;
        }
    }





}
