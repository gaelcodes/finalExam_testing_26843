package com.auca.library.domain;

import com.auca.library.domain.enums.BookStatus;
import jakarta.persistence.*;
import java.util.UUID;

@Entity
public class Book {
    @Id
    private UUID bookId = UUID.randomUUID();
    private String title;
    private String isbnCode;
    private int edition;
    private int publicationYear;
    private String publisherName;
    
    @Enumerated(EnumType.STRING)
    private BookStatus bookStatus;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shelfId")
    private Shelf shelf;

    public UUID getBookId() { return bookId; }
    public void setBookId(UUID bookId) { this.bookId = bookId; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getIsbnCode() { return isbnCode; }
    public void setIsbnCode(String isbnCode) { this.isbnCode = isbnCode; }
    public int getEdition() { return edition; }
    public void setEdition(int edition) { this.edition = edition; }
    public int getPublicationYear() { return publicationYear; }
    public void setPublicationYear(int publicationYear) { this.publicationYear = publicationYear; }
    public String getPublisherName() { return publisherName; }
    public void setPublisherName(String publisherName) { this.publisherName = publisherName; }
    public BookStatus getBookStatus() { return bookStatus; }
    public void setBookStatus(BookStatus bookStatus) { this.bookStatus = bookStatus; }
    public Shelf getShelf() { return shelf; }
    public void setShelf(Shelf shelf) { this.shelf = shelf; }
}
