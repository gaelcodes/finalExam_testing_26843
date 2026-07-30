package com.auca.library.domain;

import jakarta.persistence.*;
import java.util.UUID;
import java.time.LocalDate;
import java.math.BigDecimal;

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

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public User getReader() { return reader; }
    public void setReader(User reader) { this.reader = reader; }
    public Book getBook() { return book; }
    public void setBook(Book book) { this.book = book; }
    public LocalDate getPickupDate() { return pickupDate; }
    public void setPickupDate(LocalDate pickupDate) { this.pickupDate = pickupDate; }
    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }
    public LocalDate getReturnDate() { return returnDate; }
    public void setReturnDate(LocalDate returnDate) { this.returnDate = returnDate; }
    public BigDecimal getFine() { return fine; }
    public void setFine(BigDecimal fine) { this.fine = fine; }
    public BigDecimal getLateChargeFees() { return lateChargeFees; }
    public void setLateChargeFees(BigDecimal lateChargeFees) { this.lateChargeFees = lateChargeFees; }
}
