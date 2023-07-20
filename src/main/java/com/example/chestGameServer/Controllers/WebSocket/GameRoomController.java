package com.example.chestGameServer.Controllers.WebSocket;

import com.example.chestGameServer.Exceptions.ExceptionUtils;
import com.example.chestGameServer.Models.DTO.Events.ChatEvent;
import com.example.chestGameServer.Models.DTO.Events.MemberJoinGameRoomEvent;
import com.example.chestGameServer.Models.DTO.Messages.CardRequestMessage;
import com.example.chestGameServer.Models.DTO.Messages.CreateRoomMessage;
import com.example.chestGameServer.Models.DTO.Messages.DefaultTextMessage;
import com.example.chestGameServer.Models.Enums.DefaultAppProperties;
import com.example.chestGameServer.Models.Factories.UserMapper;
import com.example.chestGameServer.Exceptions.FullChatException;
import com.example.chestGameServer.Models.Game.GameRoom;
import com.example.chestGameServer.Models.Game.Player;
import com.example.chestGameServer.Models.User.User;
import com.example.chestGameServer.Exceptions.RoomNotFoundException;
import com.example.chestGameServer.Services.GameRoomService;
import com.example.chestGameServer.Exceptions.UserNotFoundException;
import com.example.chestGameServer.Services.UserService;
import com.example.chestGameServer.configs.WebSocketConfig;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
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
    public static final String FETCH_CHAT_EVENTS = "/rooms.game.{room_id}.events";
    public static final String FETCH_ROOMS="/user/{user_id}/rooms/game_room/get_all";
    public static final String CREATE_GAME_ROOM="/rooms.game.create";
    public static final String FETCH_CREATE_GAME_ROOM_EVENT="/rooms.game.create.event";
    public static final String JOIN_ROOM="/rooms.game.{room_id}.member.{member_id}";
    public static final String FETCH_EXCEPTIONS="/user/queue/rooms.game.{room_id}.exceptions";
    SimpMessagingTemplate messagingTemplate;
    UserService userService;
    GameRoomService gameRoomService;
    ExceptionUtils exceptionUtils;

    @GetMapping(FETCH_ROOMS)
    @ResponseBody
    public ResponseEntity<?> fetchRooms(@PathVariable("user_id")String userId){
       List<EntityModel<GameRoom>> rooms= ((List<GameRoom>) gameRoomService.findAll()).stream()
                .map(room-> EntityModel.of(room,
                        linkTo(GameRoomController.class).slash(makeJoinRoomLink(room.getId(),userId)).withRel("game_room_ws_subscribe_link")))
                .collect(Collectors.toList());
        return ResponseEntity.ok().body(
                CollectionModel.of(rooms,linkTo(methodOn(GameRoomController.class).fetchRooms(userId)).withSelfRel()));

    }
public static String makeJoinRoomLink(String roomId,String memberId){
        return JOIN_ROOM.replace("{room_id}",roomId).replace("{member_id}",memberId);
}
// map people to join in chat
@SubscribeMapping
public CardRequestMessage fetchPersonalCardRequests(){
return null;
}
@SubscribeMapping
public CardRequestMessage fetchAllCardRequests(){
return null;
}
@SubscribeMapping
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
@SubscribeMapping(FETCH_CHAT_EVENTS)
public ChatEvent fetchChatEvents(){
return null;
}
@SubscribeMapping(FETCH_EXCEPTIONS)
public String fetchExceptions(){
return null;
}
@MessageMapping(JOIN_ROOM)
public GameRoom joinRoom(@DestinationVariable("room_id") String roomId,
                         @DestinationVariable("member_id") String memberId,
                         @Header String simpSessionId) throws RoomNotFoundException, UserNotFoundException, FullChatException {
GameRoom gameRoom=gameRoomService.findById(roomId);
User user=userService.findById(memberId);
Player player = UserMapper.USER_MAPPER.toPlayer(UserMapper.USER_MAPPER.toUserDto(user));
player.setRoomId(roomId);
player.setSessionId(simpSessionId);
gameRoom.addMember(player);
gameRoomService.save(gameRoom);
gameRoomService.sendChatEvent(roomId, MemberJoinGameRoomEvent.builder()
        .user(player)
        .chat(gameRoom)
        .message(player.getName()+" joined the room")
        .build());
return gameRoom;
}
public void startGame(){
        
}

//TODO: peak name from Principal
@MessageMapping(CREATE_GAME_ROOM)
    public void createRoom(
            CreateRoomMessage message,
            @Header("simpSessionId") String sessionId
) throws FullChatException,UserNotFoundException {

    log.info("new Request for creating a room");

    GameRoom gameRoom = createGameRoom(message,message.getUserId(),sessionId);

    log.info("new room created"+ gameRoom);

    gameRoomService.save(gameRoom);

    messagingTemplate.convertAndSend(WebSocketConfig.TOPIC_DESTINATION_PREFIX+FETCH_CREATE_GAME_ROOM_EVENT,gameRoom);

    log.info("room sent to the topic");
}
// TODO : make fabric for AbstractChat
    private GameRoom createGameRoom(CreateRoomMessage message,String userId,String sessionId) throws FullChatException, UserNotFoundException {

        userService.findById(userId);

        GameRoom gameRoom=new GameRoom(message.getName());

        gameRoom.setRoomSizeLimit(message.getRoomSizeLimit());

        Player player=Player.builder()
                        .roomId(gameRoom.getId())
                        .cards(new ArrayList<>())
                    .sessionId(sessionId)
                .build();

        gameRoom.addMember(player);

        gameRoom.setOwnerId(userId);

        return gameRoom;
    }
}
