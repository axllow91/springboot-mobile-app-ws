package com.mrn.mobileappws.exceptions;

public class UserServiceException extends RuntimeException {

    public UserServiceException(String errorMessage) {
        super(errorMessage);
    }
}
