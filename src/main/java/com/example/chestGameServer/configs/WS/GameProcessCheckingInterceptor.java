package com.example.chestGameServer.configs.WS;

import com.example.chestGameServer.Controllers.WebSocket.WsUtils;
import com.example.chestGameServer.Exceptions.AppMessageException;
import com.example.chestGameServer.Exceptions.GameProcessException;
import com.example.chestGameServer.Exceptions.ObjectNotFoundException;
import com.example.chestGameServer.Models.Game.GameRoom;
import com.example.chestGameServer.Models.User.WsSession;
import com.example.chestGameServer.Repositories.GameRoomRepository;
import com.example.chestGameServer.configs.ProtectedPaths;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Component;

import java.util.NoSuchElementException;

@Component
@RequiredArgsConstructor
@Log4j2
public class GameProcessCheckingInterceptor implements ChannelInterceptor {
    // gameRoomService forms a cycle. due to messagingTemplate, I guess.
    private final GameRoomRepository gameRoomRepository;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        if (accessor.getCommand().equals(StompCommand.SEND))
            if (ProtectedPaths.gameProcessDestinations.stream().anyMatch(dest -> accessor.getDestination().contains(dest))) {
                WsSession session = WsUtils.wsSessionsMap.get(accessor.getSessionId());
                if (session == null)
                    throw new AppMessageException(message, new ObjectNotFoundException("ws session was not found", accessor.getSessionId(), WsSession.class));
                String roomId = WsUtils.getRoomId(accessor.getDestination());
                GameRoom gameRoom;
                try {
                    gameRoom = gameRoomRepository.findById(roomId).orElseThrow();
                } catch (NoSuchElementException e) {
                    throw new AppMessageException(message, e);
                }
                if (!gameRoom.isGameStarted())
                    throw new AppMessageException(message, new GameProcessException("game was not started", roomId, accessor.getDestination()));
                if (!gameRoom.getMembers().stream().anyMatch(player -> player.getSessionId().equals(accessor.getSessionId())))
                    throw new AppMessageException(message,
                            new GameProcessException("this id is not registered in the requested gameRoom", roomId, accessor.getDestination()));
            }

        return message;
    }
}
