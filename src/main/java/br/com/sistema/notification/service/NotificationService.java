package br.com.sistema.notification.service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
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
    private final RabbitAdmin rabbitAdmin;
    private final List<PublishMessageResponse> recentNotifications = new CopyOnWriteArrayList<>();

    // ====================================================
    // Métodos - Publica mensagem em fila/exchange RabbitMQ
    // ====================================================
    public PublishMessageResponse publishMessage(PublishMessageRequest request) {
        String messageId = UUID.randomUUID().toString();
        try {
            if (request.getExchange() != null && !request.getExchange().isEmpty()) {
                // Criar exchange se não existir
                createExchangeIfNotExists(request.getExchange());
                
                // Se houver routing key, criar binding
                if (request.getRoutingKey() != null && !request.getRoutingKey().isEmpty()) {
                    createQueueAndBinding(request.getExchange(), request.getRoutingKey());
                }
                
                rabbitTemplate.convertAndSend(
                    request.getExchange(),
                    request.getRoutingKey() != null ? request.getRoutingKey() : "",
                    request.getMessage()
                );
                log.info("Mensagem publicada no exchange: {} com routing key: {}",
                    request.getExchange(), request.getRoutingKey());
                    
            } else if (request.getQueue() != null && !request.getQueue().isEmpty()) {
                // Criar fila se não existir
                createQueueIfNotExists(request.getQueue());
                
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
    // Métodos - Cria fila se não existir
    // ====================================================
    private void createQueueIfNotExists(String queueName) {
        try {
            Queue queue = new Queue(
                queueName,
                true,   // durable
                false,  // exclusive
                false   // autoDelete
            );
            rabbitAdmin.declareQueue(queue);
            log.info("Fila criada dinamicamente: {}", queueName);
        } catch (Exception e) {
            log.debug("Fila '{}' já existe ou não pode ser criada: {}", queueName, e.getMessage());
        }
    }

    // ====================================================
    // Métodos - Cria exchange se não existir
    // ====================================================
    private void createExchangeIfNotExists(String exchangeName) {
        try {
            TopicExchange exchange = new TopicExchange(
                exchangeName,
                true,   // durable
                false   // autoDelete
            );
            rabbitAdmin.declareExchange(exchange);
            log.info("Exchange criado dinamicamente: {}", exchangeName);
        } catch (Exception e) {
            log.debug("Exchange '{}' já existe ou não pode ser criado: {}", exchangeName, e.getMessage());
        }
    }

    // ====================================================
    // Métodos - Cria fila e binding com exchange
    // ====================================================
    private void createQueueAndBinding(String exchangeName, String routingKey) {
        try {
            // Criar fila baseada no routing key
            String queueName = generateQueueName(exchangeName, routingKey);
            createQueueIfNotExists(queueName);
            
            // Criar binding entre exchange e fila
            org.springframework.amqp.core.Binding binding = 
                org.springframework.amqp.core.BindingBuilder
                    .bind(new Queue(queueName))
                    .to(new TopicExchange(exchangeName))
                    .with(routingKey);
                    
            rabbitAdmin.declareBinding(binding);
            log.info("Binding criado: exchange={}, queue={}, routingKey={}", 
                exchangeName, queueName, routingKey);
        } catch (Exception e) {
            log.debug("Erro ao criar binding: {}", e.getMessage());
        }
    }

    // ====================================================
    // Métodos - Gera nome de fila baseado em exchange e routing key
    // ====================================================
    private String generateQueueName(String exchangeName, String routingKey) {
        return exchangeName + "." + routingKey.replaceAll("[.*#]", "");
    }

    // ====================================================
    // Métodos - Retorna histórico recente de notificações
    // ====================================================
    public List<PublishMessageResponse> getRecentNotifications() {
        return Collections.unmodifiableList(recentNotifications);
    }

    // ====================================================
    // Métodos - Limpa histórico de notificações
    // ====================================================
    public void clearRecentNotifications() {
        recentNotifications.clear();
        log.info("Histórico de notificações limpo");
    }

    // ====================================================
    // Métodos - Retorna quantidade de notificações recentes
    // ====================================================
    public int getRecentNotificationsCount() {
        return recentNotifications.size();
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