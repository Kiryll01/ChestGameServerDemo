package com.example.chestGameServer.Exceptions;

import com.example.chestGameServer.Models.Abstract.AbstractChat;

public class RoomNotFoundException extends ObjectNotFoundException {
    public RoomNotFoundException(String message, Object searchParam, Class<? extends AbstractChat> clazz) {
        super(message, searchParam, clazz);
    }
}
