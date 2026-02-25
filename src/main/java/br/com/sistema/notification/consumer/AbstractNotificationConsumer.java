package br.com.sistema.notification.consumer;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractNotificationConsumer {

    // ====================================================
    // Métodos - Template Method para consumir mensagens
    // ====================================================
    protected void consumeMessage(String message, String queueName) {
        try {
            log.info("Mensagem recebida de {}: {}", queueName, message);
            handleMessage(message);
        } catch (Exception e) {
            log.error("Erro ao processar mensagem de {}: {}", queueName, e.getMessage(), e);
            handleError(message, e);
        }
    }

    // ====================================================
    // Métodos - Método abstrato para implementação customizada
    // ====================================================
    public abstract void handleMessage(String message) throws Exception;

    // ====================================================
    // Métodos - Método default para tratamento de erros
    // ====================================================
    protected void handleError(String message, Exception e) {
        log.error("Falha no processamento: {}", e.getMessage());
    }
}
