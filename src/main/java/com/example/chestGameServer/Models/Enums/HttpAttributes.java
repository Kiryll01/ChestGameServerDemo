package com.example.chestGameServer.Models.Enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum HttpAttributes {
    USER_PRINCIPAL("user_principal"),
    USER_NAME("user_name"),
    USER_PASS("user_pass");
    private final String name;
}
