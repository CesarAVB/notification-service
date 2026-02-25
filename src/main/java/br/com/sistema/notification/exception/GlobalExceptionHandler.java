package br.com.sistema.notification.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import br.com.sistema.notification.dto.response.ExceptionResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidQueueException.class)
    public ResponseEntity<ExceptionResponse> handleInvalidQueue(InvalidQueueException ex, WebRequest request) {
        log.warn("Fila/exchange inválida: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
            new ExceptionResponse(LocalDateTime.now(), ex.getMessage(), request.getDescription(false))
        );
    }

    @ExceptionHandler(NotificationException.class)
    public ResponseEntity<ExceptionResponse> handleNotification(NotificationException ex, WebRequest request) {
        log.error("Erro de notificação: {}", ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
            new ExceptionResponse(LocalDateTime.now(), ex.getMessage(), request.getDescription(false))
        );
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ExceptionResponse> handleIllegalArgument(IllegalArgumentException ex, WebRequest request) {
        log.warn("Argumento inválido: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
            new ExceptionResponse(LocalDateTime.now(), ex.getMessage(), request.getDescription(false))
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handleGenericException(Exception ex, WebRequest request) {
        log.error("Erro interno inesperado: {}", ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
            new ExceptionResponse(LocalDateTime.now(), "Erro interno do servidor", request.getDescription(false))
        );
    }
}
