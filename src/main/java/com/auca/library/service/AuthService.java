package com.auca.library.service;

import com.auca.library.dao.UserDao;
import com.auca.library.domain.User;

public class AuthService {
    private final UserDao userDao;
    private final AccountService accountService;

    public AuthService(UserDao userDao) {
        this.userDao = userDao;
        this.accountService = new AccountService(userDao);
    }

    public boolean authenticate(String username, String rawPassword) {
        if (username == null || username.trim().isEmpty() || rawPassword == null) {
            return false;
        }
        User user = userDao.findByUsername(username);
        if (user == null) {
            return false;
        }
        String hashedInput = accountService.hashPassword(rawPassword);
        return hashedInput.equals(user.getPassword());
    }
}
