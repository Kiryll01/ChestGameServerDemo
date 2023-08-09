package com.example.chestGameServer.Models.DTO.Messages;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CardResponseMessage {
    boolean isCardValuePresent;
    boolean isCardCountPresent;
    boolean isCardSuitsPresent;
}
