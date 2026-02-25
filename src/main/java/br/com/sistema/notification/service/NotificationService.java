package br.com.sistema.notification.service;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import br.com.sistema.notification.dto.request.PublishMessageRequest;
import br.com.sistema.notification.dto.response.PublishMessageResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {

    private final RabbitTemplate rabbitTemplate;

    // ====================================================
    // Métodos - Publica mensagem em fila/exchange RabbitMQ
    // ====================================================
    public PublishMessageResponse publishMessage(PublishMessageRequest request) {
        try {
            String messageId = UUID.randomUUID().toString();
            
            if (request.getExchange() != null && !request.getExchange().isEmpty()) {
                rabbitTemplate.convertAndSend(
                    request.getExchange(),
                    request.getRoutingKey() != null ? request.getRoutingKey() : "",
                    request.getMessage()
                );
                
                log.info("Mensagem publicada no exchange: {} com routing key: {}", 
                    request.getExchange(), request.getRoutingKey());
            } else if (request.getQueue() != null && !request.getQueue().isEmpty()) {
                rabbitTemplate.convertAndSend(request.getQueue(), request.getMessage());
                
                log.info("Mensagem publicada na fila: {}", request.getQueue());
            } else {
                log.warn("Nenhum exchange ou fila especificada");
                return PublishMessageResponse.builder()
                    .messageId(messageId)
                    .status("ERROR")
                    .message("Exchange ou fila deve ser especificado")
                    .timestamp(LocalDateTime.now())
                    .build();
            }

            return PublishMessageResponse.builder()
                .messageId(messageId)
                .status("SUCCESS")
                .message("Mensagem publicada com sucesso")
                .exchange(request.getExchange())
                .routingKey(request.getRoutingKey())
                .timestamp(LocalDateTime.now())
                .build();

        } catch (Exception e) {
            log.error("Erro ao publicar mensagem: {}", e.getMessage(), e);
            return PublishMessageResponse.builder()
                .status("ERROR")
                .message("Erro ao publicar mensagem: " + e.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
        }
    }

    // ====================================================
    // Métodos - Valida se o serviço está operacional
    // ====================================================
    public boolean isHealthy() {
        try {
            rabbitTemplate.convertAndSend("", "", "health-check");
            return true;
        } catch (Exception e) {
            log.error("Health check falhou: {}", e.getMessage());
            return false;
        }
    }
}
