package com.example.chestGameServer.configs.WS;

import com.example.chestGameServer.Controllers.WebSocket.WsUtils;
import com.example.chestGameServer.Exceptions.AppException;
import com.example.chestGameServer.Exceptions.AppMessageException;
import com.example.chestGameServer.Exceptions.UserNotFoundException;
import com.example.chestGameServer.Models.DTO.UserPrincipal;
import com.example.chestGameServer.Models.Enums.HttpAttributes;
import com.example.chestGameServer.Models.Factories.UserMapper;
import com.example.chestGameServer.Models.User.User;
import com.example.chestGameServer.Models.User.WsSession;
import com.example.chestGameServer.Services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.util.Strings;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Log4j2
public class AuthChannelInterceptorAdapter implements ChannelInterceptor {
    private final UserService userService;
    @Override
    public Message<?> preSend(final Message<?> message, final MessageChannel channel) {
        final StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
      log.info(accessor.getHeader(SimpMessageHeaderAccessor.SESSION_ID_HEADER));
        if (StompCommand.CONNECT ==accessor.getCommand()) {
            final String username = accessor.getFirstNativeHeader(HttpAttributes.USER_NAME.name());
            final String password = accessor.getFirstNativeHeader(HttpAttributes.USER_PASS.name());
            try {
                User user=userService.findUserByNameAndPass(username,password);
                WsUtils.wsSessionsMap.put(accessor.getSessionId(), WsSession.builder()
                                .user(UserMapper.USER_MAPPER.toUserDto(user)).build());
            } catch (UserNotFoundException e) {
                throw new AppMessageException(message,e);
            }
        }
        return message;
    }
}
