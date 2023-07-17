package com.example.chestGameServer.Models.Factories;

import com.example.chestGameServer.Models.DTO.UserStatsDto;
import com.example.chestGameServer.Models.User.User;
import com.example.chestGameServer.Models.User.UserStats;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserStatsMapper {
UserStatsDto toUserStatsDto(User user);
UserStats toUserStats(UserStatsDto userStatsDto);
}
