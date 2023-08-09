package com.example.chestGameServer.Controllers.WebSocket;

import com.example.chestGameServer.Exceptions.AppException;
import com.example.chestGameServer.Exceptions.ObjectNotFoundException;
import com.example.chestGameServer.Exceptions.RoomException;
import com.example.chestGameServer.Models.DTO.Messages.CardRequestMessage;
import com.example.chestGameServer.Models.DTO.Messages.CardRequestSummary;
import com.example.chestGameServer.Models.DTO.Messages.CardResponseMessage;
import com.example.chestGameServer.Models.Game.Player;
import com.example.chestGameServer.Services.GameProcessService;
import com.example.chestGameServer.Services.GameRoomService;
import com.example.chestGameServer.Services.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;

import java.nio.file.AccessDeniedException;

@Controller
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@RequiredArgsConstructor
@Log4j2
public class GameProcessController {
    public static final String FETCH_PERSONAL_CARD_REQUESTS="/topic/rooms.game.{room_id}.member.{member_id}.card-requests";
    public static final String FETCH_ALL_CARD_REQUESTS="/topic/rooms.game.{room_id}.card-requests";
    public static final String REQUEST_CARDS="rooms.game.{room_id}.receipt.{receipt_id}.card-requests";

    GameProcessService gameProcessService;
    SimpMessagingTemplate messagingTemplate;
    UserService userService;
    GameRoomService gameRoomService;
    @SubscribeMapping(FETCH_PERSONAL_CARD_REQUESTS)
    public CardRequestSummary fetchPersonalCardRequests(@DestinationVariable("room_id") String roomId,
                                                        @DestinationVariable("member_id") String memberId){return null;}
    @SubscribeMapping(FETCH_ALL_CARD_REQUESTS)
    public CardRequestSummary fetchCardRequests(@DestinationVariable("room_id") String roomId){
        return null;
    }
@MessageMapping(REQUEST_CARDS)
    public void requestCards(@DestinationVariable("room_id")String roomId,
                             @DestinationVariable("receipt_id")String receiptSessionId,
                             CardRequestMessage requestMessage,
                             //TODO : replace
                             @Header("simpSessionID") String requestSessionId
) throws RoomException{
       try {
           if(!gameRoomService.findById(roomId).getMembers().stream()
                   .filter(player -> player.isTurn())
                   .findAny()
                   .orElseThrow(()->new ObjectNotFoundException("players can not make turn now","isTurn", Player.class))
                   .isTurn()) throw new IllegalAccessException();
           CardResponseMessage cardResponseMessage=gameProcessService.requestCards(requestMessage, roomId, receiptSessionId,requestSessionId);
       CardRequestSummary cardRequestSummary=CardRequestSummary.builder()
               .cardRequestMessage(requestMessage)
               .cardResponseMessage(cardResponseMessage)
               .receiptSessionId(receiptSessionId)
               .requestSessionId(requestSessionId)
               .build();
       messagingTemplate.convertAndSend(FETCH_ALL_CARD_REQUESTS.replace("{room_id}",roomId),
               cardRequestSummary);
       }
       catch (Exception e) {
           throw new RoomException(e.getMessage(),e,requestSessionId,roomId);
       }
}
}
