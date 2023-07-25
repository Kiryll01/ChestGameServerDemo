package com.example.chestGameServer.Exceptions;

import jakarta.annotation.Nullable;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RoomException extends AppException{
    protected AppException exception;
@Nullable
protected String sessionId;
@Nullable
protected String roomId;
    public RoomException(String message, String sessionId) {
        super(message);
        this.sessionId = sessionId;
    }

    public RoomException(String message, String sessionId, String roomId, AppException exception) {
        super(message);
        this.sessionId = sessionId;
        this.roomId = roomId;
        this.exception=exception;
    }
}
