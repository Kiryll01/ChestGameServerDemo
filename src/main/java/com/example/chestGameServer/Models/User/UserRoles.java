package com.example.chestGameServer.Models.User;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserRoles {
    ROLE_ANONYMOUS("ANONYMOUS"),
    ROLE_USER("USER"),
    ROLE_UPGRADED_USER("UPGRADED_USER"),
    ROLE_ADMIN("ADMIN");
   private final String name;
}
