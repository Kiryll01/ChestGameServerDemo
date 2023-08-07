package com.example.chestGameServer.Controllers.WebSocket;

import com.example.chestGameServer.configs.WebSocketConfig;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class WsUtils {
    public static String getErrorHandlingDestination(String sessionId,String roomId) {
        return WebSocketConfig.TOPIC_DESTINATION_PREFIX + MemberWsController.FETCH_ROOM_EXCEPTIONS
                .replace("{member_id}", sessionId)
                .replace("{room-id}",roomId);
    }
    public static String getEventHandlingDestination(String roomId){
        return WebSocketConfig.TOPIC_DESTINATION_PREFIX +MemberWsController.FETCH_ROOM_EVENTS.replace("{room_id}",roomId);
    }
}