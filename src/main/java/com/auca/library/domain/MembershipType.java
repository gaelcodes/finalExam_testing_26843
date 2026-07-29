package com.auca.library.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;
import java.util.UUID;
import java.math.BigDecimal;

@Data
@Entity
public class MembershipType {
    @Id
    private UUID membershipTypeId = UUID.randomUUID();
    private String membershipName; // Gold, Silver, Striver
    private int maxBooks;
    private BigDecimal price;
}
