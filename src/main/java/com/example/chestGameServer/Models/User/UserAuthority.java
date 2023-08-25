package com.example.chestGameServer.Models.User;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.Cascade;

import java.util.Set;

@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserAuthority {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    @Enumerated(EnumType.STRING)
    UserRoles userRole;
    @ManyToMany(mappedBy = "userAuthorities",cascade = CascadeType.ALL)
    @JsonBackReference
   // @ToString.Exclude
    Set<User> user;
}
