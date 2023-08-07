package com.example.chestGameServer.Models.Game;

import com.example.chestGameServer.Models.Game.Card.Card;
import com.example.chestGameServer.Models.Game.Card.CardValue;
import com.example.chestGameServer.Models.Game.Card.Suit;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class GameUtils {
    public static List<Card> generateDeck(){
List<Card> deck=new ArrayList<>(52);
        Arrays.stream(Suit.values())
                .forEach(suit -> Arrays.stream(CardValue.values())
                        .forEach(cardValue -> deck.add(new Card(cardValue,suit))));
        List<Card> sortedDeck=new ArrayList<>(52);
        sortedDeck.add(deck.get(ThreadLocalRandom.current().nextInt(deck.size())));
    return sortedDeck;
    }

}
