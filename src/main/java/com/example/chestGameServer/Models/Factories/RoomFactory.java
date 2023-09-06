package com.example.chestGameServer.Models.Factories;

import com.example.chestGameServer.Exceptions.FullChatException;
import com.example.chestGameServer.Exceptions.UserNotFoundException;
import com.example.chestGameServer.Models.Abstract.AbstractChat;
import com.example.chestGameServer.Models.DTO.Messages.CreateRoomMessage;
import com.example.chestGameServer.Models.DTO.UserPrincipal;
import com.example.chestGameServer.Models.Game.GameRoom;
import com.example.chestGameServer.Models.Game.Player;
import com.example.chestGameServer.Models.User.User;
import com.example.chestGameServer.Services.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@RequiredArgsConstructor
@Log4j2
@Component
public class RoomFactory {
    UserService userService;
    public <C extends AbstractChat> C createRoom(CreateRoomMessage message, UserPrincipal userPrincipal,Class<C> typeChatClass) throws FullChatException, UserNotFoundException {

        User user=userService.findById(userPrincipal.getUser().getId());

        if(GameRoom.class.equals(typeChatClass)) return (C) createGameRoom(message,userPrincipal.getWsSessionId(),user);

        return null;
    }

    private GameRoom createGameRoom(CreateRoomMessage message,String sessionId,User user) throws FullChatException {

        GameRoom gameRoom=new GameRoom(message.getName());

        gameRoom.setRoomSizeLimit(message.getRoomSizeLimit());

        Player player=UserMapper.USER_MAPPER.toPlayer(UserMapper.USER_MAPPER.toUserDto(user));
        player.setRoomId(gameRoom.getId());
        player.setSessionId(sessionId);
        player.setCards(new ArrayList<>());

        gameRoom.addMember(player);

        gameRoom.setOwnerId(user.getId());

        return gameRoom;
    }
}
