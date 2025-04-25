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

    public static List<String[]> returnCourses() {
        String query = "SELECT course_name, course_id FROM Courses";
        List<String[]> courses = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                courses.add(new String[]{rs.getString("course_name"), String.valueOf(rs.getInt("course_id"))});
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return courses;  // Return list of courses
    }

    // Returns a list of instructors teaching a given course
    public static List<String[]> returnInstructors(String course) {
        String query = "SELECT DISTINCT U.user_id, U.name\n" +
                "FROM Teaching T\n" +
                "JOIN Users U ON T.faculty_id = U.user_id\n" +
                "JOIN Courses C ON T.course_id = C.course_id\n" +
                "WHERE C.course_name = ?;";
        List<String[]> instructors = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, course);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    instructors.add(new String[]{rs.getString("name"), String.valueOf(rs.getInt("user_id"))});
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return instructors;  // Return list of instructors
    }

    public void submitFeedback(int instructorID, int courseID, int i, String text)
    {
        String query = "INSERT INTO Feedback (faculty_id, course_id, rating, comments, created_at) VALUES (?, ?, ?, ?, NOW())";

        try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, instructorID);
            pstmt.setInt(2, courseID);
            pstmt.setInt(3, i);
            pstmt.setString(4, text);

            int rowsAffected = pstmt.executeUpdate();


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getInstructorID(String u, String p, String role)
    {
        String query = "SELECT user_id FROM Users WHERE email = ? AND password_ = ? AND role = ?";

        try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, u);
            pstmt.setString(2, p);
            pstmt.setString(3, role);

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("user_id");

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
            return -1;
    }

    public static List<String[]> getFeedback(int faculty_id) {
        List<String[]> feedbackList = new ArrayList<>();
        String query = "SELECT feedback_id, course_id, rating, comments, created_at FROM Feedback WHERE faculty_id = ?";

        try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, faculty_id);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                String[] feedback = new String[5];
                feedback[0] = String.valueOf(rs.getInt("feedback_id"));
                feedback[1] = String.valueOf(rs.getInt("course_id"));
                feedback[2] = String.valueOf(rs.getInt("rating"));
                feedback[3] = rs.getString("comments");
                feedback[4] = rs.getTimestamp("created_at").toString();
                feedbackList.add(feedback);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return feedbackList;
    }

    public boolean addFaculty(String name, String email, String pass, String dep) {
        try(Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD)) {
            String insertUser = "INSERT INTO Users (name, email, password_, role) VALUES (?, ?, ?, 'Faculty')";
            PreparedStatement stmt = conn.prepareStatement(insertUser, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, name);
            stmt.setString(2, email);
            stmt.setString(3, pass);
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                int userId = rs.getInt(1);
                String insertFaculty = "INSERT INTO Faculty (user_id, department) VALUES (?, ?)";
                PreparedStatement stmtFaculty = conn.prepareStatement(insertFaculty);
                stmtFaculty.setInt(1, userId);
                stmtFaculty.setString(2, dep);
                stmtFaculty.executeUpdate();
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean remFaculty(String name, String email, String pass, String dep) {
        try(Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD)) {
            String getUserId = "SELECT user_id FROM Users WHERE email = ? AND password_ = ? AND role = 'Faculty'";
            PreparedStatement stmt = conn.prepareStatement(getUserId);
            stmt.setString(1, email);
            stmt.setString(2, pass);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int userId = rs.getInt("user_id");
                String deleteUser = "DELETE FROM Users WHERE user_id = ?";
                PreparedStatement delStmt = conn.prepareStatement(deleteUser);
                delStmt.setInt(1, userId);
                delStmt.executeUpdate();
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean addStudent(String name, String email, String pass, String batch) {
        try(Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD)) {
            String insertUser = "INSERT INTO Users (name, email, password_, role) VALUES (?, ?, ?, 'Student')";
            PreparedStatement stmt = conn.prepareStatement(insertUser, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, name);
            stmt.setString(2, email);
            stmt.setString(3, pass);
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                int userId = rs.getInt(1);
                String insertStudent = "INSERT INTO Students (user_id, batch) VALUES (?, ?)";
                PreparedStatement stmtStudent = conn.prepareStatement(insertStudent);
                stmtStudent.setInt(1, userId);
                stmtStudent.setString(2, batch);
                stmtStudent.executeUpdate();
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean remStudent(String name, String email, String pass, String batch) {
        try(Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD)) {
            String getUserId = "SELECT user_id FROM Users WHERE email = ? AND password_ = ? AND role = 'Student'";
            PreparedStatement stmt = conn.prepareStatement(getUserId);
            stmt.setString(1, email);
            stmt.setString(2, pass);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int userId = rs.getInt("user_id");
                String deleteUser = "DELETE FROM Users WHERE user_id = ?";
                PreparedStatement delStmt = conn.prepareStatement(deleteUser);
                delStmt.setInt(1, userId);
                delStmt.executeUpdate();
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<String[]> getNotifs()
    {
        List<String[]> feedbackList = new ArrayList<>();
        String query = "SELECT * FROM Notifications";

        try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                String[] feedback = new String[5];
                feedback[0] = String.valueOf(rs.getInt("notification_id"));
                feedback[1] = String.valueOf(rs.getString("message"));
                feedback[2] = String.valueOf(rs.getString("created_at"));
                feedbackList.add(feedback);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return feedbackList;
    }

    public boolean sendNotifs(String text)
    {
        try(Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD)) {
            String insertUser = "INSERT INTO Notifications (message) VALUES (?)";
            PreparedStatement stmt = conn.prepareStatement(insertUser, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, text);
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
