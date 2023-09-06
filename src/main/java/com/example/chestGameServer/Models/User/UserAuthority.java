package com.example.chestGameServer.Models.User;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.SecondaryRow;

import java.io.Serializable;
import java.util.Set;

@Entity(name = "_roles")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserAuthority implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    @Enumerated(EnumType.STRING)
    UserRoles userRole;
//    @ManyToMany(mappedBy = "userAuthorities",cascade = CascadeType.ALL)
//    @JsonBackReference
//    @ToString.Exclude
//    Set<User> user;
}
