package com.example.chestGameServer.Models.DTO.Events;

import com.example.chestGameServer.Models.Game.GameRoom;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@SuperBuilder(builderMethodName = "builder")
public class GameStartedEvent extends ChatEvent<GameRoom>{

}
