package com.example.chestGameServer.Controllers.WebSocket;

import com.example.chestGameServer.Exceptions.RoomException;
import com.example.chestGameServer.Models.DTO.Events.ChatEvent;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;

@Controller
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@RequiredArgsConstructor
@Log4j2
@ControllerAdvice
public class MemberWsController {
    public static final String FETCH_ROOM_EXCEPTIONS ="rooms/{room_id}/member/{member_id}/errors";
    public static final String FETCH_ROOM_EVENTS = "rooms/{room_id}/events";
    SimpMessagingTemplate messagingTemplate;
    @SubscribeMapping(FETCH_ROOM_EXCEPTIONS)
    public String fetchExceptions(@DestinationVariable("member_id") String memberId,
            @DestinationVariable("room_id") String roomId){
        log.info("new client subscribed to fetch exceptions ");
        return null;
    }
    @SubscribeMapping(FETCH_ROOM_EVENTS)
    public ChatEvent fetchRoomEvents(@DestinationVariable ("room_id") String roomId){
        return null;
    }
    @MessageExceptionHandler
    public void handleRoomExceptions(RoomException e){
        log.info("exception was catched : "+e);
        String message = e.getMessage();
        messagingTemplate.convertAndSend(
                WsUtils.getErrorHandlingDestination(e.getSessionId(),e.getRoomId()),
                message);
    }
}
