package com.example.chestGameServer.Models.Factories;

import com.example.chestGameServer.Models.DTO.UserDTO;
import com.example.chestGameServer.Models.Game.Player;
import com.example.chestGameServer.Models.User.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserMapper USER_MAPPER = Mappers.getMapper(UserMapper.class);

    //@Mapping(target = "id",ignore = true)
    @Mapping(source = "userStats", target = "userStats")
    UserDTO toUserDto(User user);

    @Mapping(source = "userStats", target = "userStats")
    User toUser(UserDTO userDTO);

    Player toPlayer(UserDTO userDTO);

    UserDTO toUserDto(Player player);

}
