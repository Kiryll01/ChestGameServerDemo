package com.example.chestGameServer.Repositories;

import com.example.chestGameServer.Models.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,String> {

}
