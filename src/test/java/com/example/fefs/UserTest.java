package com.example.fefs;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class UserTest {

    private User user;
    private static final int USER_ID = 999;
    private static final String NAME = "Test User";
    private static final String EMAIL = "test.user@example.com";
    private static final String PASSWORD = "password123";
    private static final String ROLE = "Student";

    @Before
    public void setUp() {
        user = new User(USER_ID, NAME, EMAIL, PASSWORD, ROLE);
    }

    @Test
    public void testUserConstructor() {
        assertEquals("User ID should match", USER_ID, user.getUserId());
        assertEquals("Name should match", NAME, user.getName());
        assertEquals("Email should match", EMAIL, user.getEmail());
        assertEquals("Password should match", PASSWORD, user.getPassword());
        assertEquals("Role should match", ROLE, user.getRole());
    }

    @Test
    public void testGetters() {
        assertEquals(USER_ID, user.getUserId());
        assertEquals(NAME, user.getName());
        assertEquals(EMAIL, user.getEmail());
        assertEquals(PASSWORD, user.getPassword());
        assertEquals(ROLE, user.getRole());
    }
}