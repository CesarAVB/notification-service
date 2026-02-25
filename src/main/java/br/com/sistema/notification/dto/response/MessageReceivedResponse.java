package br.com.sistema.notification.dto.response;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class MessageReceivedResponse {
    
    private String messageId;
    private String content;
    private String queue;
    private LocalDateTime receivedAt;
    private String status;
}
