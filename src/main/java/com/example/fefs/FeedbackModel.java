package com.example.fefs;

public class FeedbackModel {
    private int feedbackId;
    private int courseId;
    private int rating;
    private String comments;
    private String createdAt;

    public FeedbackModel(int feedbackId, int courseId, int rating, String comments, String createdAt) {
        this.feedbackId = feedbackId;
        this.courseId = courseId;
        this.rating = rating;
        this.comments = comments;
        this.createdAt = createdAt;
    }

    public int getFeedbackId() { return feedbackId; }
    public int getCourseId() { return courseId; }
    public int getRating() { return rating; }
    public String getComments() { return comments; }
    public String getCreatedAt() { return createdAt; }
}

