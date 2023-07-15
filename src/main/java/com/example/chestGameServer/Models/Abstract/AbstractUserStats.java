package com.example.chestGameServer.Models.Abstract;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

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