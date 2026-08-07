package com.auca.library.service;

import com.auca.library.dao.BookDao;
import com.auca.library.dao.MembershipDao;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class ReportingServiceTest {
    private BookDao bookDao;
    private MembershipDao membershipDao;
    private ReportingService reportingService;

    @Before
    public void setUp() {
        bookDao = mock(BookDao.class);
        membershipDao = mock(MembershipDao.class);
        reportingService = new ReportingService(bookDao, membershipDao);
    }

    @Test
    public void countBooksPerLocation_returnsAccurateCount() {
        UUID locId1 = UUID.randomUUID();
        UUID locId2 = UUID.randomUUID();
        
        Map<UUID, Long> mockResult = new HashMap<>();
        mockResult.put(locId1, 150L);
        mockResult.put(locId2, 30L);
        
        when(bookDao.countBooksPerLocation()).thenReturn(mockResult);
        
        Map<UUID, Long> result = reportingService.countBooksPerLocation();
        
        assertEquals(2, result.size());
        assertEquals(Long.valueOf(150L), result.get(locId1));
        assertEquals(Long.valueOf(30L), result.get(locId2));
        verify(bookDao).countBooksPerLocation();
    }

    @Test
    public void countMembersPerLocation_returnsAccurateCount() {
        UUID locId1 = UUID.randomUUID();
        
        Map<UUID, Long> mockResult = new HashMap<>();
        mockResult.put(locId1, 50L);
        
        when(membershipDao.countMembersPerLocation()).thenReturn(mockResult);
        
        Map<UUID, Long> result = reportingService.countMembersPerLocation();
        
        assertEquals(1, result.size());
        assertEquals(Long.valueOf(50L), result.get(locId1));
        verify(membershipDao).countMembersPerLocation();
    }

    @Test
    public void countMembersPerLocation_whenEmpty_returnsZero() {
        Map<UUID, Long> mockResult = new HashMap<>();
        
        when(membershipDao.countMembersPerLocation()).thenReturn(mockResult);
        
        Map<UUID, Long> result = reportingService.countMembersPerLocation();
        
        assertEquals(0, result.size());
        verify(membershipDao).countMembersPerLocation();
    }

    @Test
    public void countBooksPerLocation_whenEmpty_returnsEmptyMap() {
        Map<UUID, Long> mockResult = new HashMap<>();
        when(bookDao.countBooksPerLocation()).thenReturn(mockResult);
        
        Map<UUID, Long> result = reportingService.countBooksPerLocation();
        
        assertEquals(0, result.size());
        verify(bookDao).countBooksPerLocation();
    }

    @Test
    public void countMembersPerLocation_multipleLocations_returnsAccurateCounts() {
        UUID locId1 = UUID.randomUUID();
        UUID locId2 = UUID.randomUUID();
        
        Map<UUID, Long> mockResult = new HashMap<>();
        mockResult.put(locId1, 10L);
        mockResult.put(locId2, 25L);
        
        when(membershipDao.countMembersPerLocation()).thenReturn(mockResult);
        
        Map<UUID, Long> result = reportingService.countMembersPerLocation();
        
        assertEquals(2, result.size());
        assertEquals(Long.valueOf(10L), result.get(locId1));
        assertEquals(Long.valueOf(25L), result.get(locId2));
        verify(membershipDao).countMembersPerLocation();
    }
}
