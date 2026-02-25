package br.com.sistema.notification.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.data.domain.Page;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.sistema.notification.dto.request.PublishMessageRequest;
import br.com.sistema.notification.dto.response.HealthCheckResponse;
import br.com.sistema.notification.dto.response.PublishMessageResponse;
import br.com.sistema.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    // ====================================================
    // Métodos - Publica mensagem via requisição HTTP
    // ====================================================
    @PostMapping("/publish")
    public ResponseEntity<PublishMessageResponse> publishMessage(@RequestBody PublishMessageRequest request) {
        return ResponseEntity.ok(notificationService.publishMessage(request));
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
    // Métodos - Retorna histórico paginado de notificações
    // ====================================================
    @GetMapping("/recent")
    public ResponseEntity<Page<PublishMessageResponse>> getRecentNotifications(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(notificationService.getRecentNotifications(page, size));
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

}
