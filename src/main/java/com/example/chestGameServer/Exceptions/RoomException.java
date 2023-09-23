package com.example.chestGameServer.Exceptions;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@ToString(callSuper = true)
public class RoomException extends UserException {
    protected String roomId;

    public RoomException(String message, Exception e, String sessionId, String roomId) {
        super(message, e, sessionId);
        this.roomId = roomId;
    }
}
