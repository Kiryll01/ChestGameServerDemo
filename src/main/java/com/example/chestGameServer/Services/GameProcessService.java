package com.example.chestGameServer.Services;

import com.example.chestGameServer.Controllers.WebSocket.WsUtils;
import com.example.chestGameServer.Exceptions.FullChatException;
import com.example.chestGameServer.Exceptions.ObjectNotFoundException;
import com.example.chestGameServer.Exceptions.UserNotFoundException;
import com.example.chestGameServer.Models.DTO.Events.GameStartedEvent;
import com.example.chestGameServer.Models.DTO.Messages.CardRequestMessage;
import com.example.chestGameServer.Models.DTO.Messages.CardResponseMessage;
import com.example.chestGameServer.Models.Game.Card.Card;
import com.example.chestGameServer.Models.Game.GameRoom;
import com.example.chestGameServer.Models.Game.GameUtils;
import com.example.chestGameServer.Models.Game.Player;
import com.example.chestGameServer.Models.User.WsSession;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

@Service
@FieldDefaults(level = AccessLevel.PROTECTED,makeFinal = true)
@RequiredArgsConstructor
@Log4j2
public class GameProcessService {
    GameRoomService gameRoomService;
    SimpMessagingTemplate messagingTemplate;
    public void startGame(GameRoom gameRoom) throws FullChatException {
        List<Card> deck= GameUtils.generateDeck();
        gameRoom.getMembers().forEach(player -> {
            player.setCards(new ArrayList<>());
            for (int i = 0; i <4 ; i++) {
                Card card=deck.remove(deck.size()-1);
                player.addCard(card);
            };
        });

        gameRoom.setDeck(deck);
        gameRoom.getMembers().get(0).setTurn(true);
        gameRoom.setGameStarted(true);
        gameRoomService.save(gameRoom);
        GameRoom gameRoomToSend=gameRoom;
        gameRoomToSend.setDeck(null);
        List<Player>players=gameRoom.getMembers();
        players.forEach(player -> player.setCards(null));
        gameRoomToSend.setMembers(players);
        setStartGameStatus(gameRoom.getMembers());
        gameRoomService.sendChatEvent(gameRoom.getId(),
                new GameStartedEvent(gameRoomToSend,"game was started at room : "+gameRoomToSend.getName()));
    }
    public CardResponseMessage requestCards(CardRequestMessage requestMessage, String roomId, String receiptSessionID, String requestingSessionID) throws ObjectNotFoundException {
        GameRoom gameRoom=gameRoomService.findById(roomId);
        Player receiptPlayer= getPlayerBySessionId(receiptSessionID, gameRoom);
        Player requestingPlayer= getPlayerBySessionId(requestingSessionID, gameRoom);
        List<Card> receiptCards=receiptPlayer.getCards();
        CardResponseMessage cardResponseMessage =new CardResponseMessage();
        Stream<Card> cardStream=receiptCards.stream()
                .filter(card -> card.getCardValue().equals(requestMessage.getCardValue()));
        long cardCount=cardStream.count();
        if(cardCount!=0) cardResponseMessage.setCardValuePresent(true);
        else return changeTurn(gameRoom, requestingPlayer, cardResponseMessage);
            if(cardCount==requestMessage.getCardCount()) cardResponseMessage.setCardCountPresent(true);
        else return  changeTurn(gameRoom, requestingPlayer, cardResponseMessage);
        if(!cardStream.anyMatch(card -> !requestMessage.getSuits().stream()
               .anyMatch(requestCard->
                       card.getSuit().equals(requestCard.getSuit())))) cardResponseMessage.setCardSuitsPresent(true);
        else return changeTurn(gameRoom, requestingPlayer, cardResponseMessage);
        List<Card> cardsToRemove=new ArrayList<>();
        requestMessage.getSuits().forEach(suit -> cardsToRemove.add(new Card(requestMessage.getCardValue(),suit)));
        receiptPlayer.getCards().removeAll(cardsToRemove);
        requestingPlayer.addCards(cardsToRemove);
        int cardSize=receiptPlayer.getCards().size();
        int deckSize=gameRoom.getDeck().size();
        if(cardSize<4)while(cardSize!=4 || deckSize!=0){
           receiptPlayer.addCard(gameRoom.getDeck().remove(gameRoom.getDeck().size()-1));
        }
        gameRoomService.save(gameRoom);
        return cardResponseMessage;
    }
    private Player getPlayerBySessionId(String requestingSessionID, GameRoom gameRoom) throws UserNotFoundException {
        return gameRoom.getMembers().stream()
                .filter(o -> o.getSessionId().equals(requestingSessionID))
                .findAny()
                .orElseThrow(() -> new UserNotFoundException("sessionId is wrong", requestingSessionID));
    }
    private CardResponseMessage changeTurn(GameRoom gameRoom, Player requestingPlayer, CardResponseMessage cardResponseMessage) {
        int requestPlayerIndex= gameRoom.getMembers().indexOf(requestingPlayer);
        requestingPlayer.setTurn(false);
        int nextPlayerIndex;
        if(requestPlayerIndex== gameRoom.getMembers().size()-1)nextPlayerIndex=0;
        else nextPlayerIndex=requestPlayerIndex+1;
        gameRoom.getMembers().get(nextPlayerIndex).setTurn(true);
        gameRoomService.save(gameRoom);
        return cardResponseMessage;
    }
    private void setStartGameStatus(List<Player> players){
        players.forEach(player -> {
            WsSession session=WsUtils.wsSessionsMap.get(player.getSessionId());
            session.setInGame(true);
            WsUtils.wsSessionsMap.put(player.getSessionId(),session);
        });

    }


}
