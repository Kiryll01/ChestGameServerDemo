package com.example.chestGameServer;

import com.example.chestGameServer.Models.Entities.User;
import com.example.chestGameServer.Models.Game.GameRoom;
import com.example.chestGameServer.Repositories.RoomRepository;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
@Log4j2
public class RedisTests extends AbstractTestClass{
    @Autowired
    RedisTemplate<String, User> redisTemplate;
    @Autowired
    HashOperations<String,String, GameRoom> roomHashOperations;
    @Autowired
    RoomRepository roomRepository;
    @Test
    public void saveRoom(){
        GameRoom gameRoomFromDb =roomRepository.save(gameRoom);
        Assertions.assertEquals(gameRoom, gameRoomFromDb);
        log.info(gameRoom);
        log.info(gameRoomFromDb);

//        GameRoom roomFromRedis = roomHashOperations.get("Rooms",gameRoomFromDb.getId());
//
//        log.info(roomFromRedis);

    }
    @AfterAll
    public void destroy(){
        roomRepository.deleteAll();
}
}
