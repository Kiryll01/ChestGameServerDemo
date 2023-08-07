package com.example.chestGameServer.Services;

import com.example.chestGameServer.Models.DTO.Events.GameStartedEvent;
import com.example.chestGameServer.Models.Game.Card.Card;
import com.example.chestGameServer.Models.Game.GameRoom;
import com.example.chestGameServer.Models.Game.GameUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@FieldDefaults(level = AccessLevel.PROTECTED,makeFinal = true)
@RequiredArgsConstructor
@Log4j2
public class GameProcessService {
    GameRoomService gameRoomService;
    public void startGame(GameRoom gameRoom)  {
        List<Card> deck= GameUtils.generateDeck();
        gameRoom.getMembers().forEach(player -> {
            for (int i = 0; i <4 ; i++) {
                Card card=deck.remove(deck.size()-1);
                player.addCard(card);
            }
        gameRoomService.sendChatEvent(gameRoom.getId(),GameStartedEvent.builder()
                .chat(gameRoom)
                .message("game was started at room : "+gameRoom.getName())
                .build()
        );
        });

    }
}
