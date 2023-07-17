package com.example.chestGameServer;

import com.example.chestGameServer.Models.User.User;
import com.example.chestGameServer.Models.User.UserStats;
import com.example.chestGameServer.Models.Game.FullChatException;
import com.example.chestGameServer.Models.Game.Player;
import com.example.chestGameServer.Models.Game.GameRoom;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Log4j2
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc(addFilters = false)
@FieldDefaults(level = AccessLevel.PROTECTED)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Transactional
@Getter
@ComponentScan(basePackages = {"com.example.chestGameServer.Repositories"})
public abstract class AbstractTestClass {
    @Autowired
    ObjectMapper mapper;
    @Value("${local.server.port}")
    String port;
    @Autowired
    MockMvc mockMvc;
    User user;
    GameRoom gameRoom;
    List<User> users;
    List<Player> players;
@BeforeAll
    public void setup() {
    user = User.builder()
            .name("max111")
            .userStats(new UserStats())
            .build();
    users=new ArrayList<>();

    users.addAll(List.of(user,
            new User(new UserStats(),"lera1313"),
            new User(new UserStats(),"anton1313"),
            new User(new UserStats(),"tanya162345")));

    players=new ArrayList<>();

    players.addAll(List.of(new Player("lera1313"),
            new Player("anton1313"),
            new Player("tanya162345")));

   gameRoom = new GameRoom("testGameRoom");

    try {
        gameRoom.setRoomSizeLimit(4);
        gameRoom.setMembers(players);
    } catch (FullChatException e) {
        throw new RuntimeException(e);
    }
}
}
