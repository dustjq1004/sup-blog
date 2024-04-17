package me.kimyeonsup.blog.global.exception.handler;


import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private static final String ERR_KEY_STATUS_CODE = "status";
    private static final String ERR_KEY_ERROR_NAME = "error";
    private static final String ERR_KEY_TIME_STAMP = "timestamp";
    private static final String ERR_KEY_MESSAGE = "errors";

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        Map<String, Object> body = new HashMap<>();

        body.put(ERR_KEY_STATUS_CODE, HttpStatus.BAD_REQUEST.value());
        body.put(ERR_KEY_ERROR_NAME, HttpStatus.BAD_REQUEST);
        body.put(ERR_KEY_TIME_STAMP, LocalDateTime.now());

        List<Map<String, String>> errors = ex.getBindingResult().getFieldErrors()
                .stream()
                .map(e -> Map.of("field", e.getField(), "defaultMessage", e.getDefaultMessage()))
                .collect(Collectors.toList());

        body.put(ERR_KEY_MESSAGE, errors);

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }
}
