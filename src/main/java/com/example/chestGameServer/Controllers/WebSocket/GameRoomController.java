package com.example.chestGameServer.Controllers.WebSocket;

import com.example.chestGameServer.Exceptions.*;

import com.example.chestGameServer.Models.DTO.Events.MemberJoinGameRoomEvent;
import com.example.chestGameServer.Models.DTO.Messages.CardRequestMessage;
import com.example.chestGameServer.Models.DTO.Messages.CreateRoomMessage;
import com.example.chestGameServer.Models.DTO.Messages.DefaultTextMessage;
import com.example.chestGameServer.Models.Enums.DefaultAppProperties;
import com.example.chestGameServer.Models.Factories.RoomFactory;
import com.example.chestGameServer.Models.Factories.UserMapper;
import com.example.chestGameServer.Models.Game.GameRoom;
import com.example.chestGameServer.Models.Game.Player;
import com.example.chestGameServer.Models.User.User;
import com.example.chestGameServer.Services.GameProcessService;
import com.example.chestGameServer.Services.GameRoomService;
import com.example.chestGameServer.Services.UserService;
import com.example.chestGameServer.configs.WebSocketConfig;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;




@Controller
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@RequiredArgsConstructor
@Log4j2
public class GameRoomController {
    public static final String CREATE_GAME_ROOM="/rooms.game.create";
    public static final String FETCH_CREATE_GAME_ROOM_EVENT="/topic/rooms.game.events.create";
    public static final String JOIN_ROOM="/rooms.game.{room_id}.join-room";

    public static final String FETCH_ALL_GAME_CHAT_MESSAGES="/topic/rooms.game.{room_id}.chat-messages";
    public static final String FETCH_PERSONAL_GAME_CHAT_MESSAGES="/topic/rooms.game.{room_id}.member.{member_id}.chat-messages";
    GameProcessService gameProcessService;
    RoomFactory roomFactory;
    SimpMessagingTemplate messagingTemplate;
    UserService userService;
    GameRoomService gameRoomService;

@SubscribeMapping(FETCH_ALL_GAME_CHAT_MESSAGES)
public DefaultTextMessage fetchAllGameChatMessages(@DestinationVariable("room_id") String roomId){
return new DefaultTextMessage("send your messages for everybody here", DefaultAppProperties.DEFAULT_URL.getName());
}
@SubscribeMapping(FETCH_PERSONAL_GAME_CHAT_MESSAGES)
public DefaultTextMessage fetchPersonalGameChatMessages(@DestinationVariable("room_id") String roomId,
                                                        @DestinationVariable("member_id") String memberId){
    return new DefaultTextMessage("send your personal messages here", DefaultAppProperties.DEFAULT_URL.getName());
}
@SubscribeMapping(FETCH_CREATE_GAME_ROOM_EVENT) public GameRoom fetchCreateGameRoomEvent(){
        return null;
    }
    @MessageMapping(JOIN_ROOM)
public GameRoom joinRoom(@DestinationVariable("room_id") String roomId,
                          String memberId
) throws RoomException {
    Player player;
    GameRoom gameRoom;
    //TODO : use spring security to get sessionId and find user by it;
    String sessionId=memberId;
    try {
        gameRoom = gameRoomService.findById(roomId);
        User user = userService.findById(memberId);
        player = UserMapper.USER_MAPPER.toPlayer(UserMapper.USER_MAPPER.toUserDto(user));
        player.setRoomId(roomId);

        player.setSessionId(sessionId);
        gameRoom.addMember(player);
        gameRoomService.save(gameRoom);
        gameRoomService.sendChatEvent(roomId, MemberJoinGameRoomEvent.builder()
                .user(player)
                .chat(gameRoom)
                .message(player.getName() + " joined the room")
                .build());

    if (gameRoom.isRoomSizeLimitReached()) gameProcessService.startGame(gameRoom);

    } catch (AppException e) {
        throw new RoomException(e.getMessage(),e,roomId,sessionId);
    }
    return gameRoom;
}

//TODO: peak name from Principal
@MessageMapping(CREATE_GAME_ROOM)
    public void createRoom(
        CreateRoomMessage message
) throws UserException{
    String sessionId=message.getSessionId();
    log.info("new Request for creating a room with sessionId "+ sessionId);
    GameRoom gameRoom;
    try {
        gameRoom = roomFactory.createRoom(message,sessionId, GameRoom.class);
    } catch (AppException e) {
       throw new UserException(e.getMessage(), e,sessionId);
    }
    log.info("new room created"+ gameRoom);
    gameRoomService.save(gameRoom);
    messagingTemplate.convertAndSend(FETCH_CREATE_GAME_ROOM_EVENT,gameRoom);
    log.info("room sent to the topic");
}

}
