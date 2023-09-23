package com.example.chestGameServer.Exceptions;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FullChatException extends AppException {
    String chatId;
    String chatName;

    public FullChatException(String message, String chatId, String chatName) {
        super(message);
        this.chatId = chatId;
        this.chatName = chatName;
    }

    public FullChatException(String chatId, String chatName) {
        super();
        this.chatId = chatId;
        this.chatName = chatName;
    }
}
