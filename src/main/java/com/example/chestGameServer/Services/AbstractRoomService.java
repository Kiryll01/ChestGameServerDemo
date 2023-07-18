package com.example.chestGameServer.Services;

import com.example.chestGameServer.Models.Abstract.AbstractChat;
import com.example.chestGameServer.Repositories.AbstractRoomRepository;
import com.example.chestGameServer.Services.Exceptions.RoomNotFoundException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@FieldDefaults(level = AccessLevel.PROTECTED,makeFinal = true)
@RequiredArgsConstructor
@Log4j2
public abstract class AbstractRoomService<C extends AbstractChat,R extends AbstractRoomRepository<C>> {
    final Class<C> typeChatClass;
    R repository;
public Iterable<C> findAll(){return repository.findAll();}
public C save(C chat){
return repository.save(chat);
}
public void deleteById(String id){
    repository.deleteById(id);
}
public C findById(String id) throws RoomNotFoundException {
    return repository.findById(id).orElseThrow(()->new RoomNotFoundException("room does not exist",id,typeChatClass));
}
public List<C> findAllByName(String name){
    return repository.findAllByName(name);
}
}
