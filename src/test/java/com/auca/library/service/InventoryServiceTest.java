package com.auca.library.service;

import com.auca.library.dao.BookDao;
import com.auca.library.dao.RoomDao;
import com.auca.library.dao.ShelfDao;
import com.auca.library.domain.Book;
import com.auca.library.domain.Room;
import com.auca.library.domain.Shelf;
import org.junit.Before;
import org.junit.Test;

import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class InventoryServiceTest {
    private RoomDao roomDao;
    private ShelfDao shelfDao;
    private BookDao bookDao;
    private InventoryService inventoryService;

    @Before
    public void setUp() {
        roomDao = mock(RoomDao.class);
        shelfDao = mock(ShelfDao.class);
        bookDao = mock(BookDao.class);
        inventoryService = new InventoryService(roomDao, shelfDao, bookDao);
    }

    @Test
    public void saveShelf_whenRoomHasSpace_succeeds() {
        UUID roomId = UUID.randomUUID();
        Room room = new Room();
        room.setRoomId(roomId);
        
        Shelf shelf = new Shelf();
        shelf.setRoom(room);
        
        when(shelfDao.countShelvesByRoomId(roomId)).thenReturn(0L);
        
        inventoryService.saveShelf(shelf);
        
        verify(shelfDao).save(shelf);
    }

    @Test(expected = IllegalArgumentException.class)
    public void saveShelf_whenRoomIsFull_throwsException() {
        UUID roomId = UUID.randomUUID();
        Room room = new Room();
        room.setRoomId(roomId);
        
        Shelf shelf = new Shelf();
        shelf.setRoom(room);
        
        when(shelfDao.countShelvesByRoomId(roomId)).thenReturn(5L); // Full
        
        inventoryService.saveShelf(shelf);
    }

    @Test(expected = IllegalArgumentException.class)
    public void assignBook_whenShelfIsFull_throwsException() {
        UUID shelfId = UUID.randomUUID();
        UUID bookId = UUID.randomUUID();
        
        Shelf shelf = new Shelf();
        shelf.setShelfId(shelfId);
        shelf.setInitialStock(10); // Full
        
        Book book = new Book();
        book.setBookId(bookId);
        
        when(shelfDao.findById(shelfId)).thenReturn(shelf);
        when(bookDao.findById(bookId)).thenReturn(book);
        
        inventoryService.assignBookToShelf(bookId, shelfId);
    }
}
