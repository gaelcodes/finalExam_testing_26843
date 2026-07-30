package com.auca.library.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.util.UUID;
import java.math.BigDecimal;

@Entity
public class MembershipType {
    @Id
    private UUID membershipTypeId = UUID.randomUUID();
    private String membershipName; // Gold, Silver, Striver
    private int maxBooks;
    private BigDecimal price;

    public UUID getMembershipTypeId() { return membershipTypeId; }
    public void setMembershipTypeId(UUID membershipTypeId) { this.membershipTypeId = membershipTypeId; }
    public String getMembershipName() { return membershipName; }
    public void setMembershipName(String membershipName) { this.membershipName = membershipName; }
    public int getMaxBooks() { return maxBooks; }
    public void setMaxBooks(int maxBooks) { this.maxBooks = maxBooks; }
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
}
