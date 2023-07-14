package com.example.chestGameServer.Models.Game;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FullChatException extends Exception{
    String chatId;
    String chatName;
    public FullChatException(String message,String chatId, String chatName) {
        super(message);
        this.chatId = chatId;
        this.chatName = chatName;
    }

    public FullChatException(String chatId, String chatName) {
        this.chatId = chatId;
        this.chatName = chatName;
    }
}
