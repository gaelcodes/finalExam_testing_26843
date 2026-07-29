package com.auca.library.domain;

import jakarta.persistence.*;
import lombok.Data;
import java.util.UUID;
import java.time.LocalDate;
import java.math.BigDecimal;

@Data
@Entity
public class Borrower {
    @Id
    private UUID id = UUID.randomUUID();
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "readerId")
    private User reader;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bookId")
    private Book book;
    
    private LocalDate pickupDate;
    private LocalDate dueDate;
    private LocalDate returnDate;
    private BigDecimal fine;
    private BigDecimal lateChargeFees;
}
