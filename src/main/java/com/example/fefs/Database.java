package com.example.fefs;
import java.sql.*;
import java.util.*;

class Database {
    private static final String USERNAME = "root";
    private static final String PASSWORD = "raja@168";
    private static final String URL = "jdbc:mysql://localhost:3306/FacultyEvaluationDB";
    private static Database db = null;

    private Database()
    {}

    public static Database getInstance()
    {
        if(db == null)
            db = new Database();
        return db;
    }

    public static String getUsername() {
        return USERNAME;
    }

    public static String getPassword() {
        return PASSWORD;
    }

    public static String getUrl() {
        return URL;
    }

    public static void writeFeedback(int studentId, int facultyId, int courseId, int rating, String comments) {
        String query = "INSERT INTO Feedback (student_id, faculty_id, course_id, rating, comments, created_at) VALUES (?, ?, ?, ?, ?, NOW())";

        try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, studentId);
            pstmt.setInt(2, facultyId);
            pstmt.setInt(3, courseId);
            pstmt.setInt(4, rating);
            pstmt.setString(5, comments);

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Feedback successfully recorded.");
            } else {
                System.out.println("Failed to record feedback.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static ResultSet getFeedbackForFaculty(int facultyId) {
        String query = "SELECT f.feedback_id, s.name AS student_name, c.course_name, f.rating, f.comments, f.created_at " +
                "FROM Feedback f " +
                "JOIN Students s ON f.student_id = s.student_id " +
                "JOIN Courses c ON f.course_id = c.course_id " +
                "WHERE f.faculty_id = ?";

        try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, facultyId);
            ResultSet rs = pstmt.executeQuery();

            return rs;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean signIn(String username, String password, String role) {
        String query = "SELECT * FROM Users WHERE email = ? AND password_ = ? AND role = ?";

        try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.setString(3, role);

            ResultSet rs = pstmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
