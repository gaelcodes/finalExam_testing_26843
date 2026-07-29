package com.auca.library.domain;

import com.auca.library.domain.enums.BookStatus;
import jakarta.persistence.*;
import lombok.Data;
import java.util.UUID;

@Data
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
}
