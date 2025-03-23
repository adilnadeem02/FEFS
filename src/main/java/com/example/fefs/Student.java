package com.example.fefs;

class Student extends User {
    public Student(int userId, String name, String email, String password, String role) {
        super(userId, name, email, password, role);
    }

    public void submitFeedback(int facultyId, int courseId, int rating, String comments){}
    public void receiveNotification(){}
}