package com.example.chestGameServer.Models.Factories;

import com.example.chestGameServer.Models.DTO.UserDTO;
import com.example.chestGameServer.Models.User.User;
import com.example.chestGameServer.Models.Game.Player;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    //@Mapping(target = "id",ignore = true)
    @Mapping(source = "userStats",target = "userStats")
    UserDTO toUserDto(User user);
    @Mapping(source = "userStats",target = "userStats")
    User toUser(UserDTO userDTO);
    Player toPlayer(UserDTO userDTO);
    UserDTO toUserDto(Player player);


}
