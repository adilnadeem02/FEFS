package com.example.fefs;

import javafx.scene.chart.PieChart;

class Student extends User {
    public Student(int userId, String name, String email, String password, String role) {
        super(userId, name, email, password, role);
    }

    public void submitFeedback(int facultyId, int courseId, int rating, String comments)
    {
        Database db = Database.getInstance();
        db.writeFeedback(userId, facultyId, courseId, rating, comments);
    }

    public void receiveNotification(){}
}