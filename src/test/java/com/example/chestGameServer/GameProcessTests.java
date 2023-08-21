package com.example.chestGameServer;

import com.example.chestGameServer.Controllers.WebSocket.GameProcessController;
import com.example.chestGameServer.Controllers.WebSocket.WsUtils;
import com.example.chestGameServer.Models.DTO.Messages.CardRequestMessage;
import com.example.chestGameServer.Models.DTO.Messages.CardRequestSummary;
import com.example.chestGameServer.Models.DTO.Messages.CardResponseMessage;
import com.example.chestGameServer.Models.Game.Card.CardValue;
import com.example.chestGameServer.Models.Game.Card.Suit;
import com.example.chestGameServer.Models.Game.GameRoom;
import com.example.chestGameServer.Services.GameProcessService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.messaging.simp.stomp.StompSession;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Log4j2
@FieldDefaults(level = AccessLevel.PROTECTED)
public class GameProcessTests extends GameRoomTests{
//@Test
public void requestCard() throws Exception{
    //joinRoomTest();
    StompSession session=client.getStompSession();

    StompSession.Receiptable receiptable=session.send(WsUtils.getCardRequestDestination(gameRoom.getId(),user.getId()),
            new CardRequestMessage(CardValue.ACE,2, List.of(Suit.CLUBS,Suit.HEARTS)));
    RunStopFrameHandler handler=new RunStopFrameHandler(new CompletableFuture<>());
    StompSession.Subscription subscription=session.subscribe(GameProcessController.FETCH_ALL_CARD_REQUESTS
            .replace("{room_id}",gameRoom.getId()),
            handler);
    byte[] payload= (byte[]) handler.getFuture().get();

    CardRequestSummary response =mapper.readValue(payload, CardRequestSummary.class);
    log.info(response);
}

}
