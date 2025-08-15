package ProblemApp.demo.controller;

import jakarta.persistence.EntityNotFoundException;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import java.time.Instant;
import java.util.Map;

@RestControllerAdvice
public class BasicExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<?> notFound(EntityNotFoundException ex){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(err("NotFound", ex.getMessage()));
    }

    @ExceptionHandler({MethodArgumentNotValidException.class, ConstraintViolationException.class, IllegalArgumentException.class})
    public ResponseEntity<?> badRequest(Exception ex){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err("ValidationError", ex.getMessage()));
    }

    @ExceptionHandler(SecurityException.class)
    public ResponseEntity<?> forbidden(SecurityException ex){
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(err("PermissionDenied", ex.getMessage()));
    }

    private Map<String,Object> err(String type, String msg){
        return Map.of("timestamp", Instant.now().toString(), "error", type, "message", msg);
    }
}

