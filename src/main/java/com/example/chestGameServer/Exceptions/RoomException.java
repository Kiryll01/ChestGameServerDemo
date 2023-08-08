package com.example.chestGameServer.Exceptions;

import jakarta.annotation.Nullable;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RoomException extends UserException{
protected String roomId;

    public RoomException(String message, Exception e, String sessionId, String roomId) {
        super(message, e, sessionId);
        this.roomId = roomId;
    }
}
