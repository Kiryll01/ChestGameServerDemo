package com.example.chestGameServer;

import com.example.chestGameServer.Models.Game.GameRoom;
import com.example.chestGameServer.Models.User.User;
import com.example.chestGameServer.Repositories.GameRoomRepository;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;

@Log4j2
public class RedisTests extends AbstractTestClass {
    @Autowired
    RedisTemplate<String, User> redisTemplate;
    @Autowired
    HashOperations<String, String, GameRoom> roomHashOperations;
    @Autowired
    GameRoomRepository gameRoomRepository;

    @Test
    public void saveRoom() {
        GameRoom gameRoomFromDb = gameRoomRepository.save(gameRoom);
        Assertions.assertEquals(gameRoom, gameRoomFromDb);
        log.info(gameRoom);
        log.info(gameRoomFromDb);
        gameRoomRepository.deleteById(gameRoom.getId());
    }

    @Test
    public void getRoomByName() {
        gameRoomRepository.save(gameRoom);
        List<GameRoom> gameRoomFromDb = gameRoomRepository.findAllByName(gameRoom.getName());
        Assertions.assertEquals(gameRoom, gameRoomFromDb.get(0));
        log.info(gameRoom);
        log.info(gameRoomFromDb);
        gameRoomRepository.deleteById(gameRoom.getId());
    }

    @AfterAll
    public void destroy() {
        gameRoomRepository.deleteAll();
    }
}
