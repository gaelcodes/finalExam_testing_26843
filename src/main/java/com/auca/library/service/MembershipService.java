package com.auca.library.service;

import com.auca.library.dao.MembershipDao;
import com.auca.library.dao.MembershipTypeDao;
import com.auca.library.dao.UserDao;
import com.auca.library.domain.Membership;
import com.auca.library.domain.MembershipType;
import com.auca.library.domain.User;
import com.auca.library.domain.enums.MembershipStatus;
import java.time.LocalDateTime;
import java.util.UUID;

public class MembershipService {
    private final MembershipDao membershipDao;
    private final MembershipTypeDao membershipTypeDao;
    private final UserDao userDao;

    public MembershipService(MembershipDao membershipDao, MembershipTypeDao membershipTypeDao, UserDao userDao) {
        this.membershipDao = membershipDao;
        this.membershipTypeDao = membershipTypeDao;
        this.userDao = userDao;
    }

    public void registerMembership(UUID userId, UUID membershipTypeId) {
        Membership existing = membershipDao.findActiveByUserId(userId);
        if (existing != null) {
            throw new IllegalArgumentException("User already has an active or pending membership");
        }

        User user = userDao.findById(userId);
        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }

        MembershipType type = membershipTypeDao.findById(membershipTypeId);
        if (type == null) {
            throw new IllegalArgumentException("Membership type not found");
        }

        Membership membership = new Membership();
        membership.setMembershipCode("MEM-" + System.currentTimeMillis());
        membership.setMembershipType(type);
        membership.setReader(user);
        membership.setMembershipStatus(MembershipStatus.PENDING);
        membership.setRegistrationDate(LocalDateTime.now());
        
        membershipDao.save(membership);
    }

    public void approveMembership(UUID membershipId) {
        Membership membership = membershipDao.findById(membershipId);
        if (membership == null) {
            throw new IllegalArgumentException("Membership not found");
        }
        if (membership.getMembershipStatus() == MembershipStatus.APPROVED) {
            throw new IllegalArgumentException("Membership is already approved");
        }
        
        membership.setMembershipStatus(MembershipStatus.APPROVED);
        membership.setExpiringTime(LocalDateTime.now().plusYears(1));
        
        membershipDao.save(membership);
    }
}
