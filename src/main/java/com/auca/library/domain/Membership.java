package com.auca.library.domain;

import com.auca.library.domain.enums.MembershipStatus;
import jakarta.persistence.*;
import java.util.UUID;
import java.time.LocalDateTime;

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

    public UUID getMembershipId() { return membershipId; }
    public void setMembershipId(UUID membershipId) { this.membershipId = membershipId; }
    public String getMembershipCode() { return membershipCode; }
    public void setMembershipCode(String membershipCode) { this.membershipCode = membershipCode; }
    public MembershipType getMembershipType() { return membershipType; }
    public void setMembershipType(MembershipType membershipType) { this.membershipType = membershipType; }
    public MembershipStatus getMembershipStatus() { return membershipStatus; }
    public void setMembershipStatus(MembershipStatus membershipStatus) { this.membershipStatus = membershipStatus; }
    public User getReader() { return reader; }
    public void setReader(User reader) { this.reader = reader; }
    public LocalDateTime getRegistrationDate() { return registrationDate; }
    public void setRegistrationDate(LocalDateTime registrationDate) { this.registrationDate = registrationDate; }
    public LocalDateTime getExpiringTime() { return expiringTime; }
    public void setExpiringTime(LocalDateTime expiringTime) { this.expiringTime = expiringTime; }
}
