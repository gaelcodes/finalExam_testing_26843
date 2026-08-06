package com.auca.library.dao;

import com.auca.library.domain.Room;
import com.auca.library.util.HibernateUtil;
import org.hibernate.Session;
import java.util.UUID;

public class RoomDao {
    public Room findById(UUID id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Room.class, id);
        }
    }
}
