package com.example.chestGameServer.Services;

import com.example.chestGameServer.Models.Abstract.AbstractChat;
import com.example.chestGameServer.Models.Abstract.AbstractEntity;
import com.example.chestGameServer.Repositories.AbstractRepository;
import com.example.chestGameServer.Repositories.AbstractRoomRepository;
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
public abstract class AbstractGenericService<E extends AbstractEntity,R extends AbstractRepository<E>> {
    R repository;
    public E save(E entity){return repository.save(entity);
    }
    public void deleteById(String id){
        repository.deleteById(id);
    }
    public E findById(String id){
        return repository.findById(id).get();
    }
}
