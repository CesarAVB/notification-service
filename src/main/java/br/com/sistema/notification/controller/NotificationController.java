package br.com.sistema.notification.controller;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.sistema.notification.dto.request.PublishMessageRequest;
import br.com.sistema.notification.dto.response.ErrorResponse;
import br.com.sistema.notification.dto.response.HealthCheckResponse;
import br.com.sistema.notification.dto.response.PublishMessageResponse;
import br.com.sistema.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class NotificationController {

    private final NotificationService notificationService;

    // ====================================================
    // Métodos - Publica mensagem via requisição HTTP
    // ====================================================
    @PostMapping("/publish")
    public ResponseEntity<PublishMessageResponse> publishMessage(
            @RequestBody PublishMessageRequest request) {
        
        if ((request.getExchange() == null || request.getExchange().isEmpty()) &&
            (request.getQueue() == null || request.getQueue().isEmpty())) {
            
            PublishMessageResponse errorResponse = PublishMessageResponse.builder()
                .status("ERROR")
                .message("Exchange ou queue deve ser especificado")
                .timestamp(LocalDateTime.now())
                .build();
            
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }

        PublishMessageResponse response = notificationService.publishMessage(request);
        
        if ("SUCCESS".equals(response.getStatus())) {
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // ====================================================
    // Métodos - Retorna status de saúde do serviço
    // ====================================================
    @GetMapping("/health")
    public ResponseEntity<HealthCheckResponse> healthCheck() {
        boolean isHealthy = notificationService.isHealthy();
        
        HealthCheckResponse response = HealthCheckResponse.builder()
            .status(isHealthy ? "UP" : "DOWN")
            .message(isHealthy ? "Serviço operacional" : "Serviço indisponível")
            .build();
        
        HttpStatus status = isHealthy ? HttpStatus.OK : HttpStatus.SERVICE_UNAVAILABLE;
        return ResponseEntity.status(status).body(response);
    }

    // ====================================================
    // Métodos - Retorna informações do serviço
    // ====================================================
    @GetMapping("/info")
    public ResponseEntity<Map<String, String>> serviceInfo() {
        Map<String, String> info = new HashMap<>();
        info.put("service", "Notification Service");
        info.put("version", "1.0.0");
        info.put("description", "Microserviço de notificação baseado em eventos RabbitMQ");
        
        return ResponseEntity.ok(info);
    }

    // ====================================================
    // Métodos - Trata exceções globais
    // ====================================================
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgument(IllegalArgumentException e) {
        ErrorResponse response = ErrorResponse.builder()
            .error("INVALID_ARGUMENT")
            .message(e.getMessage())
            .statusCode(HttpStatus.BAD_REQUEST.value())
            .timestamp(LocalDateTime.now())
            .build();
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception e) {
        ErrorResponse response = ErrorResponse.builder()
            .error("INTERNAL_ERROR")
            .message("Erro interno do servidor")
            .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
            .timestamp(LocalDateTime.now())
            .build();
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
