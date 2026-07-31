package com.auca.library.service;

import com.auca.library.dao.UserDao;
import com.auca.library.domain.User;
import com.auca.library.domain.enums.Role;

public class AccountService {
    private final UserDao userDao;

    public AccountService(UserDao userDao) {
        this.userDao = userDao;
    }

    public void createAccount(User user) {
        if (user.getRole() == Role.LIBRARIAN) {
            throw new IllegalArgumentException("Librarian accounts cannot be self-registered");
        }
        if (userDao.findByUsername(user.getUsername()) != null) {
            throw new IllegalArgumentException("Username already exists");
        }
        
        // Use a simple hash for demonstration
        String hashedPassword = hashPassword(user.getPassword());
        user.setPassword(hashedPassword);
        
        userDao.save(user);
    }

    public String hashPassword(String password) {
        return "HASHED_" + password; // Dummy hash for simplicity
    }
}
