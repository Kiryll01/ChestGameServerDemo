package com.example.chestGameServer.Models.DTO.Events;

import lombok.*;
import org.springframework.messaging.Message;
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class SubscriptionSuccessEvent extends CustomAppEventForListeners {
private String brokerLink;
    public SubscriptionSuccessEvent(Object source, Message<byte[]> message, String brokerLink) {
        super(source, message);
        this.brokerLink = brokerLink;
    }
}
