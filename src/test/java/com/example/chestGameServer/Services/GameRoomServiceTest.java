package com.example.chestGameServer.Services;

import com.example.chestGameServer.AbstractTestClass;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;
@Log4j2
@FieldDefaults(level = AccessLevel.PROTECTED)
class GameRoomServiceTest extends AbstractTestClass {
@Autowired
GameRoomService service;
    @Test
    void save() {

    }

    @Test
    void deleteById() {
    }

    @Test
    void findById() {

    }

    @Test
    void findAllByName() {
    }
}