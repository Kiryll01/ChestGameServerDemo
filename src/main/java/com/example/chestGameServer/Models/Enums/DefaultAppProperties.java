package com.example.chestGameServer.Models.Enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DefaultAppProperties {
    PORT("8080"),
    DEFAULT_URL("http://localhost:8080");
    private final String name;
}
