package com.example.chestGameServer.Services;

import lombok.Getter;

@Getter
public class ObjectNotFoundException extends Exception {
    private final Object searchParam;
    public ObjectNotFoundException(String message, Object searchParam,Class clazz) {
        super(clazz.getName()+ " object not found by param " + searchParam + " with message " + message);
        this.searchParam = searchParam;
    }
}
