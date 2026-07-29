package com.auca.library;

import com.auca.library.util.HibernateUtil;

public class App {
    public static void main(String[] args) {
        System.out.println("Initializing Hibernate and creating tables...");
        HibernateUtil.buildSessionFactory("application.properties");
        System.out.println("Tables created successfully.");
    }
}
