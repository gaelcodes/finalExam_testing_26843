package com.auca.library.service;

import com.auca.library.dao.BookDao;
import com.auca.library.dao.BorrowerDao;
import com.auca.library.dao.MembershipDao;
import com.auca.library.dao.UserDao;
import com.auca.library.domain.Book;
import com.auca.library.domain.Borrower;
import com.auca.library.domain.User;
import com.auca.library.domain.enums.BookStatus;
import java.time.LocalDate;
import java.util.UUID;

public class BorrowingService {
    private final BookDao bookDao;
    private final BorrowerDao borrowerDao;
    private final UserDao userDao;
    private final MembershipDao membershipDao;

    public BorrowingService(BookDao bookDao, BorrowerDao borrowerDao, UserDao userDao, MembershipDao membershipDao) {
        this.bookDao = bookDao;
        this.borrowerDao = borrowerDao;
        this.userDao = userDao;
        this.membershipDao = membershipDao;
    }

    public void borrowBook(UUID readerId, UUID bookId) {
        validateBorrowLimit(readerId);
        
        Book book = bookDao.findById(bookId);
        if (book == null || book.getBookStatus() != BookStatus.AVAILABLE) {
            throw new IllegalArgumentException("Book is not available");
        }
        
        User reader = userDao.findById(readerId);
        if (reader == null) {
            throw new IllegalArgumentException("Reader not found");
        }
        
        Borrower borrower = new Borrower();
        borrower.setBook(book);
        borrower.setReader(reader);
        borrower.setPickupDate(LocalDate.now());
        borrower.setDueDate(LocalDate.now().plusDays(14)); // default loan period 14 days
        borrower.setLateChargeFees(java.math.BigDecimal.ZERO);
        
        book.setBookStatus(BookStatus.BORROWED);
        
        borrowerDao.save(borrower);
        bookDao.save(book);
    }

    public void validateBorrowLimit(UUID readerId) {
        com.auca.library.domain.Membership membership = membershipDao.findActiveByUserId(readerId);
        if (membership == null || membership.getMembershipStatus() != com.auca.library.domain.enums.MembershipStatus.APPROVED) {
            throw new IllegalArgumentException("User does not have an approved membership");
        }
        
        long activeBorrows = borrowerDao.countActiveBorrowsByReaderId(readerId);
        String typeName = membership.getMembershipType().getMembershipName();
        
        int limit = 0;
        if ("Gold".equalsIgnoreCase(typeName)) limit = 5;
        else if ("Silver".equalsIgnoreCase(typeName)) limit = 3;
        else if ("Striver".equalsIgnoreCase(typeName)) limit = 2;
        
        if (activeBorrows >= limit) {
            throw new IllegalArgumentException("Borrow limit reached for membership type: " + typeName);
        }
    }
}
