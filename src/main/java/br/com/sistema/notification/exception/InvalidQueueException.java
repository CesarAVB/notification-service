package br.com.sistema.notification.exception;

public class InvalidQueueException extends NotificationException {

    private static final long serialVersionUID = 1L;

	public InvalidQueueException(String message) {
        super(message);
    }

    public InvalidQueueException(String message, Throwable cause) {
        super(message, cause);
    }
}
