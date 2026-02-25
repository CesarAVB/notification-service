package br.com.sistema.notification.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    // ====================================================
    // Métodos - Define exchanges (roteadores de mensagens)
    // ====================================================
    @Bean
    public TopicExchange notificationExchange() {
        return new TopicExchange("notification-exchange", true, false);
    }

    @Bean
    public DirectExchange eventExchange() {
        return new DirectExchange("event-exchange", true, false);
    }

    // ====================================================
    // Métodos - Define filas (armazenadores de mensagens)
    // ====================================================
    @Bean
    public Queue notificationQueue() {
        return new Queue("notification-queue", true, false, false);
    }

    @Bean
    public Queue eventQueue() {
        return new Queue("event-queue", true, false, false);
    }

    @Bean
    public Queue deadLetterQueue() {
        return new Queue("notification-dlq", true, false, false);
    }

    // ====================================================
    // Métodos - Vincula exchanges com filas via routing key
    // ====================================================
    @Bean
    public Binding notificationBinding(Queue notificationQueue, TopicExchange notificationExchange) {
        return BindingBuilder.bind(notificationQueue).to(notificationExchange).with("notification.*");
    }

    @Bean
    public Binding eventBinding(Queue eventQueue, DirectExchange eventExchange) {
        return BindingBuilder.bind(eventQueue).to(eventExchange).with("event");
    }

}
