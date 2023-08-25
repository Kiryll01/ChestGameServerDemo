package com.example.chestGameServer.Models.User;

import com.example.chestGameServer.Models.Abstract.AbstractUser;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Cascade;

import java.util.HashSet;
import java.util.Set;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
//@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "_user")
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@SuperBuilder()
public class User extends AbstractUser {
@OneToOne(cascade = CascadeType.ALL)
@JsonManagedReference
@JoinColumn(name = "user_stats_id")
UserStats userStats;
String pass;
@JsonManagedReference
@ManyToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
@JoinTable(name = "user_with_authorities",
joinColumns = @JoinColumn(name = "user_id"),
inverseJoinColumns = @JoinColumn(name = "authorities_id"))
Set<UserAuthority> userAuthorities;
//    @PrePersist
//    public void prePersist() {
//        if(userAuthorities == null)
//            userAuthorities =new HashSet<>();
//        UserAuthority userAuthority=new UserAuthority();
//        userAuthority.setUserRole(UserRoles.ROLE_USER);
//        userAuthorities.add(userAuthority);
//    }
public Set<UserRoles> getRoles(){
   Set<UserRoles> userRoles=new HashSet<>();
   userAuthorities.forEach(userAuthority -> userRoles.add(userAuthority.getUserRole()));
   return userRoles;
}
public void addRole(UserRoles userRoles){
    userAuthorities.add(UserAuthority.builder().userRole(userRoles).build());
}
    public User(UserStats userStats,String name) {
        super(name);
        this.userStats = userStats;
    }

    public User(String name, UserStats userStats, String pass) {
        super(name);
        this.userStats = userStats;
        this.pass = pass;
    }
}
