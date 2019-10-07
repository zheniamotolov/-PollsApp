package com.example.poll.exception;

import com.example.poll.payload.ApiResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestResponseEntityExceptionHandler
        extends ResponseEntityExceptionHandler {

//    @ExceptionHandler({AccessDeniedException.class})
//    public ResponseEntity<Object> handleAccessDeniedException(AccessDeniedException ex, WebRequest request) {
////        return new ResponseEntity("keeks", new HttpHeaders(), HttpStatus.UNAUTHORIZED);
//        return new ResponseEntity(new ApiResponse(false, "keeks"), HttpStatus.FORBIDDEN);
//    }


}
