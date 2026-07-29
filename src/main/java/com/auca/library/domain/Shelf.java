package com.auca.library.domain;

import jakarta.persistence.*;
import lombok.Data;
import java.util.UUID;

@Data
@Entity
public class Shelf {
    @Id
    private UUID shelfId = UUID.randomUUID();
    private String bookCategory;
    private int initialStock;
    private int availableStock;
    private int borrowedNumber;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "roomId")
    private Room room;
}
