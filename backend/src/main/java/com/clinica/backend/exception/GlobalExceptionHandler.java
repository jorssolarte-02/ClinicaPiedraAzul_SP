package com.clinica.backend.exception;
import java.util.*; import org.springframework.dao.DataIntegrityViolationException; import org.springframework.http.*; import org.springframework.web.bind.MethodArgumentNotValidException; import org.springframework.web.bind.annotation.*;
@RestControllerAdvice public class GlobalExceptionHandler {
 @ExceptionHandler(ApiException.class) ResponseEntity<Map<String,String>> api(ApiException e){return ResponseEntity.status(e.getStatus()).body(Map.of("message",e.getMessage()));}
 @ExceptionHandler(MethodArgumentNotValidException.class) ResponseEntity<Map<String,String>> validation(MethodArgumentNotValidException e){String m=e.getBindingResult().getFieldErrors().stream().map(x->x.getField()+": "+x.getDefaultMessage()).findFirst().orElse("Datos inválidos");return ResponseEntity.badRequest().body(Map.of("message",m));}
 @ExceptionHandler(DataIntegrityViolationException.class) ResponseEntity<Map<String,String>> duplicate(DataIntegrityViolationException e){return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("message","Ya existe un registro con esos datos."));}
}
