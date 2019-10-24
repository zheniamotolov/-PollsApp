package com.example.poll.exception;

import com.example.poll.model.CustomErrorMessage;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        CustomErrorMessage customErrorMessage = CustomErrorMessage.builder()
                .error(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .status(status.value())
                .build();
        return new ResponseEntity<>(customErrorMessage, status);
    }

    //    @ExceptionHandler({AccessDeniedException.class})
//    public ResponseEntity<Object> handleAccessDeniedException(AccessDeniedException ex, WebRequest request) {
////        return new ResponseEntity("keeks", new HttpHeaders(), HttpStatus.UNAUTHORIZED);
//        return new ResponseEntity(new ApiResponse(false, "keeks"), HttpStatus.FORBIDDEN);
//    }
//    @ExceptionHandler({MethodArgumentNotValidException.class})
//    protected ResponseEntity<Object> handle(MethodArgumentNotValidException e, WebRequest request){
//        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("value not pass validation by size");
//        return new ResponseEntity<>(Collections.singletonMap("response", "value not pass validation by size"),HttpStatus.BAD_REQUEST);
//    }


}
