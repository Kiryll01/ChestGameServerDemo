package com.example.chestGameServer.Models.Factories;

import com.example.chestGameServer.Exceptions.FullChatException;
import com.example.chestGameServer.Exceptions.UserNotFoundException;
import com.example.chestGameServer.Models.Abstract.AbstractChat;
import com.example.chestGameServer.Models.Abstract.AbstractUser;
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
    public <C extends AbstractChat> C createRoom(CreateRoomMessage message,String wsSessionId, AbstractUser user,Class<C> typeChatClass) throws FullChatException, UserNotFoundException {;

        if(GameRoom.class.equals(typeChatClass)) return (C) createGameRoom(message,wsSessionId,user);

        return null;
    }

    private GameRoom createGameRoom(CreateRoomMessage message,String sessionId,AbstractUser abstractUser) throws FullChatException {

        GameRoom gameRoom=new GameRoom(message.getName());

        gameRoom.setRoomSizeLimit(message.getRoomSizeLimit());

        Player player=new Player();
        player.setName(abstractUser.getName());
        player.setId(abstractUser.getId());
        player.setRoomId(gameRoom.getId());
        player.setSessionId(sessionId);
        player.setCards(new ArrayList<>());

        gameRoom.addMember(player);

        gameRoom.setOwnerId(abstractUser.getId());

        return gameRoom;
    }
}
