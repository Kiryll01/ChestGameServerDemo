package com.example.chestGameServer.Models.Game;

import com.example.chestGameServer.Models.Abstract.AbstractUser;
import com.example.chestGameServer.Models.Game.Card.Card;
import com.example.chestGameServer.Models.Game.Card.CardValue;
import lombok.*;

import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
public class Player extends AbstractUser {
    boolean isTurn;
    String roomId;
    List<Card> cards=new ArrayList<>();
    String sessionId;
    int points=0;
    // returns value of card if chest is complete
    public CardValue addCard(Card card){
        cards.add(card);
     if(checkIfChestIsComplete(List.of(card)))return card.getCardValue();
    return null;
    }
    public CardValue addCards(List<Card> cards){
        this.cards.addAll(cards);
        if(checkIfChestIsComplete(cards))return cards.get(0).getCardValue();
        return null;
    }
// should not be used to add cards for a game process purposes
    @Deprecated
    public void setCards(List<Card> cards) {
        this.cards = cards;
    }
private boolean checkIfChestIsComplete(List<Card> addedCards){
        List<Card> potentialChestCards=new ArrayList<>();
        cards.forEach(card -> {
            if(card.getCardValue().equals(addedCards.get(0).getCardValue()))potentialChestCards.add(card);
            });
            if(potentialChestCards.size()!=4) return false;
        cards.removeAll(potentialChestCards);
        points++;
        return true;
    //        Card temp=addedCards.get(0);
//        boolean areCardsEqual=true;
//        for (Card card : addedCards)
//            if(!temp.getCardValue().equals(card.getCardValue())) {
//            areCardsEqual=false;
//            break;
//        }
//        if(areCardsEqual){
}
    public void deleteCard(Card card){
        cards.remove(card);
}
public void deleteCards(List<Card> cards){
        this.cards.removeAll(cards);
}
public Player(String name, String roomId, @NonNull List<Card> cards) {
        super(name);
        super.setId(UUID.randomUUID().toString());
        this.roomId = roomId;
        this.cards = cards;
    }
    public Player(String name) {
        super(name);
        super.setId(UUID.randomUUID().toString());
    }
}
