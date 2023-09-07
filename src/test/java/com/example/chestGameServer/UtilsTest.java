package com.example.chestGameServer;

import com.example.chestGameServer.Controllers.WebSocket.GameProcessController;
import com.example.chestGameServer.Controllers.WebSocket.WsUtils;
import com.example.chestGameServer.Exceptions.FullChatException;
import com.example.chestGameServer.Exceptions.GameProcessException;
import com.example.chestGameServer.Models.Game.GameRoom;
import com.example.chestGameServer.Models.Game.Player;
import com.example.chestGameServer.Services.GameProcessService;
import com.example.chestGameServer.Services.GameRoomService;
import lombok.extern.java.Log;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
@Log4j2
@ComponentScan("com.example.chestGameServer.Services")
@ExtendWith(SpringExtension.class)
public class UtilsTest {
    @Test
    public void getRoomIdTest(){
        String roomId=UUID.randomUUID().toString();
        String testRoomId=WsUtils.getRoomId(GameProcessController.REQUEST_CARDS.replace("{room_id}",roomId));
       log.info(roomId);
       log.info(testRoomId);
        Assertions.assertEquals(roomId,testRoomId);
    }

}
