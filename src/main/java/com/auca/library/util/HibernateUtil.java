package com.auca.library.util;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class HibernateUtil {

    private static SessionFactory sessionFactory;

    public static SessionFactory buildSessionFactory(String propertiesFile) {
        if (sessionFactory == null || sessionFactory.isClosed()) {
            try (InputStream input = HibernateUtil.class.getClassLoader().getResourceAsStream(propertiesFile)) {
                Properties props = new Properties();
                props.load(input);
                Configuration config = new Configuration();
                config.setProperties(props);
                sessionFactory = config.buildSessionFactory();
            } catch (IOException e) {
                throw new RuntimeException("Failed to load Hibernate properties: " + propertiesFile, e);
            }
        }
        return sessionFactory;
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static void shutdown() {
        if (sessionFactory != null) sessionFactory.close();
    }
}
