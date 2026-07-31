package com.auca.library.service;

import com.auca.library.dao.UserDao;
import com.auca.library.domain.User;
import com.auca.library.domain.enums.Role;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class AccountServiceTest {
    private UserDao userDao;
    private AccountService accountService;
    private AuthService authService; // to test createAccount_thenAuthenticate_succeedsWithSameCredentials

    @Before
    public void setUp() {
        userDao = mock(UserDao.class);
        accountService = new AccountService(userDao);
        authService = new AuthService(userDao);
    }

    @Test
    public void createAccount_validNewUser_succeeds() {
        User user = new User();
        user.setUsername("john");
        user.setPassword("secret");
        user.setRole(Role.STUDENT);
        
        when(userDao.findByUsername("john")).thenReturn(null);
        accountService.createAccount(user);
        
        verify(userDao).save(user);
    }

    @Test(expected = IllegalArgumentException.class)
    public void createAccount_duplicateUsername_throwsException() {
        User user = new User();
        user.setUsername("john");
        user.setRole(Role.STUDENT);
        
        when(userDao.findByUsername("john")).thenReturn(new User());
        accountService.createAccount(user);
    }

    @Test(expected = IllegalArgumentException.class)
    public void createAccount_librarianRole_isRejected() {
        User user = new User();
        user.setUsername("librarian1");
        user.setRole(Role.LIBRARIAN);
        accountService.createAccount(user);
    }

    @Test
    public void createAccount_thenAuthenticate_succeedsWithSameCredentials() {
        User user = new User();
        user.setUsername("jane");
        user.setPassword("secret");
        user.setRole(Role.STUDENT);
        
        when(userDao.findByUsername("jane")).thenReturn(null);
        accountService.createAccount(user);
        
        // Mock the findByUsername for authentication
        when(userDao.findByUsername("jane")).thenReturn(user);
        
        boolean authResult = authService.authenticate("jane", "secret");
        assertEquals(true, authResult);
    }
}
