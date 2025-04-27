package com.example.fefs;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import static org.junit.Assert.*;

/**
 * JUnit test cases for the Faculty Evaluation Feedback System (FEFS)
 */
public class FEFS_Test {

    private static final String TEST_DB_URL = "jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1";
    private static final String TEST_DB_USER = "sa";
    private static final String TEST_DB_PASSWORD = "";

    private Connection testConnection;

    @Before
    public void setUp() throws SQLException {
        // Create test connection
        testConnection = DriverManager.getConnection(TEST_DB_URL, TEST_DB_USER, TEST_DB_PASSWORD);

        // Create tables
        createTables();

        // Insert test data
        insertTestData();

        // Override database connection in Database class for testing
        Database.setTestMode(TEST_DB_URL, TEST_DB_USER, TEST_DB_PASSWORD);
    }


    @After
    public void tearDown() throws SQLException {
        // Drop tables
        dropTables();

        // Close connection
        if (testConnection != null && !testConnection.isClosed()) {
            testConnection.close();
        }

        // Disable test mode in Database class
        Database.setTestMode(null, null, null);
    }


    private void createTables() throws SQLException {
        String[] createTableStatements = {
                // Users table
                "CREATE TABLE Users (" +
                        "user_id INT AUTO_INCREMENT PRIMARY KEY, " +
                        "name VARCHAR(100) NOT NULL, " +
                        "email VARCHAR(100) UNIQUE NOT NULL, " +
                        "password_ VARCHAR(255) NOT NULL, " +
                        "role VARCHAR(20) NOT NULL" +
                        ")",

                // Faculty table
                "CREATE TABLE Faculty (" +
                        "user_id INT PRIMARY KEY, " +
                        "department VARCHAR(100) NOT NULL, " +
                        "FOREIGN KEY (user_id) REFERENCES Users(user_id) ON DELETE CASCADE" +
                        ")",

                // Students table
                "CREATE TABLE Students (" +
                        "user_id INT PRIMARY KEY, " +
                        "batch VARCHAR(50) NOT NULL, " +
                        "FOREIGN KEY (user_id) REFERENCES Users(user_id) ON DELETE CASCADE" +
                        ")",

                // Courses table
                "CREATE TABLE Courses (" +
                        "course_id INT AUTO_INCREMENT PRIMARY KEY, " +
                        "course_name VARCHAR(100) NOT NULL, " +
                        "course_code VARCHAR(20) UNIQUE NOT NULL" +
                        ")",

                // Teaching table (maps faculty to courses)
                "CREATE TABLE Teaching (" +
                        "teaching_id INT AUTO_INCREMENT PRIMARY KEY, " +
                        "faculty_id INT NOT NULL, " +
                        "course_id INT NOT NULL, " +
                        "FOREIGN KEY (faculty_id) REFERENCES Users(user_id), " +
                        "FOREIGN KEY (course_id) REFERENCES Courses(course_id), " +
                        "UNIQUE (faculty_id, course_id)" +
                        ")",

                // Feedback table
                "CREATE TABLE Feedback (" +
                        "feedback_id INT AUTO_INCREMENT PRIMARY KEY, " +
                        "faculty_id INT NOT NULL, " +
                        "course_id INT NOT NULL, " +
                        "rating INT NOT NULL CHECK (rating BETWEEN 1 AND 5), " +
                        "comments TEXT, " +
                        "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                        "FOREIGN KEY (faculty_id) REFERENCES Users(user_id), " +
                        "FOREIGN KEY (course_id) REFERENCES Courses(course_id)" +
                        ")",

                // Notifications table
                "CREATE TABLE Notifications (" +
                        "notification_id INT AUTO_INCREMENT PRIMARY KEY, " +
                        "message TEXT NOT NULL, " +
                        "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                        ")"
        };

        for (String sql : createTableStatements) {
            try (PreparedStatement stmt = testConnection.prepareStatement(sql)) {
                stmt.executeUpdate();
            }
        }
    }

    /**
     * Insert test data
     */
    private void insertTestData() throws SQLException {
        // Insert test users (Admin, Faculty, Student)
        String[] insertUserStatements = {
                // Admin
                "INSERT INTO Users (name, email, password_, role) VALUES ('Admin User', 'admin@test.com', 'adminpass', 'Administrator')",

                // Faculty
                "INSERT INTO Users (name, email, password_, role) VALUES ('Faculty User', 'faculty@test.com', 'facultypass', 'Faculty')",
                "INSERT INTO Users (name, email, password_, role) VALUES ('Another Faculty', 'faculty2@test.com', 'faculty2pass', 'Faculty')",

                // Students
                "INSERT INTO Users (name, email, password_, role) VALUES ('Student User', 'student@test.com', 'studentpass', 'Student')",
                "INSERT INTO Users (name, email, password_, role) VALUES ('Another Student', 'student2@test.com', 'student2pass', 'Student')"
        };

        for (String sql : insertUserStatements) {
            try (PreparedStatement stmt = testConnection.prepareStatement(sql)) {
                stmt.executeUpdate();
            }
        }

        // Insert faculty details
        String[] insertFacultyStatements = {
                "INSERT INTO Faculty (user_id, department) VALUES (2, 'Computer Science')",
                "INSERT INTO Faculty (user_id, department) VALUES (3, 'Mathematics')"
        };

        for (String sql : insertFacultyStatements) {
            try (PreparedStatement stmt = testConnection.prepareStatement(sql)) {
                stmt.executeUpdate();
            }
        }

        // Insert student details
        String[] insertStudentStatements = {
                "INSERT INTO Students (user_id, batch) VALUES (4, '2023')",
                "INSERT INTO Students (user_id, batch) VALUES (5, '2024')"
        };

        for (String sql : insertStudentStatements) {
            try (PreparedStatement stmt = testConnection.prepareStatement(sql)) {
                stmt.executeUpdate();
            }
        }

        // Insert courses
        String[] insertCourseStatements = {
                "INSERT INTO Courses (course_name, course_code) VALUES ('Introduction to Programming', 'CS101')",
                "INSERT INTO Courses (course_name, course_code) VALUES ('Data Structures', 'CS201')",
                "INSERT INTO Courses (course_name, course_code) VALUES ('Linear Algebra', 'MATH101')"
        };

        for (String sql : insertCourseStatements) {
            try (PreparedStatement stmt = testConnection.prepareStatement(sql)) {
                stmt.executeUpdate();
            }
        }

        // Assign courses to faculty
        String[] insertTeachingStatements = {
                "INSERT INTO Teaching (faculty_id, course_id) VALUES (2, 1)",  // Faculty 1 teaches CS101
                "INSERT INTO Teaching (faculty_id, course_id) VALUES (2, 2)",  // Faculty 1 teaches CS201
                "INSERT INTO Teaching (faculty_id, course_id) VALUES (3, 3)"   // Faculty 2 teaches MATH101
        };

        for (String sql : insertTeachingStatements) {
            try (PreparedStatement stmt = testConnection.prepareStatement(sql)) {
                stmt.executeUpdate();
            }
        }

        // Insert some feedback
        String[] insertFeedbackStatements = {
                "INSERT INTO Feedback (faculty_id, course_id, rating, comments) VALUES (2, 1, 4, 'Great teaching style!')",
                "INSERT INTO Feedback (faculty_id, course_id, rating, comments) VALUES (2, 2, 3, 'Course content could be improved')",
                "INSERT INTO Feedback (faculty_id, course_id, rating, comments) VALUES (3, 3, 5, 'Excellent explanations')"
        };

        for (String sql : insertFeedbackStatements) {
            try (PreparedStatement stmt = testConnection.prepareStatement(sql)) {
                stmt.executeUpdate();
            }
        }

        // Insert notifications
        String[] insertNotificationStatements = {
                "INSERT INTO Notifications (message) VALUES ('Welcome to the new semester!')",
                "INSERT INTO Notifications (message) VALUES ('Feedback submission deadline extended to next week')"
        };

        for (String sql : insertNotificationStatements) {
            try (PreparedStatement stmt = testConnection.prepareStatement(sql)) {
                stmt.executeUpdate();
            }
        }
    }

    /**
     * Drop tables after testing
     */
    private void dropTables() throws SQLException {
        String[] dropTableStatements = {
                "DROP TABLE IF EXISTS Feedback",
                "DROP TABLE IF EXISTS Teaching",
                "DROP TABLE IF EXISTS Notifications",
                "DROP TABLE IF EXISTS Students",
                "DROP TABLE IF EXISTS Faculty",
                "DROP TABLE IF EXISTS Courses",
                "DROP TABLE IF EXISTS Users"
        };

        for (String sql : dropTableStatements) {
            try (PreparedStatement stmt = testConnection.prepareStatement(sql)) {
                stmt.executeUpdate();
            }
        }
    }

    /**
     * Test user login functionality
     */
    @Test
    public void testLogin() {
        FEFS fefs = FEFS.getInstance();

        // Valid credentials
        assertTrue("Admin login failed", fefs.login("admin@test.com", "adminpass", "Administrator"));
        assertTrue("Faculty login failed", fefs.login("faculty@test.com", "facultypass", "Faculty"));
        assertTrue("Student login failed", fefs.login("student@test.com", "studentpass", "Student"));

        // Invalid credentials
        assertFalse("Invalid email should fail", fefs.login("nonexistent@test.com", "password", "Student"));
        assertFalse("Invalid password should fail", fefs.login("student@test.com", "wrongpass", "Student"));
        assertFalse("Invalid role should fail", fefs.login("student@test.com", "studentpass", "Faculty"));
    }

    /**
     * Test retrieving courses
     */
    @Test
    public void testReturnCourses() {
        FEFS fefs = FEFS.getInstance();
        List<String[]> courses = fefs.returnCourses();

        // Check that all courses are returned
        assertEquals("Should return 3 courses", 3, courses.size());

        // Check content of first course
        assertEquals("First course name should be 'Introduction to Programming'",
                "Introduction to Programming", courses.get(0)[0]);
    }

    /**
     * Test getting instructors by course
     */
    @Test
    public void testGetInstructors() {
        FEFS fefs = FEFS.getInstance();
        List<String[]> instructors = fefs.getInstructors("Introduction to Programming");

        // Check that the correct instructor is returned
        assertEquals("Should return 1 instructor", 1, instructors.size());
        assertEquals("Instructor should be 'Faculty User'", "Faculty User", instructors.get(0)[0]);
    }

    /**
     * Test getting instructor ID by credentials
     */
    @Test
    public void testGetInstructorID() {
        FEFS fefs = FEFS.getInstance();
        int id = fefs.getInstructorID("faculty@test.com", "facultypass", "Faculty");

        // Check that the correct ID is returned
        assertEquals("Faculty ID should be 2", 2, id);

        // Test with invalid credentials
        id = fefs.getInstructorID("invalid@test.com", "wrongpass", "Faculty");
        assertEquals("Invalid credentials should return -1", -1, id);
    }

    /**
     * Test submitting feedback
     */
    @Test
    public void testSubmitFeedback() {
        FEFS.submitFeedback(2, 1, 5, "Excellent course!");

        // Verify feedback was added by retrieving it
        List<String[]> feedback = FEFS.getFeedback(2);

        // Initial data had 2 feedback entries for faculty ID 2, now should have 3
        assertEquals("Should have 3 feedback entries", 3, feedback.size());

        // Find the newest feedback (should be the one we just added)
        boolean foundNewFeedback = false;
        for (String[] fb : feedback) {
            if (fb[2].equals("5") && fb[3].equals("Excellent course!")) {
                foundNewFeedback = true;
                break;
            }
        }

        assertTrue("New feedback should be found", foundNewFeedback);
    }

    /**
     * Test retrieving feedback for a faculty member
     */
    @Test
    public void testGetFeedback() {
        List<String[]> feedback = FEFS.getFeedback(2);  // Faculty ID 2

        // Check that the correct number of feedback entries is returned
        assertEquals("Faculty ID 2 should have 2 feedback entries", 2, feedback.size());

        // Check feedback for faculty member who has no feedback
        feedback = FEFS.getFeedback(999);  // Non-existent faculty ID
        assertEquals("Non-existent faculty should have 0 feedback entries", 0, feedback.size());
    }

    /**
     * Test adding a faculty member
     */
    @Test
    public void testAddFaculty() {
        FEFS fefs = FEFS.getInstance();
        boolean result = fefs.addFaculty("New Faculty", "newfaculty@test.com", "newpass", "Physics");

        // Verify faculty was added
        assertTrue("Adding faculty should succeed", result);
        assertTrue("Should be able to login with new faculty credentials",
                fefs.login("newfaculty@test.com", "newpass", "Faculty"));
    }


    /**
     * Test adding a student
     */
    @Test
    public void testAddStudent() {
        FEFS fefs = FEFS.getInstance();
        boolean result = fefs.addStudent("New Student", "newstudent@test.com", "newpass", "2025");

        // Verify student was added
        assertTrue("Adding student should succeed", result);
        assertTrue("Should be able to login with new student credentials",
                fefs.login("newstudent@test.com", "newpass", "Student"));
    }

    /**
     * Test removing a student
     */
    @Test
    public void testRemoveStudent() {
        FEFS fefs = FEFS.getInstance();
        boolean result = fefs.remStudent("Student User", "student@test.com", "studentpass", "2023");

        // Verify student was removed
        assertTrue("Removing student should succeed", result);
        assertFalse("Should not be able to login with removed student credentials",
                fefs.login("student@test.com", "studentpass", "Student"));
    }

    /**
     * Test sending notifications
     */
    @Test
    public void testSendNotification() {
        FEFS fefs = FEFS.getInstance();
        boolean result = fefs.sendNotif("Important test notification");

        // Verify notification was sent
        assertTrue("Sending notification should succeed", result);

        // Check that notification appears in the list
        List<String[]> notifications = fefs.getNotifs();
        assertEquals("Should have 3 notifications (2 initial + 1 new)", 3, notifications.size());

        // Find the new notification
        boolean foundNewNotification = false;
        for (String[] notif : notifications) {
            if (notif[1].equals("Important test notification")) {
                foundNewNotification = true;
                break;
            }
        }

        assertTrue("New notification should be found", foundNewNotification);
    }

    /**
     * Test retrieving notifications
     */
    @Test
    public void testGetNotifications() {
        FEFS fefs = FEFS.getInstance();
        List<String[]> notifications = fefs.getNotifs();

        // Check that all notifications are returned
        assertEquals("Should return 2 notifications", 2, notifications.size());

        // Check content of first notification
        assertEquals("First notification message should match",
                "Welcome to the new semester!", notifications.get(0)[1]);
    }
}

/**
 * Mock implementation of Database class test mode functionality
 * This class should be added to the Database.java file for testing
 */
/*
// Add these fields and methods to the Database class

private static String testDbUrl = null;
private static String testDbUser = null;
private static String testDbPassword = null;

public static void setTestMode(String url, String user, String password) {
    testDbUrl = url;
    testDbUser = user;
    testDbPassword = password;
}

// Modify connection-creating methods to use test connection when in test mode
private static Connection getConnection() throws SQLException {
    if (testDbUrl != null && testDbUser != null && testDbPassword != null) {
        return DriverManager.getConnection(testDbUrl, testDbUser, testDbPassword);
    } else {
        return DriverManager.getConnection(URL, USERNAME, PASSWORD);
    }
}

// Then replace all DriverManager.getConnection(URL, USERNAME, PASSWORD) calls with getConnection()
*/