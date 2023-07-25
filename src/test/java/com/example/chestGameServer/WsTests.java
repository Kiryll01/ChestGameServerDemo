package com.example.chestGameServer;

import com.example.chestGameServer.Controllers.MainOptionsController;
import com.example.chestGameServer.Controllers.WebSocket.GameRoomController;
import com.example.chestGameServer.Controllers.WebSocket.MemberWsController;
import com.example.chestGameServer.Models.DTO.Messages.CreateRoomMessage;
import com.example.chestGameServer.Models.Game.GameRoom;
import com.example.chestGameServer.configs.WebSocketConfig;
import lombok.extern.log4j.Log4j2;

import lombok.*;
import lombok.experimental.FieldDefaults;

import org.apache.logging.log4j.util.Strings;
import org.junit.jupiter.api.*;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.redis.connection.Subscription;
import org.springframework.lang.NonNull;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.*;

import org.springframework.test.web.servlet.MockMvc;

import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@Log4j2
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WsTests extends AbstractTestClass{
    WebClient client;
    @BeforeAll
    public void setupWebSockets() throws Exception {

        RunStopFrameHandler runStopFrameHandler = new RunStopFrameHandler(new CompletableFuture<>());

        String wsUrl = "ws://127.0.0.1:" + port + WebSocketConfig.REGISTRY;

        WebSocketStompClient stompClient = new WebSocketStompClient(new SockJsClient(createTransportClient()));

        stompClient.setMessageConverter(new MappingJackson2MessageConverter());

        StompSession stompSession = stompClient
                .connect(wsUrl, new StompSessionHandlerAdapter() {})
                .get(5, TimeUnit.SECONDS);

        //Thread.sleep(5000);

        client = WebClient.builder()
                .stompClient(stompClient)
                .stompSession(stompSession)
                .handler(runStopFrameHandler)
                .build();

    }
@Test
public void createRoomTest() throws Exception  {

        StompSession session = client.getStompSession();
    CreateRoomMessage createRoomMessage = new CreateRoomMessage("Zahodite_bratiya_igrat", 4, user.getId());
    session.send(WebSocketConfig.APPLICATION_DESTINATION_PREFIX+GameRoomController.CREATE_GAME_ROOM, createRoomMessage);
         RunStopFrameHandler handler=new RunStopFrameHandler(new CompletableFuture<>());
        session.subscribe(WebSocketConfig.TOPIC_DESTINATION_PREFIX+GameRoomController.FETCH_CREATE_GAME_ROOM_EVENT,handler);

    byte[] payload= (byte[]) handler.getFuture().get();
    GameRoom responseGameRoom=mapper.readValue(payload, GameRoom.class);
    Assertions.assertEquals(createRoomMessage.getName(),responseGameRoom.getName(),"names should be equal");
    System.out.println("received payload:");

    log.info(responseGameRoom);

    String user_id=user.getId();

        String contentAsString = mockMvc
            .perform(MockMvcRequestBuilders.get(MainOptionsController.FETCH_ROOMS,user_id))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();

    log.info(contentAsString);

    };
    @Test
    public void createBreakRoomTest() throws Exception {
        StompSession session = client.getStompSession();

        String sessionId=session.getSessionId();

        CreateRoomMessage createRoomMessage = new CreateRoomMessage("newGameRoom", 7, sessionId);
        RunStopFrameHandler handler=new RunStopFrameHandler(new CompletableFuture<>());
        session.send(WebSocketConfig.APPLICATION_DESTINATION_PREFIX+GameRoomController.CREATE_GAME_ROOM,createRoomMessage);

        log.info(sessionId);

        StompSession.Subscription subscription=session.subscribe(
                WebSocketConfig.TOPIC_DESTINATION_PREFIX+ MemberWsController.FETCH_ROOM_EXCEPTIONS.replace("{username}",sessionId)
                ,handler);
      log.info(subscription.getSubscriptionHeaders());
        session.send(WebSocketConfig.APPLICATION_DESTINATION_PREFIX+GameRoomController.CREATE_GAME_ROOM,createRoomMessage);
        byte[] payload= (byte[]) handler.getFuture().get();
        String message=mapper.readValue(payload, String.class);
        log.info(message);
        Assertions.assertTrue(message.contains("this type of chat can contain only 2-4 members"));
    }


@Test
public void joinRoomTest() throws Exception{

}
    @AfterAll
    public void tearDown() {
gameRoomService.deleteAll();
        if (client.getStompSession().isConnected()) {
            client.getStompSession().disconnect();
            client.getStompClient().stop();
        }
    }


    private List<Transport> createTransportClient() {

        List<Transport> transports = new ArrayList<>(1);

        transports.add(new WebSocketTransport(new StandardWebSocketClient()));

        return transports;
    }

    @Data
    @AllArgsConstructor
    @FieldDefaults(level = AccessLevel.PRIVATE)
    private class RunStopFrameHandler implements StompSessionHandler {
        CompletableFuture<Object> future;

        @Override
        public @NonNull Type getPayloadType(StompHeaders stompHeaders) {

            //log.info(stompHeaders.toString());

            return byte[].class;
        }

        @Override
        public void handleFrame(@NonNull StompHeaders stompHeaders, Object o) {

            log.info(stompHeaders.toString());

            future.complete(o);

            //future=new CompletableFuture<>();
        }

        @Override
        public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
            log.info("connected");
        }

        @Override
        public void handleException(StompSession session, StompCommand command, StompHeaders headers, byte[] payload, Throwable exception) {
            log.error("caught ws exception");
            System.out.println(command);
            System.out.println(headers);
            System.out.println(payload);
            System.out.println(exception);
        }

        @Override
        public void handleTransportError(StompSession session, Throwable exception) {
            log.error(exception);
        }
    }

    @Data
    @Builder
    @FieldDefaults(level = AccessLevel.PRIVATE)
    private static class WebClient {

        WebSocketStompClient stompClient;

        StompSession stompSession;

        String sessionToken;

        RunStopFrameHandler handler;
    }
}

