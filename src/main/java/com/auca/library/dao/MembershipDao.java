package com.auca.library.dao;

import com.auca.library.domain.Membership;
import com.auca.library.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import java.util.UUID;

public class MembershipDao {
    public void save(Membership membership) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.merge(membership);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw e;
        }
    }

    public Membership findById(UUID id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Membership.class, id);
        }
    }

    public Membership findActiveByUserId(UUID userId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Membership> query = session.createQuery(
                "FROM Membership m WHERE m.reader.personId = :userId AND m.membershipStatus IN ('PENDING', 'APPROVED')", 
                Membership.class
            );
            query.setParameter("userId", userId);
            query.setMaxResults(1);
            return query.uniqueResult();
        }
    }
}
