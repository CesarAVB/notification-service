package br.com.sistema.notification.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class MessageEventListener {

    // ====================================================
    // Métodos - Escuta mensagens da fila notification-queue
    // ====================================================
    @RabbitListener(queues = "notification-queue")
    public void listenNotificationQueue(String message) {
        try {
            log.info("Mensagem recebida na fila notification-queue: {}", message);
            processMessage(message);
        } catch (Exception e) {
            log.error("Erro ao processar mensagem: {}", e.getMessage(), e);
        }
    }

    // ====================================================
    // Métodos - Escuta mensagens da fila event-queue
    // ====================================================
    @RabbitListener(queues = "event-queue")
    public void listenEventQueue(String message) {
        try {
            log.info("Mensagem recebida na fila event-queue: {}", message);
            processMessage(message);
        } catch (Exception e) {
            log.error("Erro ao processar mensagem: {}", e.getMessage(), e);
        }
    }

    // ====================================================
    // Métodos - Processa a mensagem recebida
    // ====================================================
    private void processMessage(String message) {
        log.debug("Processando mensagem: {}", message);
    }
}
