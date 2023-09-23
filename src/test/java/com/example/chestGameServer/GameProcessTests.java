package com.example.chestGameServer;

import com.example.chestGameServer.Controllers.MainOptionsController;
import com.example.chestGameServer.Controllers.WebSocket.GameProcessController;
import com.example.chestGameServer.Controllers.WebSocket.WsUtils;
import com.example.chestGameServer.Models.DTO.Messages.CardRequestMessage;
import com.example.chestGameServer.Models.DTO.Messages.CardRequestSummary;
import com.example.chestGameServer.Models.Game.Card.Card;
import com.example.chestGameServer.Models.Game.Card.CardValue;
import com.example.chestGameServer.Models.Game.Card.Suit;
import com.example.chestGameServer.Models.Game.GameRoom;
import com.example.chestGameServer.Models.User.WsSession;
import com.example.chestGameServer.Services.GameProcessService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.stomp.StompSession;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadLocalRandom;

@Log4j2
@FieldDefaults(level = AccessLevel.PROTECTED)
public class GameProcessTests extends GameRoomTests {
    @Autowired
    GameProcessService gameProcessService;
    GameRoom startedGameRoom;
    String sessionId;
    RunStopFrameHandler exceptionHandler;
    RunStopFrameHandler eventHandler;

    @BeforeAll
    public void startGameTest() throws Exception {

        log.info(gameRoom);

        StompSession session = client.getStompSession();


        //  RunStopFrameHandler handler=new RunStopFrameHandler(new CompletableFuture<>());

        // session.subscribe(WsUtils.getEventHandlingDestination(gameRoom.getId()),handler);

        session.send(MainOptionsController.makeJoinRoomLink(gameRoom.getId()), null);

        Thread.sleep(1000);
        // handler.getFuture().get();

        GameRoom gameStartedGameRoom = gameRoomService.findById(gameRoom.getId());

        Assertions.assertTrue(gameStartedGameRoom.isGameStarted(), "game should be started");

        gameStartedGameRoom.getMembers().forEach(player -> {
            WsSession playerSession = WsUtils.wsSessionsMap.get(player.getSessionId());
            Assertions.assertTrue(playerSession.isInGame(), playerSession.getUser().getName() + " is not in game");
        });
        String simpSessionId = gameStartedGameRoom.getMembers().stream().filter(player -> player.getName().equals(user.getName())).findAny().get().getSessionId();
        WsSession currentSession = WsUtils.wsSessionsMap.get(simpSessionId);
        Assertions.assertEquals(gameStartedGameRoom.getId(), currentSession.getGameRoomId(), currentSession.getUser().getName() + " has different gameRoomId");

        Assertions.assertTrue(gameStartedGameRoom.isGameStarted());
        gameStartedGameRoom.getMembers().forEach(player -> Assertions.assertEquals(4, player.getCards().size()));
        Assertions.assertEquals(52 - (gameStartedGameRoom.getMembers().size() * 4), gameStartedGameRoom.getDeck().size());
        startedGameRoom = gameStartedGameRoom;
        log.info(startedGameRoom);

        sessionId = simpSessionId;

        gameRoomService.save(startedGameRoom);

        subscribeToFetchEventsExceptions();
    }

    @Test
    public void requestCardTest() throws Exception {
        StompSession session = client.getStompSession();

        startedGameRoom.getMembers().forEach(player -> player.setTurn(false));
        startedGameRoom.getMembers().get(3).setTurn(true);
        gameRoomService.save(startedGameRoom);
        session.send(WsUtils.getCardRequestDestination(startedGameRoom.getId(), players.get(0).getSessionId()),
                new CardRequestMessage(CardValue.ACE, 2, List.of(Suit.CLUBS, Suit.HEARTS)));
        RunStopFrameHandler handler = new RunStopFrameHandler(new CompletableFuture<>());
        session.subscribe(GameProcessController.getFetchCardsDestination(startedGameRoom.getId()),
                handler);
        byte[] payload = (byte[]) handler.getFuture().get();

        CardRequestSummary response = mapper.readValue(payload, CardRequestSummary.class);
        GameRoom startedGameRoom = gameRoomService.findById(gameRoom.getId());
        Assertions.assertTrue(startedGameRoom.getMembers().get(0).isTurn(), "first player should have turn now");
        if (response.getCardResponseMessage().isCardCountPresent() == false || response.getCardResponseMessage().isCardSuitsPresent() == false ||
                response.getCardResponseMessage().isCardValuePresent() == false) {
            Assertions.assertEquals(52 - startedGameRoom.getMembers().size() * 4, startedGameRoom.getDeck().size());
            startedGameRoom.getMembers().forEach(player -> Assertions.assertEquals(4, player.getCards().size()));
        }

        startedGameRoom.getMembers().forEach(player -> player.setTurn(false));
        startedGameRoom.getMembers().get(3).setTurn(true);

        List<Card> cards = startedGameRoom.getMembers().get(0).getCards();

        Card nonRepetitiveCard = null;
        for (int i = 0; i < cards.size(); i++) {
            nonRepetitiveCard = cards.get(i);
            for (int j = i + 1; j < cards.size(); j++) {
                if (cards.get(i).getCardValue().equals(cards.get(j).getCardValue())) {
                    nonRepetitiveCard = null;
                    break;
                }
                if (nonRepetitiveCard != null) break;
            }
        }
        log.info(startedGameRoom.getMembers().get(0).getCards());
        if (nonRepetitiveCard == null) {
            log.info("there no non-repetitive cards, restart test");
            return;
        }

        CardRequestMessage cardRequestOfRightCard = new CardRequestMessage(nonRepetitiveCard.getCardValue(), 1, List.of(nonRepetitiveCard.getSuit()));
        session.send(WsUtils.getCardRequestDestination(startedGameRoom.getId(), players.get(0).getSessionId()),
                cardRequestOfRightCard);

        byte[] payloadOfRightCard = (byte[]) handler.getFuture().get();
        CardRequestSummary responseOfRightCard = mapper.readValue(payloadOfRightCard, CardRequestSummary.class);
        log.info(responseOfRightCard);

        startedGameRoom = gameRoomService.findById(gameRoom.getId());

        Assertions.assertEquals(cardRequestOfRightCard, responseOfRightCard.getCardRequestMessage());
        Assertions.assertTrue(responseOfRightCard.getCardResponseMessage().isCardCountPresent());
        Assertions.assertTrue(responseOfRightCard.getCardResponseMessage().isCardValuePresent());
        Assertions.assertTrue(responseOfRightCard.getCardResponseMessage().isCardSuitsPresent());

        Assertions.assertTrue(startedGameRoom.getMembers().get(0).isTurn());

        Assertions.assertTrue(startedGameRoom.getMembers().get(3).getCards().contains(new Card(responseOfRightCard.getCardRequestMessage().getCardValue(), responseOfRightCard.getCardRequestMessage().getSuits().get(0))));

        Assertions.assertTrue(startedGameRoom.getMembers().get(3).getCards().size() == 5, startedGameRoom.getMembers().get(3).getCards().toString());

        Assertions.assertTrue(startedGameRoom.getMembers().get(0).getCards().size() == 4);

        Assertions.assertTrue(startedGameRoom.getDeck().size() == (52 - startedGameRoom.getMembers().size() * 4 - 1));

    }


    @Test
    public void imitateGameTest() throws Exception {

        StompSession session = client.getStompSession();
        RunStopFrameHandler handler = new RunStopFrameHandler(new CompletableFuture<>());
        session.subscribe(WsUtils.getFetchGameProcessEventsDestination(startedGameRoom.getId()), handler);

        int receiptPlayerIndex = -1;
        while (startedGameRoom.getMembers().stream().anyMatch(player -> player.getCards().size() != 0)) {
            receiptPlayerIndex++;
            startedGameRoom.getMembers().forEach(p -> p.setTurn(false));
            startedGameRoom.getMembers().get(3).setTurn(true);
            int playerToAsk = ThreadLocalRandom.current().nextInt(0, 2);
            int cardsToAskCount = ThreadLocalRandom.current().nextInt(1, 3);
            CardRequestMessage cardsToAsk = new CardRequestMessage();
            int cardValueIndex = ThreadLocalRandom.current().nextInt(0, 12);
            List<Integer> previousIndexes = new ArrayList<>(3);
            CardValue[] values = CardValue.values();
            Suit[] suits = Suit.values();
            cardsToAsk.setCardCount(cardsToAskCount);
            cardsToAsk.setCardValue(values[cardValueIndex]);
            List<Suit> suitsToAsk = new ArrayList<>();
            for (int i = 0; i < cardsToAskCount; i++) {
                int cardSuitIndex;
                while (true) {
                    cardSuitIndex = ThreadLocalRandom.current().nextInt(0, 3);
                    if (!previousIndexes.contains(cardSuitIndex)) break;
                }
                suitsToAsk.add(suits[cardSuitIndex]);
            }
            cardsToAsk.setSuits(suitsToAsk);
            session.send(WsUtils.getCardRequestDestination(startedGameRoom.getId(), players.get(playerToAsk).getSessionId()),
                    cardsToAsk);
        }
        log.info("the game is finished");

    }

    @AfterAll
    public void logEventsExceptions() throws ExecutionException, InterruptedException {
//       log.info( eventHandler.getFuture().get());
//       log.info(exceptionHandler.getFuture().get());
    }

    private void subscribeToFetchEventsExceptions() {
        StompSession session = client.getStompSession();
        exceptionHandler = new RunStopFrameHandler(new CompletableFuture<>());
        eventHandler = new RunStopFrameHandler(new CompletableFuture<>());
        session.subscribe(WsUtils.getRoomExceptionHandlingDestination(sessionId, startedGameRoom.getId()), exceptionHandler);
        session.subscribe(WsUtils.getCommonExceptionHandlingDestination(sessionId), exceptionHandler);
        session.subscribe(WsUtils.getEventHandlingDestination(startedGameRoom.getId()), eventHandler);
        session.subscribe(WsUtils.getFetchGameProcessEventsDestination(startedGameRoom.getId()), eventHandler);
    }
}
