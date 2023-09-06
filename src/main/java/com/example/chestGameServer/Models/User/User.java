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
@Entity(name = "_user")
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@SuperBuilder()
@NoArgsConstructor
public class User extends AbstractUser {
    @OneToOne(cascade = CascadeType.ALL)
    @JsonManagedReference
    @JoinColumn(name = "user_stats_id")
    UserStats userStats;
    String pass;
    @Enumerated(EnumType.STRING)
    Set<UserRoles> roles;
    public void addRole(UserRoles userRole){
        roles.add(userRole);
    }
    public void deleteRole(UserRoles userRole){
        roles.remove(userRole);
    }
    public User(UserStats userStats, String name) {
        super(name);
        //setDefaultRole();
        this.userStats = userStats;
    }

    public User(String name, UserStats userStats, String pass) {
        super(name);
        //setDefaultRole();
        this.userStats = userStats;
        this.pass = pass;
    }
//    @JsonManagedReference
//    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
//    @JoinTable(name = "user_with_authorities",
//            joinColumns = @JoinColumn(name = "user_id",referencedColumnName = "id"),
//            inverseJoinColumns = @JoinColumn(name = "authorities_id",referencedColumnName = "id"))
//    Set<UserAuthority> userAuthorities;
//    private void setDefaultRole() {
//        UserAuthority userAuthority = new UserAuthority();
//        userAuthority.setUserRole(UserRoles.ROLE_USER);
//        userAuthorities = Set.of(userAuthority);
//    }
//    @PrePersist
//    public void onPrePersist() {
//        if(userAuthorities == null)
//            userAuthorities =new HashSet<>();
//        UserAuthority userAuthority=new UserAuthority();
//        userAuthority.setUserRole(UserRoles.ROLE_USER);
//        setUserAuthorities(Set.of(userAuthority));
//    }
//    public Set<UserRoles> getRoles() {
//        Set<UserRoles> userRoles = new HashSet<>();
//        userAuthorities.forEach(userAuthority -> userRoles.add(userAuthority.getUserRole()));
//        return userRoles;
//    }
//
//    public void addRole(UserRoles userRoles) {
//        userAuthorities.add(UserAuthority.builder().userRole(userRoles).build());
//    }
//    public static abstract class UserBuilder<C extends User, B extends UserBuilder<C, B>>
//            extends AbstractUserBuilder<C, B> {
//private String name;
//private Set<UserAuthority> userAuthorities;
//        public B name(String name){
//            this.name=name;
////            UserAuthority userAuthority = new UserAuthority();
////            userAuthority.setUserRole(UserRoles.ROLE_USER);
////            this.userAuthorities=Set.of(userAuthority);
//            return self();
//        }
//    }
}
