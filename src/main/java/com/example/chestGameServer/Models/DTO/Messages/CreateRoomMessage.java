package com.example.chestGameServer.Models.DTO.Messages;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class CreateRoomMessage {
String name;
int roomSizeLimit;
}
