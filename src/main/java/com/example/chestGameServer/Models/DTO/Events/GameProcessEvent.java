package com.example.chestGameServer.Models.DTO.Events;

import com.example.chestGameServer.Models.Game.GameRoom;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@NoArgsConstructor
@SuperBuilder(builderMethodName = "gameProcessBuilder")
public class GameProcessEvent extends ChatEvent<GameRoom> {
    public GameProcessEvent(GameRoom chat, String message) {
        super(chat, message);
    }
}
