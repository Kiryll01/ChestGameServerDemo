package com.example.chestGameServer;

import com.example.chestGameServer.Controllers.WebSocket.GameProcessController;
import com.example.chestGameServer.Controllers.WebSocket.GameRoomController;
import com.example.chestGameServer.Controllers.WebSocket.MemberWsController;
import com.example.chestGameServer.Controllers.WebSocket.WsUtils;
import com.example.chestGameServer.Exceptions.FullChatException;
import com.example.chestGameServer.Models.DTO.Messages.CardRequestMessage;
import com.example.chestGameServer.Models.DTO.Messages.CardRequestSummary;
import com.example.chestGameServer.Models.DTO.Messages.CardResponseMessage;
import com.example.chestGameServer.Models.Game.Card.CardValue;
import com.example.chestGameServer.Models.Game.Card.Suit;
import com.example.chestGameServer.Models.Game.GameRoom;
import com.example.chestGameServer.Models.User.WsSession;
import com.example.chestGameServer.Services.GameProcessService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.stomp.StompSession;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Log4j2
@FieldDefaults(level = AccessLevel.PROTECTED)
public class GameProcessTests extends GameRoomTests{
    @Autowired
    GameProcessService gameProcessService;
    @BeforeAll
    public void startGameTest() throws Exception{

        log.info(gameRoom);

        StompSession session=client.getStompSession();

      //  RunStopFrameHandler handler=new RunStopFrameHandler(new CompletableFuture<>());

       // session.subscribe(WsUtils.getEventHandlingDestination(gameRoom.getId()),handler);

        session.send(GameRoomController.JOIN_ROOM.replace("{room_id}",gameRoom.getId()),null);

       Thread.sleep(5000);
        // handler.getFuture().get();

        GameRoom gameStartedGameRoom=gameRoomService.findById(gameRoom.getId());

        Assertions.assertTrue(gameStartedGameRoom.isGameStarted(),"game should be started");

        gameStartedGameRoom.getMembers().forEach(player ->{
            WsSession playerSession=WsUtils.wsSessionsMap.get(player.getSessionId());
            Assertions.assertEquals(playerSession.getGameRoomId(),gameRoom.getId(),playerSession.getUser().getName()+" has different gameRoomId");
            Assertions.assertTrue(playerSession.isInGame(),playerSession.getUser().getName()+" is not in game");
        });

        Assertions.assertTrue(gameStartedGameRoom.isGameStarted());
        gameStartedGameRoom.getMembers().forEach(player -> Assertions.assertEquals(4,player.getCards().size()));
        Assertions.assertEquals(52-(gameStartedGameRoom.getMembers().size()*4),gameStartedGameRoom.getDeck().size());
    }
@Test
public void startGame() throws Exception{




//    StompSession.Receiptable receiptable=session.send(WsUtils.getCardRequestDestination(gameRoom.getId(),user.getId()),
//            new CardRequestMessage(CardValue.ACE,2, List.of(Suit.CLUBS,Suit.HEARTS)));
//    RunStopFrameHandler handler=new RunStopFrameHandler(new CompletableFuture<>());
//    StompSession.Subscription subscription=session.subscribe(GameProcessController.FETCH_ALL_CARD_REQUESTS
//            .replace("{room_id}",gameRoom.getId()),
//            handler);
//    byte[] payload= (byte[]) handler.getFuture().get();
//
//    CardRequestSummary response =mapper.readValue(payload, CardRequestSummary.class);
//    log.info(response);
}
}
