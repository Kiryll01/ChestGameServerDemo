package com.example.chestGameServer.Models.User;

import com.example.chestGameServer.Models.Abstract.AbstractUser;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

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
