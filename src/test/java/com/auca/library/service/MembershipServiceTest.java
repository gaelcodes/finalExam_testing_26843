package com.auca.library.service;

import com.auca.library.dao.MembershipDao;
import com.auca.library.dao.MembershipTypeDao;
import com.auca.library.dao.UserDao;
import com.auca.library.domain.Membership;
import com.auca.library.domain.MembershipType;
import com.auca.library.domain.User;
import com.auca.library.domain.enums.MembershipStatus;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class MembershipServiceTest {
    private MembershipDao membershipDao;
    private MembershipTypeDao membershipTypeDao;
    private UserDao userDao;
    private MembershipService membershipService;

    @Before
    public void setUp() {
        membershipDao = mock(MembershipDao.class);
        membershipTypeDao = mock(MembershipTypeDao.class);
        userDao = mock(UserDao.class);
        membershipService = new MembershipService(membershipDao, membershipTypeDao, userDao);
    }

    @Test
    public void registerMembership_gold_createsPendingMembershipLinkedToGoldType() {
        UUID userId = UUID.randomUUID();
        UUID typeId = UUID.randomUUID();
        
        User user = new User();
        user.setPersonId(userId);
        
        MembershipType goldType = new MembershipType();
        goldType.setMembershipTypeId(typeId);
        goldType.setMembershipName("Gold");
        
        when(membershipDao.findActiveByUserId(userId)).thenReturn(null);
        when(userDao.findById(userId)).thenReturn(user);
        when(membershipTypeDao.findById(typeId)).thenReturn(goldType);
        
        membershipService.registerMembership(userId, typeId);
        
        ArgumentCaptor<Membership> captor = ArgumentCaptor.forClass(Membership.class);
        verify(membershipDao).save(captor.capture());
        
        Membership saved = captor.getValue();
        assertEquals(MembershipStatus.PENDING, saved.getMembershipStatus());
        assertEquals(goldType, saved.getMembershipType());
        assertEquals(user, saved.getReader());
    }

    @Test(expected = IllegalArgumentException.class)
    public void registerMembership_userAlreadyHasActiveMembership_throwsException() {
        UUID userId = UUID.randomUUID();
        UUID typeId = UUID.randomUUID();
        
        when(membershipDao.findActiveByUserId(userId)).thenReturn(new Membership());
        
        membershipService.registerMembership(userId, typeId);
    }

    @Test
    public void approveMembership_pendingMembership_setsStatusApproved() {
        UUID membershipId = UUID.randomUUID();
        Membership membership = new Membership();
        membership.setMembershipId(membershipId);
        membership.setMembershipStatus(MembershipStatus.PENDING);
        
        when(membershipDao.findById(membershipId)).thenReturn(membership);
        
        membershipService.approveMembership(membershipId);
        
        verify(membershipDao).save(membership);
        assertEquals(MembershipStatus.APPROVED, membership.getMembershipStatus());
    }

    @Test(expected = IllegalArgumentException.class)
    public void approveMembership_alreadyApprovedMembership_throwsException() {
        UUID membershipId = UUID.randomUUID();
        Membership membership = new Membership();
        membership.setMembershipId(membershipId);
        membership.setMembershipStatus(MembershipStatus.APPROVED);
        
        when(membershipDao.findById(membershipId)).thenReturn(membership);
        
        membershipService.approveMembership(membershipId);
    }
}
