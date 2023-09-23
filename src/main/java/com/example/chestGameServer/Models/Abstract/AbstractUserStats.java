package com.example.chestGameServer.Models.Abstract;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@SuperBuilder
@MappedSuperclass
public abstract class AbstractUserStats {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

}