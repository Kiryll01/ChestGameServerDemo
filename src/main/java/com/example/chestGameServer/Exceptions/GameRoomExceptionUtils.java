package com.example.chestGameServer.Exceptions;

import com.example.chestGameServer.Controllers.WebSocket.GameRoomController;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@Component
public class GameRoomExceptionUtils extends ExceptionUtils {
    public GameRoomExceptionUtils(SimpMessagingTemplate messagingTemplate) {
        super(messagingTemplate);
    }
    public void sendExceptionToSpecifiedUrl(String simpSessionId,AppException exception) {
        String message = exception.getMessage();
        messagingTemplate.convertAndSendToUser(simpSessionId, GameRoomController.FETCH_EXCEPTIONS, message);
    }
}

