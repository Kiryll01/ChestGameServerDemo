package com.example.chestGameServer.Services;

import com.example.chestGameServer.Controllers.WebSocket.WsUtils;
import com.example.chestGameServer.Models.DTO.Events.ChatEvent;
import com.example.chestGameServer.Models.Game.GameRoom;
import com.example.chestGameServer.Repositories.GameRoomRepository;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class GameRoomService extends AbstractRoomService<GameRoom,GameRoomRepository> {
    public GameRoomService(GameRoomRepository repository, SimpMessagingTemplate messagingTemplate) {
        super(GameRoom.class, repository,messagingTemplate);
    }
public void deleteAll(){
        repository.deleteAll();
}
    @Override
    public void sendChatEvent(String roomId, ChatEvent event) {
        messagingTemplate.convertAndSend(WsUtils.getEventHandlingDestination(roomId),
                event);
    }
}
