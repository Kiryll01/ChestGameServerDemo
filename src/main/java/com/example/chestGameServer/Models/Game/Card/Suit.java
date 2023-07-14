package com.example.chestGameServer.Models.Game.Card;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Suit {
    DIAMONDS("diamonds"),
    HEARTS("hearts"),
    SPADES("spades"),
    CLUBS("clubs");

    String suit;
}
