package com.example.chestGameServer.Models.DTO.Messages;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class CreateRoomMessage {
String name;
int roomSizeLimit;
String userId;
}
