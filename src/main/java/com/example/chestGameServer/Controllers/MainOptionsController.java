package com.example.chestGameServer.Controllers;

import com.example.chestGameServer.Controllers.WebSocket.GameRoomController;
import com.example.chestGameServer.Exceptions.UserNotFoundException;
import com.example.chestGameServer.Models.DTO.UserDTO;
import com.example.chestGameServer.Models.Factories.UserMapper;
import com.example.chestGameServer.Models.Game.GameRoom;
import com.example.chestGameServer.Models.User.User;
import com.example.chestGameServer.Services.GameRoomService;
import com.example.chestGameServer.Services.UserService;
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

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@RequiredArgsConstructor
public class MainOptionsController {
    GameRoomService gameRoomService;
    public static String makeJoinRoomLink(String roomId){
        return GameRoomController.JOIN_ROOM.replace("{room_id}",roomId);
    }
    UserService userService;
    public static final String FETCH_ROOMS="/user/{user_id}/rooms/game_room/get_all";
    public static final String RETRIEVE_USER="/user/{username}/get";
    public static final String FETCH_USERS="/user/get_all";
    public static final String GET_ACCOUNT_INFO="/user/get_info";

    @GetMapping(FETCH_USERS)
    public ResponseEntity<List<UserDTO>> fetchUsers(){
        List<User> usersFromDb=userService.getAll();
        List<UserDTO> userDTOS=new ArrayList<>(100);
        usersFromDb.forEach(user -> userDTOS.add(UserMapper.USER_MAPPER.toUserDto(user)));
        return ResponseEntity.ok(userDTOS);
    }

    @GetMapping(RETRIEVE_USER)
    public ResponseEntity<UserDTO> retrieveUser(@PathVariable("username") String userName) throws UserNotFoundException {
        User user=userService.findUserByName(userName);
        if(user==null)throw new UserNotFoundException("user not found",userName);
        return ResponseEntity.ok()
                .body(UserMapper.USER_MAPPER.toUserDto(user));
    }
    @GetMapping(FETCH_ROOMS)
    @ResponseBody
    public ResponseEntity<?> fetchGameRooms(@PathVariable("user_id")String userId){
        List<EntityModel<GameRoom>> rooms= ((List<GameRoom>) gameRoomService.findAll()).stream()
                .map(room-> EntityModel.of(room,
                        linkTo(GameRoomController.class).slash(makeJoinRoomLink(room.getId())).withRel("game_room_ws_subscribe_link")))
                .collect(Collectors.toList());
        return ResponseEntity.ok().body(
                CollectionModel.of(rooms,linkTo(methodOn(MainOptionsController.class).fetchGameRooms(userId)).withSelfRel()));

    }
}
