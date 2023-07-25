package com.example.chestGameServer.Controllers;

import com.example.chestGameServer.Controllers.WebSocket.GameRoomController;
import com.example.chestGameServer.Models.Game.GameRoom;
import com.example.chestGameServer.Services.GameRoomService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@RequiredArgsConstructor
public class MainOptionsController {
    GameRoomService gameRoomService;
    public static String makeJoinRoomLink(String roomId,String memberId){
        return GameRoomController.JOIN_ROOM.replace("{room_id}",roomId).replace("{member_id}",memberId);
    }
    public static final String FETCH_ROOMS="/user/{user_id}/rooms/game_room/get_all";
    @GetMapping(FETCH_ROOMS)
    @ResponseBody
    public ResponseEntity<?> fetchGameRooms(@PathVariable("user_id")String userId){
        List<EntityModel<GameRoom>> rooms= ((List<GameRoom>) gameRoomService.findAll()).stream()
                .map(room-> EntityModel.of(room,
                        linkTo(GameRoomController.class).slash(makeJoinRoomLink(room.getId(),userId)).withRel("game_room_ws_subscribe_link")))
                .collect(Collectors.toList());
        return ResponseEntity.ok().body(
                CollectionModel.of(rooms,linkTo(methodOn(MainOptionsController.class).fetchGameRooms(userId)).withSelfRel()));

    }
}
