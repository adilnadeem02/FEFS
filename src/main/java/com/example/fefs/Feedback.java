package com.example.fefs;

class Feedback {
    private int feedbackId;
    private int studentId;
    private int facultyId;
    private int courseId;
    private int rating;
    private String comments;

    public Feedback(int feedbackId, int studentId, int facultyId, int courseId, int rating, String comments) {
        this.feedbackId = feedbackId;
        this.studentId = studentId;
        this.facultyId = facultyId;
        this.courseId = courseId;
        this.rating = rating;
        this.comments = comments;
    }

    public void storeFeedback(){}
    public void getFeedbackByFaculty(int facultyId){}
}