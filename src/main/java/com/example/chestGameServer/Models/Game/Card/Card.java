package com.example.chestGameServer.Models.Game.Card;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
public class Card {
  CardValue cardValue;
  Suit suit;
public Card(){
 cardValue=null;
 suit=null;
}
  public Card(CardValue cardValue, Suit suit){
  this.cardValue= cardValue;
  this.suit=suit;
}
}
