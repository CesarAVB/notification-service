package br.com.sistema.notification.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PublishMessageRequest {
    
    private String exchange;
    private String routingKey;
    private String queue;
    private String message;
    private String contentType;
}
