package com.example.chestGameServer;

import com.example.chestGameServer.Models.User.User;
import com.example.chestGameServer.Models.User.UserStats;
import com.example.chestGameServer.Repositories.UserRepository;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Log4j2
public class EmbeddedDBTests {
    @Autowired
    UserRepository userRepository;
    User userToSave;
    @BeforeAll
    public void setup(){
        userToSave = User.builder()
                .name("max111")
                .userStats(new UserStats())
                .build();
    }
    @Test
    public void testingUserSave(){
        User userFromDb= userRepository.save(userToSave);
        log.info(userToSave);
        log.info(userFromDb);
        Assertions.assertEquals(userToSave,userFromDb);
    }
    @Test
    public void testingFindUserByName(){
        userRepository.save(userToSave);
        User userFromDb=userRepository.findUserByName(userToSave.getName());
        Assertions.assertNotNull(userFromDb);
        Assertions.assertEquals(userFromDb,userToSave);
    }
}
