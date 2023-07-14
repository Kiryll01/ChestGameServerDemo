package com.example.chestGameServer.Models.Abstract;

import com.example.chestGameServer.Models.Game.FullChatException;
import com.example.chestGameServer.Models.Game.Player;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@FieldDefaults(level = AccessLevel.PROTECTED)
@Data
@NoArgsConstructor
public abstract class AbstractChat<M extends AbstractUser> implements Serializable {
    final String id= UUID.randomUUID().toString().substring(0,4);
    String name;
    List<M> members=new ArrayList<>();
     int roomSizeLimit;

    public void setRoomSizeLimit(int roomSizeLimit) throws FullChatException {
        this.roomSizeLimit = roomSizeLimit;
    }

    public void deleteMember(String id){
        for (M member : members) {
            if(member.getId().equals(id)) members.remove(member);
        }
    }
    public void setMembers(List<M> members) throws FullChatException{
        if(members.size()>roomSizeLimit) throw new FullChatException("too much members",id,name);
        this.members=members;
    }
    public List<M> getMembers() {
        List<M> copyList=new ArrayList<>(roomSizeLimit);
        members.forEach(member->copyList.add(member));
        return copyList;
    }
    public void addMember(M member) throws FullChatException {
        if(isRoomSizeLimitReached()) throw new FullChatException("roomSizeLimit is reached",id,name);
        members.add(member);
    }
    public AbstractChat(List<M> members) {
        this.members = members;
    }

    protected boolean isRoomSizeLimitReached(){
        if(members.size()==roomSizeLimit)return true;
    return false;
    }
}
