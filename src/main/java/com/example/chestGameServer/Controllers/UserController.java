package com.example.chestGameServer.Controllers;

import com.example.chestGameServer.Exceptions.UserNotFoundException;
import com.example.chestGameServer.Models.DTO.Requests.AuthRequest;
import com.example.chestGameServer.Models.DTO.UserDTO;
import com.example.chestGameServer.Models.DTO.UserPrincipal;
import com.example.chestGameServer.Models.Enums.DefaultAppProperties;
import com.example.chestGameServer.Models.Enums.HttpAttributes;
import com.example.chestGameServer.Models.Factories.UserMapper;
import com.example.chestGameServer.Models.User.User;
import com.example.chestGameServer.Services.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@RequiredArgsConstructor
@Log4j2
public class UserController {
    public static final String SIGN_UP="/user/auth/sign_up";
    public static final String SIGN_IN="/user/auth/sign_in";
    UserService userService;
    HttpUtils httpUtils;
    @PostMapping(SIGN_IN)
    public ResponseEntity<?> sign_in(@RequestBody AuthRequest authRequest,HttpServletResponse httpResponse) throws UserNotFoundException {
        User user =userService.findUserByNameAndPass(authRequest.getName(), authRequest.getPass());
        httpUtils.setUserPrincipal(UserPrincipal.builder().
                user(UserMapper.USER_MAPPER.toUserDto(user))
                        .build(),true);
        httpUtils.setUserPrincipal(UserPrincipal.builder().
                user(UserMapper.USER_MAPPER.toUserDto(user))
                .build(),true);

        httpUtils.setUserPrincipalCookie(httpResponse, user.getName(), user.getPass());

        return ResponseEntity.ok()
                .body(UserMapper.USER_MAPPER.toUserDto(user));
    }

    //TODO : add validation
    @PostMapping(SIGN_UP)
    public ResponseEntity<?> signUp(@RequestBody AuthRequest request, UriComponentsBuilder uriBuilder,
                                    HttpServletResponse httpResponse){

        User user=User.builder()
                .name(request.getName())
                .pass(request.getPass())
                .build();
        if(userService.findUserByName(request.getName())!=null)return ResponseEntity
                .badRequest()
                .body(request.getName()+" already exists");
        userService.save(user);

        URI uri=uriBuilder.scheme(DefaultAppProperties.SCHEME.getName())
                .host(DefaultAppProperties.HOST.getName())
                .path(MainOptionsController.RETRIEVE_USER)
                .buildAndExpand(request.getName()).toUri();
        UserDTO userDTO=UserMapper.USER_MAPPER.toUserDto(user);

        httpUtils.setUserPrincipal(UserPrincipal.builder().
                user(UserMapper.USER_MAPPER.toUserDto(user))
                .build(),true);

            httpUtils.setUserPrincipalCookie(httpResponse, user.getName(), user.getPass());

            return ResponseEntity.created(uri)
                .body(userDTO);
    }
}

