package com.example.chestGameServer.Exceptions;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PROTECTED,makeFinal = true)
@Component
public class ExceptionUtils {
    SimpMessagingTemplate messagingTemplate;
public void sendExceptionToUser(String simpSessionId,String userPrefixedDestination,AppException exception){
String message=exception.getMessage();
messagingTemplate.convertAndSendToUser(simpSessionId,userPrefixedDestination,message);
}
}
