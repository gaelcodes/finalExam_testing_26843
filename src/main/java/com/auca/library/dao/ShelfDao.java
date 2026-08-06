package com.auca.library.dao;

import com.auca.library.domain.Shelf;
import com.auca.library.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import java.util.UUID;

public class ShelfDao {
    public void save(Shelf shelf) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.merge(shelf);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw e;
        }
    }

    public Shelf findById(UUID id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Shelf.class, id);
        }
    }
    
    public long countShelvesByRoomId(UUID roomId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Long> query = session.createQuery("SELECT COUNT(s) FROM Shelf s WHERE s.room.roomId = :roomId", Long.class);
            query.setParameter("roomId", roomId);
            return query.uniqueResult();
        }
    }
}
