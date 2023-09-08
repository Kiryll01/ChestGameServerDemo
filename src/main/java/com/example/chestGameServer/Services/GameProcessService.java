package com.example.chestGameServer.Services;

import com.example.chestGameServer.Controllers.WebSocket.GameProcessController;
import com.example.chestGameServer.Controllers.WebSocket.WsUtils;
import com.example.chestGameServer.Exceptions.FullChatException;
import com.example.chestGameServer.Exceptions.ObjectNotFoundException;
import com.example.chestGameServer.Exceptions.UserNotFoundException;
import com.example.chestGameServer.Models.DTO.Events.ChestCompletedEvent;
import com.example.chestGameServer.Models.DTO.Events.GameProcessEvent;
import com.example.chestGameServer.Models.DTO.Events.GameStartedEvent;
import com.example.chestGameServer.Models.DTO.Messages.CardRequestMessage;
import com.example.chestGameServer.Models.DTO.Messages.CardResponseMessage;
import com.example.chestGameServer.Models.Game.Card.Card;
import com.example.chestGameServer.Models.Game.Card.CardValue;
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
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@FieldDefaults(level = AccessLevel.PROTECTED,makeFinal = true)
@RequiredArgsConstructor
@Log4j2
public class GameProcessService {
    GameRoomService gameRoomService;
    SimpMessagingTemplate messagingTemplate;
    public void sendGameProcessEvent(String roomId,GameProcessEvent event){
     messagingTemplate.convertAndSend(GameProcessController.FETCH_GAME_PROCESS_EVENTS.replace("{room_id}",roomId),event);
    }

    //TODO : check chests on the start
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
       List<Card> rightCards= receiptCards.stream()
                .filter(card -> card.getCardValue().equals(requestMessage.getCardValue())).collect(Collectors.toList());
        long cardCount=rightCards.size();
        if(cardCount!=0) cardResponseMessage.setCardValuePresent(true);
        else return changeTurn(gameRoom, requestingPlayer, cardResponseMessage);
        if(cardCount==requestMessage.getCardCount()) cardResponseMessage.setCardCountPresent(true);
        else return  changeTurn(gameRoom, requestingPlayer, cardResponseMessage);

        for (Card card : rightCards) {
           if(!requestMessage.getSuits().stream().anyMatch(suit -> card.getSuit().equals(suit)))
               return changeTurn(gameRoom, requestingPlayer, cardResponseMessage);
        }
        cardResponseMessage.setCardSuitsPresent(true);
        List<Card> cardsToRemove=new ArrayList<>();
        requestMessage.getSuits().forEach(suit -> cardsToRemove.add(new Card(requestMessage.getCardValue(),suit)));
        receiptPlayer.getCards().removeAll(cardsToRemove);
        CardValue potentialChestCardValue=requestingPlayer.addCards(cardsToRemove);
        if(potentialChestCardValue!=null) sendGameProcessEvent(roomId,ChestCompletedEvent.builder().completedChestCardValue(potentialChestCardValue)
                .chestCompletedSessionId(requestingPlayer.getSessionId())
                .chat(gameRoom)
                .message("player "+ requestingPlayer.getName()+ " completed a chest of "+ potentialChestCardValue.getName())
                .build());
        if(receiptPlayer.getCards().size()<4)while(receiptPlayer.getCards().size()!=4 && gameRoom.getDeck().size()!=0){
           CardValue cardValue=receiptPlayer.addCard(gameRoom.getDeck().remove(gameRoom.getDeck().size()-1));
        if(cardValue!=null) sendGameProcessEvent(roomId,ChestCompletedEvent.builder().completedChestCardValue(potentialChestCardValue)
                .chestCompletedSessionId(receiptPlayer.getSessionId())
                .chat(gameRoom)
                .message("player "+ receiptPlayer.getName()+ " completed a chest of "+ potentialChestCardValue.getName())
                .build());
        }
        return changeTurn(gameRoom,requestingPlayer,cardResponseMessage);
    }
    private Player getPlayerBySessionId(String sessionID, GameRoom gameRoom) throws UserNotFoundException {
        return gameRoom.getMembers().stream()
                .filter(o -> o.getSessionId().equals(sessionID))
                .findAny()
                .orElseThrow(() -> new UserNotFoundException("sessionId is wrong", sessionID));
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
