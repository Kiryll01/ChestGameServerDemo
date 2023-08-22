package com.example.chestGameServer.Services;

import com.example.chestGameServer.Models.User.User;
import com.example.chestGameServer.Repositories.UserRepository;
import com.example.chestGameServer.Exceptions.UserNotFoundException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

import java.util.List;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@RequiredArgsConstructor
@Log4j2
public class UserService {
    @EventListener(SessionConnectedEvent.class)
            public void sessionConnectedListener(SessionConnectedEvent event){
       log.info("new connection established : ");
    }
    @EventListener(SessionSubscribeEvent.class)
    public void sessionConnectEvent(SessionSubscribeEvent event){
        log.info("received new Subscribe frame");
    }


UserRepository userRepository;
public User save(User user){
   return userRepository.save(user);
}
public void deleteById(String id){
    userRepository.deleteById(id);
}
public User findById(String id) throws UserNotFoundException {
   return userRepository.findById(id).orElseThrow( ()-> new UserNotFoundException("",id) );
}
    public User findUserByNameAndPass(String name,String pass) throws UserNotFoundException {
        User user =userRepository.findUserByNameAndPass(name,pass);
        if(user==null) throw new UserNotFoundException(name+" was not found", name+" "+pass);
        return user;
    }
    public User findUserByName(String name)  {
        User user=userRepository.findUserByName(name);
        return user;
    }
    public List<User> getAll(){
    return userRepository.findAll();
    }
}
