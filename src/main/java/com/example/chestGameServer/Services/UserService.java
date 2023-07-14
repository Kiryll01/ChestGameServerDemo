package com.example.chestGameServer.Services;

import com.example.chestGameServer.Models.Entities.User;
import com.example.chestGameServer.Repositories.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@RequiredArgsConstructor
public class UserService {
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
}
