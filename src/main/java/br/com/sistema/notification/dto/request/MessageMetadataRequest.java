package br.com.sistema.notification.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MessageMetadataRequest {
    
    private String exchangeName;
    private String exchangeType;
    private String queueName;
    private String routingKey;
}
