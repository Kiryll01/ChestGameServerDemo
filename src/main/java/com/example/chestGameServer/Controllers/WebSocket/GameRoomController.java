package com.example.chestGameServer.Controllers.WebSocket;

import com.example.chestGameServer.Models.DTO.Messages.CreateRoomMessage;
import com.example.chestGameServer.Models.Game.GameRoom;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;

@Controller
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@RequiredArgsConstructor
public class GameRoomController {
public static final String CREATE_GAME_ROOM="topic/rooms.game.create";
public static final String FETCH_CREATE_GAME_ROOM_EVENT="topic/fetch.rooms.game.create";

@SubscribeMapping(FETCH_CREATE_GAME_ROOM_EVENT)
  public GameRoom fetchCreateGameRoomEvent(){
    return null;
}
@MessageMapping(CREATE_GAME_ROOM)
    public void createRoom(CreateRoomMessage message){

}
}
