package com.example.chestGameServer.Exceptions;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class AppException extends Exception {

    public AppException(String message) {
        super(message);
    }

}
