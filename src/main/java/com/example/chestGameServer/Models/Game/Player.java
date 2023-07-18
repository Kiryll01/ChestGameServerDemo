package com.example.chestGameServer.Models.Game;

import com.example.chestGameServer.Models.Abstract.AbstractUser;
import com.example.chestGameServer.Models.Game.Card.Card;
import lombok.*;

import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.UUID;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class Player extends AbstractUser {
    String roomId;
    List<Card> cards;
    String sessionId;

    public Player(String name, String roomId, List<Card> cards) {
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
