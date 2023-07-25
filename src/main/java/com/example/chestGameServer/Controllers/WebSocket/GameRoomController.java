package com.example.chestGameServer.Controllers.WebSocket;

import com.example.chestGameServer.Exceptions.*;
import com.example.chestGameServer.Models.DTO.Events.ChatEvent;
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
import com.example.chestGameServer.Services.GameRoomService;
import com.example.chestGameServer.Services.UserService;
import com.example.chestGameServer.configs.WebSocketConfig;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Controller
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@RequiredArgsConstructor
@Log4j2
public class GameRoomController {
    public static final String CREATE_GAME_ROOM="/rooms/game/create";
    public static final String FETCH_CREATE_GAME_ROOM_EVENT="rooms/game/events/create";
    public static final String JOIN_ROOM="/rooms/game/{room_id}/member/{member_id}/join-room";
    public static final String FETCH_PERSONAL_CARD_REQUESTS="rooms/game/{room_id}/member/{member_id}/card-request";
    public static final String FETCH_ALL_CARD_REQUESTS="rooms/game/{room_id}/card-requests";
    public static final String FETCH_ALL_GAME_CHAT_MESSAGES="rooms/game/{room_id}/chat-messages";
    public static final String FETCH_PERSONAL_GAME_CHAT_MESSAGES="rooms/game/{room_id}/member/{member_id}/chat-messages";
    RoomFactory roomFactory;
    SimpMessagingTemplate messagingTemplate;
    UserService userService;
    GameRoomService gameRoomService;

@SubscribeMapping(FETCH_PERSONAL_CARD_REQUESTS)
public CardRequestMessage fetchPersonalCardRequests(){
return null;
}
@SubscribeMapping(FETCH_ALL_CARD_REQUESTS)
public CardRequestMessage fetchAllCardRequests(){
return null;
}
@SubscribeMapping()
public DefaultTextMessage fetchAllGameChatMessages(){
return new DefaultTextMessage("send your messages for everybody here", DefaultAppProperties.DEFAULT_URL.getName());
}
@SubscribeMapping
public DefaultTextMessage fetchPersonalGameChatMessages(){
    return new DefaultTextMessage("send your personal messages here", DefaultAppProperties.DEFAULT_URL.getName());
}
@SubscribeMapping(FETCH_CREATE_GAME_ROOM_EVENT) public GameRoom fetchCreateGameRoomEvent(){
        return null;
    }
@MessageMapping(JOIN_ROOM)
public GameRoom joinRoom(@DestinationVariable("room_id") String roomId,
                         @DestinationVariable("member_id") String memberId,
                         @Header("simpSessionId") String sessionId) throws RoomException {
    Player player;
    GameRoom gameRoom;
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
    } catch (AppException e) {
        throw new RoomException(e.getMessage(),sessionId,roomId,e);
    }
    gameRoomService.save(gameRoom);
    gameRoomService.sendChatEvent(roomId, MemberJoinGameRoomEvent.builder()
            .user(player)
            .chat(gameRoom)
            .message(player.getName() + " joined the room")
            .build());
    return gameRoom;
}

//TODO: peak name from Principal
@MessageMapping(CREATE_GAME_ROOM)
    public void createRoom(
        CreateRoomMessage message,
       @Header("simpSessionId") String sessionId
) throws RoomException {

    log.info("new Request for creating a room with sessionId "+ sessionId);

    GameRoom gameRoom = null;
    try {
        gameRoom = roomFactory.createRoom(message,sessionId, GameRoom.class);
    } catch (AppException e) {
       throw new RoomException(e.getMessage(),sessionId, null,e);
    }

    log.info("new room created"+ gameRoom);

    gameRoomService.save(gameRoom);

    messagingTemplate.convertAndSend(WebSocketConfig.TOPIC_DESTINATION_PREFIX+FETCH_CREATE_GAME_ROOM_EVENT,gameRoom);

    log.info("room sent to the topic");
}

}
