package com.mrn.mobileappws.exceptions;

import com.mrn.mobileappws.ui.model.response.ErrorMessage;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;

@ControllerAdvice
public class AppExceptionsHandler {

    @ExceptionHandler(value = {UserServiceException.class,})
    public ResponseEntity<Object> handlerUserServiceException(UserServiceException exc, WebRequest request) {

        ErrorMessage errorMessage = new ErrorMessage(new Date(), exc.getMessage());

        return new ResponseEntity<>(errorMessage, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = {Exception.class,})
    public ResponseEntity<Object> handleOtherException(Exception exc, WebRequest request) {

        ErrorMessage errorMessage = new ErrorMessage(new Date(), exc.getMessage());

        return new ResponseEntity<>(errorMessage, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
