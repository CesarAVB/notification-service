package br.com.sistema.notification.service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import br.com.sistema.notification.dto.request.PublishMessageRequest;
import br.com.sistema.notification.dto.response.PublishMessageResponse;
import br.com.sistema.notification.exception.InvalidQueueException;
import br.com.sistema.notification.exception.NotificationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {

    private static final int MAX_RECENT = 50;

    private final RabbitTemplate rabbitTemplate;
    private final List<PublishMessageResponse> recentNotifications = new CopyOnWriteArrayList<>();

    // ====================================================
    // Métodos - Publica mensagem em fila/exchange RabbitMQ
    // ====================================================
    public PublishMessageResponse publishMessage(PublishMessageRequest request) {
        String messageId = UUID.randomUUID().toString();

        try {
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
                throw new InvalidQueueException("Exchange ou fila deve ser especificado");
            }
        } catch (InvalidQueueException e) {
            throw e;
        } catch (Exception e) {
            log.error("Erro ao publicar mensagem no RabbitMQ: {}", e.getMessage(), e);
            throw new NotificationException("Erro ao publicar mensagem: " + e.getMessage(), e);
        }

        PublishMessageResponse response = PublishMessageResponse.builder()
            .messageId(messageId)
            .status("SUCCESS")
            .message("Mensagem publicada com sucesso")
            .exchange(request.getExchange())
            .routingKey(request.getRoutingKey())
            .timestamp(LocalDateTime.now())
            .build();

        recentNotifications.add(0, response);
        if (recentNotifications.size() > MAX_RECENT) {
            recentNotifications.subList(MAX_RECENT, recentNotifications.size()).clear();
        }

        return response;
    }

    // ====================================================
    // Métodos - Retorna histórico recente de notificações
    // ====================================================
    public List<PublishMessageResponse> getRecentNotifications() {
        return Collections.unmodifiableList(recentNotifications);
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
