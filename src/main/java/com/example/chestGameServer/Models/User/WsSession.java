package com.example.chestGameServer.Models.User;

import com.example.chestGameServer.Models.DTO.UserDTO;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;
import java.util.Set;

@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class WsSession {
UserDTO user;
@Builder.Default
boolean isInGame=false;
String gameRoomId;
@Builder.Default
final long createdAt= Instant.now().getEpochSecond();
}
