package com.example.chestGameServer.Exceptions;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GameProcessException extends AppException {
    String roomId;
    String messageDestination;

    public GameProcessException(String message, String roomId, String messageDestination) {
        super(message + " roomId: " + roomId + " messageDestination: " + messageDestination);
        this.roomId = roomId;
        this.messageDestination = messageDestination;
    }


}
