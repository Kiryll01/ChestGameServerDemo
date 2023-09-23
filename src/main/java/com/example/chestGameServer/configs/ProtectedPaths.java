package com.example.chestGameServer.configs;

import com.example.chestGameServer.Controllers.WebSocket.GameProcessController;
import com.example.chestGameServer.Models.User.UserRoles;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

@Getter
@Component
public class ProtectedPaths {
    public static final HashMap<String, Set<UserRoles>> protectedHttpPaths = new HashMap<>(10);
    public static final Set<String> gameProcessDestinations = new HashSet<>(10);

    static {
        httpConfig();
        wsConfig();
    }

    public static void httpConfig() {
        //protectedPaths.put(WebSocketConfig.REGISTRY,Set.of(UserRoles.ROLE_USER));
    }

    public static void wsConfig() {
        //protectedPaths.put(WebSocketConfig.REGISTRY,Set.of(UserRoles.ROLE_USER));
        gameProcessDestinations.add(GameProcessController.PATTERN);
    }
}
