package com.example.chestGameServer.Models.Game;

import com.example.chestGameServer.Exceptions.FullChatException;
import com.example.chestGameServer.Models.Abstract.AbstractChat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.redis.core.RedisHash;

import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@RedisHash("GameRooms")
@JsonIgnoreProperties(ignoreUnknown = true)
public class GameRoom extends AbstractChat<Player> {
    public GameRoom(String name) {
        super();
        super.setName(name);
        roomSizeLimit=4;
    }

    public GameRoom() {
        roomSizeLimit=4;
    }

    @Override
    public void setRoomSizeLimit(int roomSizeLimit) throws FullChatException {
       if(checkRoomSizeLimit(roomSizeLimit)) super.setRoomSizeLimit(roomSizeLimit);
       else throw new FullChatException("this type of chat can contain only 2-4 members",super.getId(),super.getName());
    }

    @Override
    public void setMembers(List<Player> members) throws FullChatException {
        super.setMembers(members);
        members.forEach(member->member.setRoomId(super.getId()));
    }

    @Override
    public void addMember(Player member) throws FullChatException {
        super.addMember(member);
        member.setRoomId(super.getId());
    }

    private static boolean checkRoomSizeLimit(int roomSizeLimit){
        if(roomSizeLimit>4 || roomSizeLimit<2) return false;
        return true;
    }

}
