package br.com.sistema.notification.dto.response;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class ErrorResponse {
    
    private String error;
    private String message;
    private Integer statusCode;
    private LocalDateTime timestamp;
}
