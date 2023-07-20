package com.example.chestGameServer.Exceptions;

import jakarta.annotation.Nullable;

public class UserNotFoundException extends ObjectNotFoundException{

    public UserNotFoundException(@Nullable String message, Object searchParam) {
        super(message, searchParam, UserNotFoundException.class);
    }
}
