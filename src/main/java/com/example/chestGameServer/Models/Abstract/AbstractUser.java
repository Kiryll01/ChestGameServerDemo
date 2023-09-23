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

import java.io.Serializable;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@NoArgsConstructor
@MappedSuperclass
@SuperBuilder()
public abstract class AbstractUser implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    String name;

    public AbstractUser(String name) {
        this.name = name;
    }
}
