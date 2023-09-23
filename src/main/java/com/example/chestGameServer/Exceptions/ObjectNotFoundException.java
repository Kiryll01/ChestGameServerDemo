package com.example.chestGameServer.Exceptions;

import lombok.Getter;

@Getter
public class ObjectNotFoundException extends AppException {
    private final Object searchParam;

    public ObjectNotFoundException(String message, Object searchParam, Class clazz) {
        super(clazz.getName() + " object not found by param " + searchParam + " with message " + message);
        this.searchParam = searchParam;
    }
}
