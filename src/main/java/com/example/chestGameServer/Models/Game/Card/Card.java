package com.example.chestGameServer.Models.Game.Card;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
@FieldDefaults(makeFinal = true,level = AccessLevel.PRIVATE)
@Data
public class Card {
  String cardValue;
  String suit;

  public Card() {
    cardValue=null;
    suit=null;
  }

  public Card(CardValue cardValue, Suit suit){
  this.cardValue= cardValue.getName();
  this.suit=suit.getSuit();
}
}
