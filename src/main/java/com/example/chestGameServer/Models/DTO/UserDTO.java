package com.example.chestGameServer.Models.DTO;

import com.example.chestGameServer.Models.Abstract.AbstractUser;
import lombok.*;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class UserDTO extends AbstractUser {
    String inf;
}
