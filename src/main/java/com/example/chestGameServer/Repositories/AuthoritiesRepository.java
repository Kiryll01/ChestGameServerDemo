package com.example.chestGameServer.Repositories;

import com.example.chestGameServer.Models.User.UserAuthority;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthoritiesRepository extends JpaRepository<UserAuthority,String>{
}
