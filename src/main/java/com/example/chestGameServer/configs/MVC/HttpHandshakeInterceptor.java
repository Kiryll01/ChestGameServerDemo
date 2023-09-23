package com.example.chestGameServer.configs.MVC;


import com.example.chestGameServer.Models.DTO.UserDTO;
import com.example.chestGameServer.Models.DTO.UserPrincipal;
import com.example.chestGameServer.Models.Enums.HttpAttributes;
import com.example.chestGameServer.Models.Factories.UserMapper;
import com.example.chestGameServer.Models.User.User;
import com.example.chestGameServer.Services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

//TODO : not used, because name and pass are not passed as attributes
@Log4j2
@RequiredArgsConstructor
public class HttpHandshakeInterceptor implements HandshakeInterceptor {
    private final UserService userService;

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
                                   Map attributes) throws Exception {
        HttpHeaders headers = request.getHeaders();
        if (!headers.containsKey(HttpAttributes.USER_NAME.getName()) || !headers.containsKey(HttpAttributes.USER_PASS.getName())) {
            response.setStatusCode(HttpStatusCode.valueOf(401));
            return false;
        }
        String name = headers.get(HttpAttributes.USER_NAME.getName()).toString();
        String pass = headers.get(HttpAttributes.USER_PASS.getName()).toString();
        User user = userService.findUserByNameAndPass(name, pass);
        if (user == null) {
            response.setStatusCode(HttpStatusCode.valueOf(401));
            return false;
        }
        UserDTO userDTO = UserMapper.USER_MAPPER.toUserDto(user);
        UserPrincipal userPrincipal = new UserPrincipal(userDTO, user.getRoles());
//        ServletServerHttpRequest servletRequest = (ServletServerHttpRequest) request;
//        HttpServletRequest httpServletRequest = servletRequest.getServletRequest();
//        HttpSession httpSession=httpServletRequest.getSession(false);
//        UserPrincipal userPrincipal= (UserPrincipal) httpSession.getAttribute(HttpAttributes.USER_NAME.getName());
        //if(userPrincipal==null) return false;
        attributes.put(HttpAttributes.USER_PRINCIPAL.getName(), userPrincipal);
        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {
        log.info("handshake was successful");
    }
}