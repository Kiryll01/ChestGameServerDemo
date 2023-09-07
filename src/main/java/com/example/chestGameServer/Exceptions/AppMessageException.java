package com.example.chestGameServer.Exceptions;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessagingException;

@FieldDefaults(level = AccessLevel.PROTECTED)
public class AppMessageException extends MessagingException {
Exception e;
    public AppMessageException(Message<?> message, String description, Exception cause) {
        super(message, description, cause);
        this.e=cause;
    }
    public AppMessageException(Message<?> message, Exception cause) {
        super(message, cause.getMessage(), cause);
        this.e=cause;
    }
}
