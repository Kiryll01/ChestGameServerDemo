package com.example.chestGameServer.Models.DTO.Events;

import com.example.chestGameServer.Models.Abstract.AbstractChat;
import com.example.chestGameServer.Models.Abstract.AbstractUser;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@FieldDefaults(level = AccessLevel.PROTECTED)
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@NoArgsConstructor
@SuperBuilder(builderMethodName = "memberJoinChat")
public class MemberJoinChatEvent<C extends AbstractChat, U extends AbstractUser> extends ChatEvent<C> {
    U user;

    public MemberJoinChatEvent(C chat, String message, U user) {
        super(chat, message);
        this.user = user;
    }
}
