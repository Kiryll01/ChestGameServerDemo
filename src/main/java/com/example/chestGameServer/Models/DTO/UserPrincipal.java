package com.example.chestGameServer.Models.DTO;

import com.example.chestGameServer.Models.Abstract.AbstractUser;
import com.example.chestGameServer.Models.User.UserRoles;
import jakarta.persistence.Entity;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.util.Set;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserPrincipal  {
UserDTO user;
Set<UserRoles> userRoles;
// set by the server
String wsSessionId;

    public UserPrincipal(UserDTO user, Set<UserRoles> userRoles) {
        this.user = user;
        this.userRoles = userRoles;
    }
}
