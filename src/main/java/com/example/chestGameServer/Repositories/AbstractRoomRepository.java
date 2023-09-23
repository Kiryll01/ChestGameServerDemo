package com.example.chestGameServer.Repositories;

import com.example.chestGameServer.Models.Abstract.AbstractChat;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;

@NoRepositoryBean
public interface AbstractRoomRepository<C extends AbstractChat> extends CrudRepository<C, String> {
    List<C> findAllByName(String name);
}
