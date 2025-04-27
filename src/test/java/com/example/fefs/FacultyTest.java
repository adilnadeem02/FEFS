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

public class FacultyTest {

    private Faculty faculty;
    private static final int USER_ID = 2001;
    private static final String NAME = "Faculty Test";
    private static final String EMAIL = "faculty.test@example.com";
    private static final String PASSWORD = "facultypass";
    private static final String ROLE = "Faculty";

    // Database connection info
    private static final String TEST_URL = "jdbc:mysql://localhost:3306/FacultyEvaluationDB";
    private static final String TEST_USERNAME = "root";
    private static final String TEST_PASSWORD = "raja@168";

    private Connection conn;
    private int testStudentId;
    private int testCourseId;

    @Before
    public void setUp() throws SQLException {
        faculty = new Faculty(USER_ID, NAME, EMAIL, PASSWORD, ROLE);
        conn = DriverManager.getConnection(TEST_URL, TEST_USERNAME, TEST_PASSWORD);

        // Create test user in database for faculty
        String insertFaculty = "INSERT INTO Users (user_id, name, email, password_, role) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement stmt = conn.prepareStatement(insertFaculty);
        stmt.setInt(1, USER_ID);
        stmt.setString(2, NAME);
        stmt.setString(3, EMAIL);
        stmt.setString(4, PASSWORD);
        stmt.setString(5, ROLE);
        stmt.executeUpdate();

        // Add entry to Faculty table
        String insertFacultyDept = "INSERT INTO Faculty (user_id, department) VALUES (?, 'Computer Science')";
        stmt = conn.prepareStatement(insertFacultyDept);
        stmt.setInt(1, USER_ID);
        stmt.executeUpdate();

        // Create test student
        String insertStudent = "INSERT INTO Users (name, email, password_, role) VALUES ('Test Student', 'test.student@test.com', 'password', 'Student')";
        stmt = conn.prepareStatement(insertStudent, java.sql.Statement.RETURN_GENERATED_KEYS);
        stmt.executeUpdate();
        ResultSet rs = stmt.getGeneratedKeys();
        if (rs.next()) {
            testStudentId = rs.getInt(1);
            // Add entry to Students table
            String insertStudentBatch = "INSERT INTO Students (user_id, batch) VALUES (?, '2023')";
            PreparedStatement batchStmt = conn.prepareStatement(insertStudentBatch);
            batchStmt.setInt(1, testStudentId);
            batchStmt.executeUpdate();
        }

        // Create test course
        String insertCourse = "INSERT INTO Courses (course_name, course_code) VALUES ('Test Course', 'TC101')";
        stmt = conn.prepareStatement(insertCourse, java.sql.Statement.RETURN_GENERATED_KEYS);
        stmt.executeUpdate();
        rs = stmt.getGeneratedKeys();
        if (rs.next()) {
            testCourseId = rs.getInt(1);
        }

        // Add test feedback
        String insertFeedback = "INSERT INTO Feedback (faculty_id, course_id, rating, comments, created_at) VALUES ( ?, ?, 5, 'Great teaching!', NOW())";
        stmt = conn.prepareStatement(insertFeedback);
        stmt.setInt(1, USER_ID);
        stmt.setInt(2, testCourseId);
        stmt.executeUpdate();
    }

    @After
    public void tearDown() throws SQLException {
        // Clean up test data
        String deleteFeedback = "DELETE FROM Feedback WHERE faculty_id = ?";
        PreparedStatement stmt = conn.prepareStatement(deleteFeedback);
        stmt.setInt(1, USER_ID);
        stmt.executeUpdate();

        String deleteFaculty = "DELETE FROM Faculty WHERE user_id = ?";
        stmt = conn.prepareStatement(deleteFaculty);
        stmt.setInt(1, USER_ID);
        stmt.executeUpdate();

        String deleteUser = "DELETE FROM Users WHERE user_id = ?";
        stmt = conn.prepareStatement(deleteUser);
        stmt.setInt(1, USER_ID);
        stmt.executeUpdate();

        String deleteStudent = "DELETE FROM Students WHERE user_id = ?";
        stmt = conn.prepareStatement(deleteStudent);
        stmt.setInt(1, testStudentId);
        stmt.executeUpdate();

        stmt = conn.prepareStatement(deleteUser);
        stmt.setInt(1, testStudentId);
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
    public void testFacultyInheritance() {
        // Test that Faculty inherits from User
        assertTrue(faculty instanceof User);

        // Test inherited getters
        assertEquals("User ID should match", USER_ID, faculty.getUserId());
        assertEquals("Name should match", NAME, faculty.getName());
        assertEquals("Email should match", EMAIL, faculty.getEmail());
        assertEquals("Password should match", PASSWORD, faculty.getPassword());
        assertEquals("Role should match", ROLE, faculty.getRole());
    }
}