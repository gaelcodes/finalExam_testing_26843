package com.auca.library.dao;

import com.auca.library.domain.MembershipType;
import com.auca.library.util.HibernateUtil;
import org.hibernate.Session;
import java.util.UUID;

public class MembershipTypeDao {
    public MembershipType findById(UUID id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(MembershipType.class, id);
        }
    }
}
