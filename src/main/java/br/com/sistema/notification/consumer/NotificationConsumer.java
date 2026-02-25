package br.com.sistema.notification.consumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class NotificationConsumer extends AbstractNotificationConsumer {

    // ====================================================
    // Métodos - Escuta fila notification-queue
    // ====================================================
    @RabbitListener(queues = "notification-queue")
    public void handleNotification(String message) {
        consumeMessage(message, "notification-queue");
    }

    // ====================================================
    // Métodos - Implementa lógica customizada da mensagem
    // ====================================================
    @Override
    public void handleMessage(String message) throws Exception {
        log.info("Notificação recebida e processada: {}", message);
    }

    // ====================================================
    // Métodos - Implementa tratamento customizado de erros
    // ====================================================
    @Override
    protected void handleError(String message, Exception e) {
        log.error("Erro ao processar notificação: {}", message);
        super.handleError(message, e);
    }
}
