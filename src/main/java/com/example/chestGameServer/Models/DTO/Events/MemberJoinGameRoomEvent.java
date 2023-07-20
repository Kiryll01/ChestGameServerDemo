package com.example.chestGameServer.Models.DTO.Events;

import com.example.chestGameServer.Models.Game.GameRoom;
import com.example.chestGameServer.Models.Game.Player;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@FieldDefaults(level = AccessLevel.PROTECTED)
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@NoArgsConstructor
@SuperBuilder()
public class MemberJoinGameRoomEvent extends MemberJoinChatEvent<GameRoom, Player> {
    public MemberJoinGameRoomEvent(GameRoom chat, String message, Player user) {
        super(chat, message, user);
    }
}
