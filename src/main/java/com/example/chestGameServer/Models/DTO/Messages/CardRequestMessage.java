package com.example.chestGameServer.Models.DTO.Messages;

import com.example.chestGameServer.Models.Game.Card.CardValue;
import com.example.chestGameServer.Models.Game.Card.Suit;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.List;

@AllArgsConstructor
@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CardRequestMessage {
    CardValue cardValue;
    @Min(1)
    @Max(3)
    int cardCount;
    List<Suit> suits;
}
