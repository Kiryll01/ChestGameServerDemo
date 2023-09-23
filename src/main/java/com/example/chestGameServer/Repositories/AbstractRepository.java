package com.example.chestGameServer.Repositories;

import com.example.chestGameServer.Models.Abstract.AbstractEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface AbstractRepository<E extends AbstractEntity> extends CrudRepository<E, String> {
}
