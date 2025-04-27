package com.example.fefs;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static org.junit.Assert.*;

public class DatabaseTest {

    private Database db;
    private static final String TEST_USERNAME = "root";
    private static final String TEST_PASSWORD = "raja@168";
    private static final String TEST_URL = "jdbc:mysql://localhost:3306/FacultyEvaluationDB";

    private Connection conn;

    // Test user IDs to be used and cleaned up
    private int testFacultyId;
    private int testStudentId;
    private int testCourseId;

    @Before
    public void setUp() throws SQLException {
        db = Database.getInstance();
        conn = DriverManager.getConnection(TEST_URL, TEST_USERNAME, TEST_PASSWORD);

        // Create test data - Faculty
        String insertFaculty = "INSERT INTO Users (name, email, password_, role) VALUES ('Test Faculty', 'test.faculty@test.com', 'testpass', 'Faculty')";
        PreparedStatement stmt = conn.prepareStatement(insertFaculty, java.sql.Statement.RETURN_GENERATED_KEYS);
        stmt.executeUpdate();
        ResultSet rs = stmt.getGeneratedKeys();
        if (rs.next()) {
            testFacultyId = rs.getInt(1);
            String insertFacultyDept = "INSERT INTO Faculty (user_id, department) VALUES (?, 'Test Department')";
            PreparedStatement deptStmt = conn.prepareStatement(insertFacultyDept);
            deptStmt.setInt(1, testFacultyId);
            deptStmt.executeUpdate();
        }

        String insertStudent = "INSERT INTO Users (name, email, password_, role) VALUES ('Test Student', 'test.student@test.com', 'testpass', 'Student')";
        stmt = conn.prepareStatement(insertStudent, java.sql.Statement.RETURN_GENERATED_KEYS);
        stmt.executeUpdate();
        rs = stmt.getGeneratedKeys();
        if (rs.next()) {
            testStudentId = rs.getInt(1);
            String insertStudentBatch = "INSERT INTO Students (user_id, batch) VALUES (?, '2023')";
            PreparedStatement batchStmt = conn.prepareStatement(insertStudentBatch);
            batchStmt.setInt(1, testStudentId);
            batchStmt.executeUpdate();
        }

        String insertCourse = "INSERT INTO Courses (course_name, course_code) VALUES ('Test Course', 'TC101')";
        stmt = conn.prepareStatement(insertCourse, java.sql.Statement.RETURN_GENERATED_KEYS);
        stmt.executeUpdate();
        rs = stmt.getGeneratedKeys();
        if (rs.next()) {
            testCourseId = rs.getInt(1);
            String insertTeaching = "INSERT INTO Teaching (faculty_id, course_id) VALUES (?, ?)";
            PreparedStatement teachingStmt = conn.prepareStatement(insertTeaching);
            teachingStmt.setInt(1, testFacultyId);
            teachingStmt.setInt(2, testCourseId);
            teachingStmt.executeUpdate();
        }
    }

    @After
    public void tearDown() throws SQLException {
        String deleteFeedback = "DELETE FROM Feedback WHERE faculty_id = ?";
        PreparedStatement stmt = conn.prepareStatement(deleteFeedback);
        stmt.setInt(1, testFacultyId);
        stmt.executeUpdate();

        String deleteTeaching = "DELETE FROM Teaching WHERE faculty_id = ? AND course_id = ?";
        stmt = conn.prepareStatement(deleteTeaching);
        stmt.setInt(1, testFacultyId);
        stmt.setInt(2, testCourseId);
        stmt.executeUpdate();

        String deleteFaculty = "DELETE FROM Faculty WHERE user_id = ?";
        stmt = conn.prepareStatement(deleteFaculty);
        stmt.setInt(1, testFacultyId);
        stmt.executeUpdate();

        String deleteStudent = "DELETE FROM Students WHERE user_id = ?";
        stmt = conn.prepareStatement(deleteStudent);
        stmt.setInt(1, testStudentId);
        stmt.executeUpdate();

        String deleteUsers = "DELETE FROM Users WHERE user_id IN (?, ?)";
        stmt = conn.prepareStatement(deleteUsers);
        stmt.setInt(1, testFacultyId);
        stmt.setInt(2, testStudentId);
        stmt.executeUpdate();

        String deleteCourse = "DELETE FROM Courses WHERE course_id = ?";
        stmt = conn.prepareStatement(deleteCourse);
        stmt.setInt(1, testCourseId);
        stmt.executeUpdate();

        String deleteNotifs = "DELETE FROM Notifications WHERE message = 'Test notification'";
        stmt = conn.prepareStatement(deleteNotifs);
        stmt.executeUpdate();

        if (conn != null) {
            conn.close();
        }
    }

    @Test
    public void testGetInstance() {
        Database instance1 = Database.getInstance();
        Database instance2 = Database.getInstance();

        assertNotNull(instance1);
        assertSame(instance1, instance2);
    }

    @Test
    public void testSignIn() {
        boolean result = db.signIn("test.faculty@test.com", "testpass", "Faculty");
        assertTrue("Faculty sign-in should succeed", result);

        result = db.signIn("test.student@test.com", "testpass", "Student");
        assertTrue("Student sign-in should succeed", result);

        result = db.signIn("nonexistent@test.com", "testpass", "Faculty");
        assertFalse("Non-existent user sign-in should fail", result);

        result = db.signIn("test.faculty@test.com", "wrongpass", "Faculty");
        assertFalse("Sign-in with wrong password should fail", result);
    }

    @Test
    public void testWriteFeedback() {
        int rating = 4;
        String comments = "Test feedback comments";

        Database.submitFeedback(testFacultyId, testCourseId, rating, comments);

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
    public void testReturnCourses() {
        List<String[]> courses = Database.returnCourses();

        assertNotNull("Courses list should not be null", courses);
        boolean found = false;
        for (String[] course : courses) {
            if (course[0].equals("Test Course")) {
                found = true;
                assertEquals("Course ID should match", String.valueOf(testCourseId), course[1]);
                break;
            }
        }
        assertTrue("Test course should be found in the list", found);
    }

    @Test
    public void testReturnInstructors() {
        List<String[]> instructors = Database.returnInstructors("Test Course");

        assertNotNull("Instructors list should not be null", instructors);
        boolean found = false;
        for (String[] instructor : instructors) {
            if (instructor[0].equals("Test Faculty")) {
                found = true;
                assertEquals("Faculty ID should match", String.valueOf(testFacultyId), instructor[1]);
                break;
            }
        }
        assertTrue("Test faculty should be found for the test course", found);
    }

    @Test
    public void testGetInstructorID() {
        int id = db.getInstructorID("test.faculty@test.com", "testpass", "Faculty");
        assertEquals("Faculty ID should match", testFacultyId, id);

        id = db.getInstructorID("nonexistent@example.com", "testpass", "Faculty");
        assertEquals("Non-existent faculty should return -1", -1, id);
    }

    @Test
    public void testGetFeedback() {
        Database.submitFeedback(testFacultyId, testCourseId, 4, "Good professor");

        List<String[]> feedbackList = Database.getFeedback(testFacultyId);

        assertNotNull("Feedback list should not be null", feedbackList);
        assertFalse("Feedback list should not be empty", feedbackList.isEmpty());

        String[] feedback = feedbackList.get(0);
        assertEquals("Course ID should match", String.valueOf(testCourseId), feedback[1]);
        assertEquals("Rating should match", "4", feedback[2]);
        assertEquals("Comments should match", "Good professor", feedback[3]);
    }

    @Test
    public void testAddAndRemoveFaculty() {
        // Add faculty
        boolean result = db.addFaculty("New Faculty", "new.faculty@test.com", "newpass", "Computer Science");
        assertTrue("Adding faculty should succeed", result);

        // Verify faculty was added
        try {
            String query = "SELECT u.user_id, f.department FROM Users u JOIN Faculty f ON u.user_id = f.user_id WHERE u.email = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, "new.faculty@test.com");
            ResultSet rs = stmt.executeQuery();

            assertTrue("Faculty should be found in database", rs.next());
            assertEquals("Department should match", "Computer Science", rs.getString("department"));

            // Remove faculty
            result = db.remFaculty("New Faculty", "new.faculty@test.com", "newpass", "Computer Science");
            assertTrue("Removing faculty should succeed", result);

            // Verify faculty was removed
            stmt = conn.prepareStatement("SELECT * FROM Users WHERE email = ?");
            stmt.setString(1, "new.faculty@test.com");
            rs = stmt.executeQuery();

            assertFalse("Faculty should be removed from database", rs.next());
        } catch (SQLException e) {
            fail("SQLException: " + e.getMessage());
        }
    }

    @Test
    public void testAddAndRemoveStudent() {
        // Add student
        boolean result = db.addStudent("New Student", "new.student@test.com", "newpass", "2024");
        assertTrue("Adding student should succeed", result);

        // Verify student was added
        try {
            String query = "SELECT u.user_id, s.batch FROM Users u JOIN Students s ON u.user_id = s.user_id WHERE u.email = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, "new.student@test.com");
            ResultSet rs = stmt.executeQuery();

            assertTrue("Student should be found in database", rs.next());
            assertEquals("Batch should match", "2024", rs.getString("batch"));

            // Remove student
            result = db.remStudent("New Student", "new.student@test.com", "newpass", "2024");
            assertTrue("Removing student should succeed", result);

            // Verify student was removed
            stmt = conn.prepareStatement("SELECT * FROM Users WHERE email = ?");
            stmt.setString(1, "new.student@test.com");
            rs = stmt.executeQuery();

            assertFalse("Student should be removed from database", rs.next());
        } catch (SQLException e) {
            fail("SQLException: " + e.getMessage());
        }
    }

    @Test
    public void testSendAndGetNotifs() {
        // Send notification
        boolean result = db.sendNotifs("Test notification");
        assertTrue("Sending notification should succeed", result);

        // Get notifications
        List<String[]> notifs = db.getNotifs();

        assertNotNull("Notifications list should not be null", notifs);

        boolean found = false;
        for (String[] notif : notifs) {
            if (notif[1].equals("Test notification")) {
                found = true;
                break;
            }
        }
        assertTrue("Test notification should be found in the list", found);
    }
}