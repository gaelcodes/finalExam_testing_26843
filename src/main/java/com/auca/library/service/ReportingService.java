package com.auca.library.service;

import com.auca.library.dao.BookDao;
import com.auca.library.dao.MembershipDao;
import java.util.Map;
import java.util.UUID;

public class ReportingService {
    private final BookDao bookDao;
    private final MembershipDao membershipDao;

    public ReportingService(BookDao bookDao, MembershipDao membershipDao) {
        this.bookDao = bookDao;
        this.membershipDao = membershipDao;
    }

    public Map<UUID, Long> countBooksPerLocation() {
        return bookDao.countBooksPerLocation();
    }

    public Map<UUID, Long> countMembersPerLocation() {
        return membershipDao.countMembersPerLocation();
    }
}
