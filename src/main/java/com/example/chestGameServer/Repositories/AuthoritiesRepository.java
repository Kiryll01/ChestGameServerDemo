package com.example.chestGameServer.Repositories;

import com.example.chestGameServer.Models.User.UserAuthority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface AuthoritiesRepository extends CrudRepository<UserAuthority,String> {
    
}
