package com.example.chestGameServer.Models.DTO.Events;

import com.example.chestGameServer.Models.Game.Card.CardValue;
import com.example.chestGameServer.Models.Game.GameRoom;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@NoArgsConstructor
@SuperBuilder(builderMethodName = "builder")
public class ChestCompletedEvent extends GameProcessEvent {
    CardValue completedChestCardValue;
    String chestCompletedSessionId;

    public ChestCompletedEvent(GameRoom chat, String message, CardValue completedChestCardValue, String chestCompletedSessionId) {
        super(chat, message);
        this.completedChestCardValue = completedChestCardValue;
        this.chestCompletedSessionId = chestCompletedSessionId;
    }
}
