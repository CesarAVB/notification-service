package br.com.sistema.notification.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class HealthCheckResponse {
    
    private String status;
    private String message;
}
