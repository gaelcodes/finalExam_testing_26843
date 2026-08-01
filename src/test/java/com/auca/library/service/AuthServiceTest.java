package com.auca.library.service;

import com.auca.library.dao.UserDao;
import com.auca.library.domain.User;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class AuthServiceTest {
    private UserDao userDao;
    private AuthService authService;
    private AccountService accountService;

    @Before
    public void setUp() {
        userDao = mock(UserDao.class);
        authService = new AuthService(userDao);
        accountService = new AccountService(userDao);
    }

    @Test
    public void authenticate_correctCredentials_returnsTrue() {
        User user = new User();
        user.setUsername("bob");
        user.setPassword(accountService.hashPassword("secret123"));
        
        when(userDao.findByUsername("bob")).thenReturn(user);
        
        assertTrue(authService.authenticate("bob", "secret123"));
    }

    @Test
    public void authenticate_wrongPassword_returnsFalse() {
        User user = new User();
        user.setUsername("bob");
        user.setPassword(accountService.hashPassword("secret123"));
        
        when(userDao.findByUsername("bob")).thenReturn(user);
        
        assertFalse(authService.authenticate("bob", "wrongpass"));
    }

    @Test
    public void authenticate_unknownUsername_returnsFalse() {
        when(userDao.findByUsername("unknown")).thenReturn(null);
        
        assertFalse(authService.authenticate("unknown", "secret123"));
    }

    @Test
    public void authenticate_nullOrBlankInput_returnsFalse() {
        assertFalse(authService.authenticate(null, "secret123"));
        assertFalse(authService.authenticate("  ", "secret123"));
        assertFalse(authService.authenticate("bob", null));
    }
}
