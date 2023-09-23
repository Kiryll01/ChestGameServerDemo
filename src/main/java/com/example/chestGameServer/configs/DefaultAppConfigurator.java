package com.example.chestGameServer.configs;

import com.example.chestGameServer.Models.User.User;
import com.example.chestGameServer.Models.User.UserRoles;
import com.example.chestGameServer.Models.User.UserStats;
import com.example.chestGameServer.Services.UserService;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Configuration;

import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

@Configuration
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
@Log4j2
public class DefaultAppConfigurator {
    final UserService userService;
    public static final String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789~`!@#$%^&*()-_=+[{]}\\|;:\'\",<.>/?";
    private User user;

    @PostConstruct
    public void fillEmbeddedDb() {
        String pass = new String();
        for (int i = 0; i < 15; i++)
            pass += characters.charAt(ThreadLocalRandom.current().nextInt(characters.length() - 1));
        user = User.builder()
                .name("testUser")
                .userStats(new UserStats())
                .roles(Set.of(UserRoles.ROLE_USER))
                .pass(pass)
                .build();
        userService.save(user);
        log.info("default user is saved with pass " + pass + " and role " + user.getRoles());

    }

    @PreDestroy
    public void cleanDb() {
        userService.deleteById(user.getId());
        log.info("db is cleaned");
    }
}
