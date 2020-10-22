package com.nokia.task;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/* To handle OutOfMemory Exceptions thrown by Add Method */
@ControllerAdvice
public class TaskExceptionHandler {
    
    @ExceptionHandler(value = {OutOfMemoryError.class})
    public ResponseEntity<Object> handleOutOfMemoryException(OutOfMemoryError e)
    {
        return new ResponseEntity<>(false,HttpStatus.OK);
    }
}
