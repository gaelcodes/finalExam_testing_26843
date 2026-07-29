package com.auca.library.domain;

import com.auca.library.domain.enums.MembershipStatus;
import jakarta.persistence.*;
import lombok.Data;
import java.util.UUID;
import java.time.LocalDateTime;

@Data
@Entity
public class Membership {
    @Id
    private UUID membershipId = UUID.randomUUID();
    private String membershipCode;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "membershipTypeId")
    private MembershipType membershipType;
    
    @Enumerated(EnumType.STRING)
    private MembershipStatus membershipStatus;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "readerId")
    private User reader;
    
    private LocalDateTime registrationDate;
    private LocalDateTime expiringTime;
}
