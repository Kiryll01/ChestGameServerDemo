package com.example.chestGameServer.Services;

import com.example.chestGameServer.Models.Game.GameRoom;
import com.example.chestGameServer.Repositories.GameRoomRepository;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class GameRoomService extends AbstractRoomService<GameRoom,GameRoomRepository> {
    public GameRoomService(GameRoomRepository repository) {
        super(repository);
    }

}
