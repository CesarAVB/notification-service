package br.com.sistema.notification.dto.response;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ExceptionResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    private LocalDateTime timestamp;
    private String message;
    private String details;
}
