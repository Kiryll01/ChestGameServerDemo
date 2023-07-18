package com.example.chestGameServer.Models.DTO.Events;

import com.example.chestGameServer.Models.Abstract.AbstractChat;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder(builderMethodName = "chatEventBuilder")
public class ChatEvent <C extends AbstractChat>{
C chat;
String message;
}
