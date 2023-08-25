package com.example.chestGameServer.configs;

import com.example.chestGameServer.Exceptions.AppException;
import com.example.chestGameServer.Exceptions.UserNotFoundException;
import com.example.chestGameServer.Models.DTO.UserDTO;
import com.example.chestGameServer.Models.DTO.UserPrincipal;
import com.example.chestGameServer.Models.Factories.UserMapper;
import com.example.chestGameServer.Models.User.User;
import com.example.chestGameServer.Models.User.UserRoles;
import com.example.chestGameServer.Services.UserService;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class WebSocketAuthenticationService {
    private final UserService userService;
    public UserPrincipal getAuthenticationOrFail(final String username, final String password) throws AppException {
        if (username == null || username.trim().isEmpty()) {
            throw new NullPointerException("Username was null or empty.");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new NullPointerException("Password was null or empty.");
        }
        User user=userService.findUserByNameAndPass(username, password);
        Set<UserRoles> roles=user.getRoles();
        UserDTO userDTO= UserMapper.USER_MAPPER.toUserDto(user);
        return new UserPrincipal(userDTO,roles);
    }

}
