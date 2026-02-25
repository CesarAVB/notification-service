package br.com.sistema.notification.dto.response;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class PublishMessageResponse {
    
    private String messageId;
    private String status;
    private String message;
    private String exchange;
    private String routingKey;
    private LocalDateTime timestamp;
}
