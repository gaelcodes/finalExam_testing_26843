package com.auca.library.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;
import java.util.UUID;

@Data
@Entity
public class Room {
    @Id
    private UUID roomId = UUID.randomUUID();
    private String roomCode;
}
