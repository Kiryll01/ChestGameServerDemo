package com.example.chestGameServer.Models.DTO.Events;

import lombok.*;
import org.springframework.messaging.Message;
import org.springframework.web.socket.messaging.AbstractSubProtocolEvent;
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class CustomAppEventForListeners extends AbstractSubProtocolEvent {
    public CustomAppEventForListeners(Object source, Message<byte[]> message) {
        super(source, message);
    }
}
