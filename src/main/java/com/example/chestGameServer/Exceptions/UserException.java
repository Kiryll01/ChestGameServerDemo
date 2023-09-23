package com.example.chestGameServer.Exceptions;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@ToString(callSuper = true)
public class UserException extends AppException {
    Exception e;
    String sessionId;

    public UserException(String message, Exception e, String sessionId) {
        super(message);
        this.e = e;
        this.sessionId = sessionId;
    }
}
