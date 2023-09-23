package com.example.chestGameServer.Models.User;

import com.example.chestGameServer.Models.Abstract.AbstractUserStats;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@Entity
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class UserStats extends AbstractUserStats {
    @OneToOne(mappedBy = "userStats")
    @JsonBackReference
    @ToString.Exclude
    User user;
}
