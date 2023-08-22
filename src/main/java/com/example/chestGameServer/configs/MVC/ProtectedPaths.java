package com.example.chestGameServer.configs.MVC;

import com.example.chestGameServer.Models.User.UserRoles;
import com.example.chestGameServer.configs.WebSocketConfig;
import lombok.Data;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

@Getter
@Component
public class ProtectedPaths {
    public static final HashMap<String,Set<UserRoles>> protectedPaths=new HashMap<>(10);

    static {
        config();
    }
    public static void config(){
        protectedPaths.put(WebSocketConfig.REGISTRY,Set.of(UserRoles.ROLE_USER));
    
    }
}
