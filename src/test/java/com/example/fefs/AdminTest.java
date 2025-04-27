package com.example.fefs;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class AdminTest {

    private Admin admin;
    private static final int USER_ID = 1001;
    private static final String NAME = "Admin User";
    private static final String EMAIL = "admin@example.com";
    private static final String PASSWORD = "adminpass";
    private static final String ROLE = "Administrator";

    @Before
    public void setUp() {
        admin = new Admin(USER_ID, NAME, EMAIL, PASSWORD, ROLE);
    }

    @Test
    public void testAdminInheritance() {
        assertTrue(admin instanceof User);

        assertEquals("User ID should match", USER_ID, admin.getUserId());
        assertEquals("Name should match", NAME, admin.getName());
        assertEquals("Email should match", EMAIL, admin.getEmail());
        assertEquals("Password should match", PASSWORD, admin.getPassword());
        assertEquals("Role should match", ROLE, admin.getRole());
    }

    @Test
    public void testGenerateAnalytics() {
        admin.generateAnalytics();
    }

    @Test
    public void testManageUserAccounts() {
        admin.manageUserAccounts(99, "delete");
    }

    @Test
    public void testCustomizeEvaluationCriteria() {
        admin.customizeEvaluationCriteria(1, "New criteria");
    }

    @Test
    public void testFlagFacultyPerformance() {
        admin.flagFacultyPerformance(1, "Performance issues");
    }
}
