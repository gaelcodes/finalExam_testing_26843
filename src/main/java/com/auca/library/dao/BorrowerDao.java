package com.auca.library.dao;

import com.auca.library.domain.Borrower;
import com.auca.library.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import java.util.UUID;
import org.hibernate.query.Query;

public class BorrowerDao {
    public void save(Borrower borrower) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.merge(borrower);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw e;
        }
    }
    
    public long countActiveBorrowsByReaderId(UUID readerId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Long> query = session.createQuery(
                "SELECT COUNT(b) FROM Borrower b WHERE b.reader.personId = :readerId AND b.returnDate IS NULL", 
                Long.class
            );
            query.setParameter("readerId", readerId);
            return query.uniqueResult();
        }
    }

    public Borrower findById(UUID id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Borrower.class, id);
        }
    }
}
