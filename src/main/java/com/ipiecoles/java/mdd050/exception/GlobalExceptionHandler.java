package com.ipiecoles.java.mdd050.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleEntityNotFoundException(
            EntityNotFoundException entityNotFoundException) {
       return entityNotFoundException.getMessage();
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleMATME(MethodArgumentTypeMismatchException e){
        return "La valeur " + e.getValue() + " est incorrecte pour le paramètre "
                + e.getName();
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handlIllegalArgumentException(
            IllegalArgumentException e){
        return e.getMessage();
    }

    @ExceptionHandler(EntityExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public String handlEntityExistsException(
            EntityExistsException e){
        return e.getMessage();
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public String handlHttpMessageNotReadableException(
            HttpMessageNotReadableException e){
        return e.getMessage();
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleOtherException(Exception e){
        return "Un problème technique est survenu.";
    }
}
