package com.example.chestGameServer.Models.DTO.Messages;

import lombok.*;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class CardRequestSummary {
    CardRequestMessage cardRequestMessage;
    CardResponseMessage cardResponseMessage;
    String receiptSessionId;
    String requestSessionId;
}
