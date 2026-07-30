package com.auca.library.domain;

import jakarta.persistence.*;
import java.util.UUID;

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

    public UUID getShelfId() { return shelfId; }
    public void setShelfId(UUID shelfId) { this.shelfId = shelfId; }
    public String getBookCategory() { return bookCategory; }
    public void setBookCategory(String bookCategory) { this.bookCategory = bookCategory; }
    public int getInitialStock() { return initialStock; }
    public void setInitialStock(int initialStock) { this.initialStock = initialStock; }
    public int getAvailableStock() { return availableStock; }
    public void setAvailableStock(int availableStock) { this.availableStock = availableStock; }
    public int getBorrowedNumber() { return borrowedNumber; }
    public void setBorrowedNumber(int borrowedNumber) { this.borrowedNumber = borrowedNumber; }
    public Room getRoom() { return room; }
    public void setRoom(Room room) { this.room = room; }
}
