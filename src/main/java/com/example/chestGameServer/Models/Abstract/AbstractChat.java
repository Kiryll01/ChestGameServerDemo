package com.example.chestGameServer.Models.Abstract;

import com.example.chestGameServer.Exceptions.FullChatException;
import jakarta.persistence.Id;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import org.springframework.data.redis.core.index.Indexed;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@FieldDefaults(level = AccessLevel.PROTECTED)
@Data
@NoArgsConstructor
@SuperBuilder
public abstract class AbstractChat<M extends AbstractUser> implements Serializable{
    @Id
    String id= UUID.randomUUID().toString().substring(0,4);
    @Indexed
    String name;
    String ownerId;
    List<M> members=new ArrayList<>();
    int roomSizeLimit=1000;

    public void setRoomSizeLimit(int roomSizeLimit) throws FullChatException {
        this.roomSizeLimit = roomSizeLimit;
    }

    public void deleteMember(String id){
        for (M member : members) {
            if(member.getId().equals(id)) members.remove(member);
        }
    }
    public void setMembers(List<M> members) throws FullChatException{
        if(members.size()>roomSizeLimit) throw new FullChatException("too much members",getId(),name);
        this.members=members;
    }
    public List<M> getMembers() {
        List<M> copyList=new ArrayList<>(roomSizeLimit);
        members.forEach(member->copyList.add(member));
        return copyList;
    }
    public void addMember(M member) throws FullChatException {
        if(isRoomSizeLimitReached()) throw new FullChatException("roomSizeLimit is reached",getId(),name);
        members.add(member);
    }
    public AbstractChat(List<M> members) {
        this.members = members;
    }

    public boolean isRoomSizeLimitReached(){
        if(members.size()==roomSizeLimit)return true;
    return false;
    }
}
