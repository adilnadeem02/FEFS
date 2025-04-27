package com.example.fefs;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.Assert.*;

public class StudentTest {

    private Student student;
    private static final int USER_ID = 3001;
    private static final String NAME = "Student Test";
    private static final String EMAIL = "student.test@example.com";
    private static final String PASSWORD = "studentpass";
    private static final String ROLE = "Student";

    // Database connection info
    private static final String TEST_URL = "jdbc:mysql://localhost:3306/FacultyEvaluationDB";
    private static final String TEST_USERNAME = "root";
    private static final String TEST_PASSWORD = "raja@168";

    private Connection conn;
    private int testFacultyId;
    private int testCourseId;

    @Before
    public void setUp() throws SQLException {
        student = new Student(USER_ID, NAME, EMAIL, PASSWORD, ROLE);
        conn = DriverManager.getConnection(TEST_URL, TEST_USERNAME, TEST_PASSWORD);

        // Create test user in database for student
        String insertStudent = "INSERT INTO Users (user_id, name, email, password_, role) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement stmt = conn.prepareStatement(insertStudent);
        stmt.setInt(1, USER_ID);
        stmt.setString(2, NAME);
        stmt.setString(3, EMAIL);
        stmt.setString(4, PASSWORD);
        stmt.setString(5, ROLE);
        stmt.executeUpdate();

        // Add entry to Students table
        String insertStudentBatch = "INSERT INTO Students (user_id, batch) VALUES (?, '2024')";
        stmt = conn.prepareStatement(insertStudentBatch);
        stmt.setInt(1, USER_ID);
        stmt.executeUpdate();

        // Create test faculty
        String insertFaculty = "INSERT INTO Users (name, email, password_, role) VALUES ('Test Faculty', 'test.faculty@test.com', 'password', 'Faculty')";
        stmt = conn.prepareStatement(insertFaculty, java.sql.Statement.RETURN_GENERATED_KEYS);
        stmt.executeUpdate();
        ResultSet rs = stmt.getGeneratedKeys();
        if (rs.next()) {
            testFacultyId = rs.getInt(1);
            // Add entry to Faculty table
            String insertFacultyDept = "INSERT INTO Faculty (user_id, department) VALUES (?, 'Computer Science')";
            PreparedStatement deptStmt = conn.prepareStatement(insertFacultyDept);
            deptStmt.setInt(1, testFacultyId);
            deptStmt.executeUpdate();
        }

        // Create test course
        String insertCourse = "INSERT INTO Courses (course_name, course_code) VALUES ('Test Course', 'TC101')";
        stmt = conn.prepareStatement(insertCourse, java.sql.Statement.RETURN_GENERATED_KEYS);
        stmt.executeUpdate();
        rs = stmt.getGeneratedKeys();
        if (rs.next()) {
            testCourseId = rs.getInt(1);
        }
    }

    @After
    public void tearDown() throws SQLException {
        // Clean up test data
        String deleteFeedback = "DELETE FROM Feedback WHERE faculty_id = ?";
        PreparedStatement stmt = conn.prepareStatement(deleteFeedback);
        stmt.setInt(1, testFacultyId);
        stmt.executeUpdate();

        String deleteStudent = "DELETE FROM Students WHERE user_id = ?";
        stmt = conn.prepareStatement(deleteStudent);
        stmt.setInt(1, USER_ID);
        stmt.executeUpdate();

        String deleteUser = "DELETE FROM Users WHERE user_id = ?";
        stmt = conn.prepareStatement(deleteUser);
        stmt.setInt(1, USER_ID);
        stmt.executeUpdate();

        String deleteFaculty = "DELETE FROM Faculty WHERE user_id = ?";
        stmt = conn.prepareStatement(deleteFaculty);
        stmt.setInt(1, testFacultyId);
        stmt.executeUpdate();

        stmt = conn.prepareStatement(deleteUser);
        stmt.setInt(1, testFacultyId);
        stmt.executeUpdate();

        String deleteCourse = "DELETE FROM Courses WHERE course_id = ?";
        stmt = conn.prepareStatement(deleteCourse);
        stmt.setInt(1, testCourseId);
        stmt.executeUpdate();

        if (conn != null) {
            conn.close();
        }
    }

    @Test
    public void testStudentInheritance() {
        // Test that Student inherits from User
        assertTrue(student instanceof User);

        // Test inherited getters
        assertEquals("User ID should match", USER_ID, student.getUserId());
        assertEquals("Name should match", NAME, student.getName());
        assertEquals("Email should match", EMAIL, student.getEmail());
        assertEquals("Password should match", PASSWORD, student.getPassword());
        assertEquals("Role should match", ROLE, student.getRole());
    }

    @Test
    public void testSubmitFeedback() {
        int rating = 4;
        String comments = "Excellent teaching style";

        student.submitFeedback(testFacultyId, testCourseId, rating, comments);

        // Verify feedback was submitted
        try {
            String query = "SELECT * FROM Feedback WHERE faculty_id = ? AND course_id = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, testFacultyId);
            stmt.setInt(2, testCourseId);
            ResultSet rs = stmt.executeQuery();

            assertTrue("Feedback should be found in database", rs.next());
            assertEquals("Rating should match", rating, rs.getInt("rating"));
            assertEquals("Comments should match", comments, rs.getString("comments"));
        } catch (SQLException e) {
            fail("SQLException: " + e.getMessage());
        }
    }

    @Test
    public void testReceiveNotification() {
        // This is a method stub in the original code
        // Just verify it doesn't throw an exception
        student.receiveNotification();
    }
}