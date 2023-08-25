package com.example.chestGameServer.configs;

import com.example.chestGameServer.Exceptions.AppException;
import com.example.chestGameServer.Models.DTO.UserPrincipal;
import com.example.chestGameServer.Models.Enums.HttpAttributes;
import com.example.chestGameServer.Models.User.User;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthChannelInterceptorAdapter implements ChannelInterceptor {
    private final WebSocketAuthenticationService webSocketAuthenticationService;

    @Override
    public Message<?> preSend(final Message<?> message, final MessageChannel channel) {
        final StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        if (StompCommand.CONNECT == accessor.getCommand()) {
            final String username = accessor.getFirstNativeHeader(HttpAttributes.USER_NAME.name());
            final String password = accessor.getFirstNativeHeader(HttpAttributes.USER_PASS.name());
            try {
                UserPrincipal user=webSocketAuthenticationService.getAuthenticationOrFail(username,password);
            accessor.setHeader(HttpAttributes.USER_PRINCIPAL.getName(), user);
            } catch (AppException e) {

            }
        }
        return message;
    }
}
