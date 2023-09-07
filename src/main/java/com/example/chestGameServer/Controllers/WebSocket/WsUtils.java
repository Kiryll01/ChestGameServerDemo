package com.example.chestGameServer.Controllers.WebSocket;

import com.example.chestGameServer.Models.User.WsSession;
import lombok.extern.log4j.Log4j2;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Log4j2
public class WsUtils {
    public static final Map<String, WsSession> wsSessionsMap=new ConcurrentHashMap<>(50);
    // roomId should be after the "room"
    public static String getRoomId(String destination){
      final String roomPattern="room.";
        int index=destination.indexOf(roomPattern);
        if(index==-1) return "";
        int idIndex=index+roomPattern.length();
        int idEndIndex=idIndex;
        char endChar=destination.charAt(idIndex);
        while(endChar!='.'){
            idEndIndex++;
            endChar=destination.charAt(idEndIndex);
        }
        return destination.substring(idIndex,idEndIndex);

    }
    public static String getRoomExceptionHandlingDestination(String sessionId, String roomId) {
        return  MemberWsController.FETCH_ROOM_EXCEPTIONS
                .replace("{member_id}", sessionId)
                .replace("{room-id}",roomId);
    }
    public static String getCommonExceptionHandlingDestination(String id) {
        return  MemberWsController.FETCH_USER_EXCEPTIONS
                .replace("{user_id}", id);
    }
    public static String getEventHandlingDestination(String roomId){
        return MemberWsController.FETCH_ROOM_EVENTS.replace("{room_id}",roomId);
    }
    public static String getCardRequestDestination(String roomId,String receiptSessionId){
        return GameProcessController.REQUEST_CARDS.replace("{room_id}",roomId).replace("{receipt_id}",receiptSessionId);
    }
}
