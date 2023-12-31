package com.example.chestGameServer.Repositories;

import com.example.chestGameServer.Models.User.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
    User findUserByNameAndPass(String name, String pass);

    User findUserByName(String name);
}
