package br.com.barbearia.udia.compartilhado;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class NossoHandler {

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Map<String, String>> handlerResponseStatusEx(ResponseStatusException e) {

        var response = Map
                .of("menssagem", e.getReason(),
                        "status", e.getStatusCode().toString(),
                        "ocorridoEm", LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<AdviceDTO> validarMethodArgumentNotValid(MethodArgumentNotValidException e) {

        var fieldErrors = e.getBindingResult().getFieldErrors();
        var fields = fieldErrors.stream().map(FieldError::getField).collect(Collectors.joining(", "));
        var fieldMessage = fieldErrors.stream().map(FieldError::getDefaultMessage).collect(Collectors.joining(", "));

        var response = new AdviceDTO(fields, fieldMessage, HttpStatus.BAD_REQUEST.toString(), LocalDateTime.now(ZoneId.of("GMT-3")));

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

}
