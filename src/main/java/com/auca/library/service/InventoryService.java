package com.auca.library.service;

import com.auca.library.dao.BookDao;
import com.auca.library.dao.RoomDao;
import com.auca.library.dao.ShelfDao;
import com.auca.library.domain.Book;
import com.auca.library.domain.Room;
import com.auca.library.domain.Shelf;
import java.util.UUID;

public class InventoryService {
    private final RoomDao roomDao;
    private final ShelfDao shelfDao;
    private final BookDao bookDao;
    
    public static final int ROOM_MAX_SHELVES = 5;
    public static final int SHELF_MAX_BOOKS = 10;

    public InventoryService(RoomDao roomDao, ShelfDao shelfDao, BookDao bookDao) {
        this.roomDao = roomDao;
        this.shelfDao = shelfDao;
        this.bookDao = bookDao;
    }

    public void saveShelf(Shelf shelf) {
        if (shelf.getRoom() == null || shelf.getRoom().getRoomId() == null) {
            throw new IllegalArgumentException("Shelf must be assigned to a room");
        }
        
        long currentShelves = shelfDao.countShelvesByRoomId(shelf.getRoom().getRoomId());
        if (currentShelves >= ROOM_MAX_SHELVES) {
            throw new IllegalArgumentException("Room is full, cannot add more shelves");
        }
        
        shelfDao.save(shelf);
    }

    public void assignBookToShelf(UUID bookId, UUID shelfId) {
        Shelf shelf = shelfDao.findById(shelfId);
        if (shelf == null) {
            throw new IllegalArgumentException("Shelf not found");
        }
        
        Book book = bookDao.findById(bookId);
        if (book == null) {
            throw new IllegalArgumentException("Book not found");
        }
        
        int booksOnShelf = shelf.getInitialStock() - shelf.getBorrowedNumber();
        
        if (shelf.getInitialStock() >= SHELF_MAX_BOOKS) {
            throw new IllegalArgumentException("Shelf is full, cannot add more books");
        }
        
        shelf.setInitialStock(shelf.getInitialStock() + 1);
        shelf.setAvailableStock(shelf.getAvailableStock() + 1);
        book.setShelf(shelf);
        
        shelfDao.save(shelf);
        bookDao.save(book);
    }
}
