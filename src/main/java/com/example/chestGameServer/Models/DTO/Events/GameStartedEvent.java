package com.example.chestGameServer.Models.DTO.Events;

import com.example.chestGameServer.Models.Game.GameRoom;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@SuperBuilder(builderMethodName = "builder")
@JsonIgnoreProperties(ignoreUnknown = true)
public class GameStartedEvent extends ChatEvent<GameRoom> {
    public GameStartedEvent(GameRoom chat, String message) {
        super(chat, message);
    }
}
