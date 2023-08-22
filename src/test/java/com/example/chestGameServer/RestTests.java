package com.example.chestGameServer;


import com.example.chestGameServer.Controllers.MainOptionsController;
import com.example.chestGameServer.Controllers.UserController;
import com.example.chestGameServer.Models.DTO.Requests.AuthRequest;
import com.example.chestGameServer.Models.User.User;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;


import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Log4j2
@FieldDefaults(level = AccessLevel.PROTECTED)
@Getter
public class RestTests extends AbstractTestClass{
    User userToSave;
    @BeforeAll
    public void setup(){
        userToSave=User.builder()
                .name("pavel")
                .pass("i_am_pavel")
                .build();
        userService.save(userToSave);
    }
    @Test
    public void signUpTest() throws Exception {
        AuthRequest authRequest =new AuthRequest("max","maximus123");
        MvcResult result=mockMvc.perform(MockMvcRequestBuilders.post(UserController.SIGN_UP)
                        .content(mapper.writeValueAsString(authRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();
        log.info(result.getResponse().getContentAsString());
        Assertions.assertTrue(result.getResponse().getContentAsString().contains("max"));
        Assertions.assertTrue(result.getResponse().getCookie("user_name").getValue().equals("max"));
    }
    @Test
    public void retrieveUserTest() throws Exception {
        MvcResult result=mockMvc.perform(MockMvcRequestBuilders.get(MainOptionsController.RETRIEVE_USER,"pavel")
                        .contentType(MediaType.APPLICATION_JSON))
                //.headers(httpHeaders))
                .andExpect(status().isOk())
                .andReturn();
        log.info(result.getResponse().getContentAsString());
    }
    @Test
    public void getAccountInf() throws Exception {
        HttpHeaders httpHeaders=new HttpHeaders();
        httpHeaders.setBasicAuth(userToSave.getName(),userToSave.getPass());
        MvcResult result=mockMvc.perform(MockMvcRequestBuilders.get(MainOptionsController.GET_ACCOUNT_INFO)
                        .contentType(MediaType.APPLICATION_JSON)
                        .headers(httpHeaders))
                .andExpect(status().isOk())
                .andReturn();
        log.info(result.getResponse().getContentAsString());
    }
}
