package com.auca.library.service;

import com.auca.library.dao.BookDao;
import com.auca.library.dao.BorrowerDao;
import com.auca.library.dao.MembershipDao;
import com.auca.library.dao.UserDao;
import com.auca.library.domain.Book;
import com.auca.library.domain.Borrower;
import com.auca.library.domain.User;
import com.auca.library.domain.enums.BookStatus;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class BorrowingServiceTest {
    private BookDao bookDao;
    private BorrowerDao borrowerDao;
    private UserDao userDao;
    private MembershipDao membershipDao;
    private BorrowingService borrowingService;

    @Before
    public void setUp() {
        bookDao = mock(BookDao.class);
        borrowerDao = mock(BorrowerDao.class);
        userDao = mock(UserDao.class);
        membershipDao = mock(MembershipDao.class);
        borrowingService = new BorrowingService(bookDao, borrowerDao, userDao, membershipDao);
    }

    @Test
    public void borrowBook_availableBook_createsBorrowerRecordWithZeroFine() {
        UUID readerId = UUID.randomUUID();
        UUID bookId = UUID.randomUUID();
        
        User reader = new User();
        reader.setPersonId(readerId);
        
        Book book = new Book();
        book.setBookId(bookId);
        book.setBookStatus(BookStatus.AVAILABLE);
        
        com.auca.library.domain.MembershipType goldType = new com.auca.library.domain.MembershipType();
        goldType.setMembershipName("Gold");
        
        com.auca.library.domain.Membership membership = new com.auca.library.domain.Membership();
        membership.setMembershipStatus(com.auca.library.domain.enums.MembershipStatus.APPROVED);
        membership.setMembershipType(goldType);
        
        when(membershipDao.findActiveByUserId(readerId)).thenReturn(membership);
        when(borrowerDao.countActiveBorrowsByReaderId(readerId)).thenReturn(0L);
        when(userDao.findById(readerId)).thenReturn(reader);
        when(bookDao.findById(bookId)).thenReturn(book);
        
        borrowingService.borrowBook(readerId, bookId);
        
        ArgumentCaptor<Borrower> captor = ArgumentCaptor.forClass(Borrower.class);
        verify(borrowerDao).save(captor.capture());
        
        Borrower saved = captor.getValue();
        assertEquals(java.math.BigDecimal.ZERO, saved.getLateChargeFees());
        assertEquals(reader, saved.getReader());
        assertEquals(book, saved.getBook());
    }

    @Test
    public void borrowBook_setsBookStatusToBorrowed() {
        UUID readerId = UUID.randomUUID();
        UUID bookId = UUID.randomUUID();
        
        User reader = new User();
        reader.setPersonId(readerId);
        
        Book book = new Book();
        book.setBookId(bookId);
        book.setBookStatus(BookStatus.AVAILABLE);
        
        com.auca.library.domain.MembershipType goldType = new com.auca.library.domain.MembershipType();
        goldType.setMembershipName("Gold");
        
        com.auca.library.domain.Membership membership = new com.auca.library.domain.Membership();
        membership.setMembershipStatus(com.auca.library.domain.enums.MembershipStatus.APPROVED);
        membership.setMembershipType(goldType);
        
        when(membershipDao.findActiveByUserId(readerId)).thenReturn(membership);
        when(borrowerDao.countActiveBorrowsByReaderId(readerId)).thenReturn(0L);
        when(userDao.findById(readerId)).thenReturn(reader);
        when(bookDao.findById(bookId)).thenReturn(book);
        
        borrowingService.borrowBook(readerId, bookId);
        
        verify(bookDao).save(book);
        assertEquals(BookStatus.BORROWED, book.getBookStatus());
    }

    @Test
    public void borrowBook_dueDateIsPickupDatePlusLoanPeriod() {
        UUID readerId = UUID.randomUUID();
        UUID bookId = UUID.randomUUID();
        
        User reader = new User();
        reader.setPersonId(readerId);
        
        Book book = new Book();
        book.setBookId(bookId);
        book.setBookStatus(BookStatus.AVAILABLE);
        
        com.auca.library.domain.MembershipType goldType = new com.auca.library.domain.MembershipType();
        goldType.setMembershipName("Gold");
        
        com.auca.library.domain.Membership membership = new com.auca.library.domain.Membership();
        membership.setMembershipStatus(com.auca.library.domain.enums.MembershipStatus.APPROVED);
        membership.setMembershipType(goldType);
        
        when(membershipDao.findActiveByUserId(readerId)).thenReturn(membership);
        when(borrowerDao.countActiveBorrowsByReaderId(readerId)).thenReturn(0L);
        when(userDao.findById(readerId)).thenReturn(reader);
        when(bookDao.findById(bookId)).thenReturn(book);
        
        borrowingService.borrowBook(readerId, bookId);
        
        ArgumentCaptor<Borrower> captor = ArgumentCaptor.forClass(Borrower.class);
        verify(borrowerDao).save(captor.capture());
        
        Borrower saved = captor.getValue();
        LocalDate expectedDue = saved.getPickupDate().plusDays(14); // default 14 days
        assertEquals(expectedDue, saved.getDueDate());
    }

    @Test
    public void goldMember_withFourActiveBorrows_canBorrowAFifth() {
        UUID readerId = UUID.randomUUID();
        com.auca.library.domain.MembershipType goldType = new com.auca.library.domain.MembershipType();
        goldType.setMembershipName("Gold");
        
        com.auca.library.domain.Membership membership = new com.auca.library.domain.Membership();
        membership.setMembershipStatus(com.auca.library.domain.enums.MembershipStatus.APPROVED);
        membership.setMembershipType(goldType);
        
        when(membershipDao.findActiveByUserId(readerId)).thenReturn(membership);
        when(borrowerDao.countActiveBorrowsByReaderId(readerId)).thenReturn(4L);
        
        // This should not throw an exception
        borrowingService.validateBorrowLimit(readerId);
    }

    @Test(expected = IllegalArgumentException.class)
    public void goldMember_withFiveActiveBorrows_cannotBorrowASixth() {
        UUID readerId = UUID.randomUUID();
        com.auca.library.domain.MembershipType goldType = new com.auca.library.domain.MembershipType();
        goldType.setMembershipName("Gold");
        
        com.auca.library.domain.Membership membership = new com.auca.library.domain.Membership();
        membership.setMembershipStatus(com.auca.library.domain.enums.MembershipStatus.APPROVED);
        membership.setMembershipType(goldType);
        
        when(membershipDao.findActiveByUserId(readerId)).thenReturn(membership);
        when(borrowerDao.countActiveBorrowsByReaderId(readerId)).thenReturn(5L);
        
        borrowingService.validateBorrowLimit(readerId);
    }

    @Test(expected = IllegalArgumentException.class)
    public void silverMember_withThreeActiveBorrows_isBlocked() {
        UUID readerId = UUID.randomUUID();
        com.auca.library.domain.MembershipType type = new com.auca.library.domain.MembershipType();
        type.setMembershipName("Silver");
        
        com.auca.library.domain.Membership membership = new com.auca.library.domain.Membership();
        membership.setMembershipStatus(com.auca.library.domain.enums.MembershipStatus.APPROVED);
        membership.setMembershipType(type);
        
        when(membershipDao.findActiveByUserId(readerId)).thenReturn(membership);
        when(borrowerDao.countActiveBorrowsByReaderId(readerId)).thenReturn(3L);
        
        borrowingService.validateBorrowLimit(readerId);
    }

    @Test(expected = IllegalArgumentException.class)
    public void striverMember_withTwoActiveBorrows_isBlocked() {
        UUID readerId = UUID.randomUUID();
        com.auca.library.domain.MembershipType type = new com.auca.library.domain.MembershipType();
        type.setMembershipName("Striver");
        
        com.auca.library.domain.Membership membership = new com.auca.library.domain.Membership();
        membership.setMembershipStatus(com.auca.library.domain.enums.MembershipStatus.APPROVED);
        membership.setMembershipType(type);
        
        when(membershipDao.findActiveByUserId(readerId)).thenReturn(membership);
        when(borrowerDao.countActiveBorrowsByReaderId(readerId)).thenReturn(2L);
        
        borrowingService.validateBorrowLimit(readerId);
    }

    @Test(expected = IllegalArgumentException.class)
    public void userWithoutApprovedMembership_isBlocked() {
        UUID readerId = UUID.randomUUID();
        com.auca.library.domain.Membership membership = new com.auca.library.domain.Membership();
        membership.setMembershipStatus(com.auca.library.domain.enums.MembershipStatus.PENDING);
        
        when(membershipDao.findActiveByUserId(readerId)).thenReturn(membership);
        
        borrowingService.validateBorrowLimit(readerId);
    }

}
