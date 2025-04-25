package com.example.fefs;

public class NotifClass {
    private int notificationId;
    private String message;
    private String createdAt;

    public NotifClass(int notificationId, String message, String createdAt) {
        this.notificationId = notificationId;
        this.message = message;
        this.createdAt = createdAt;
    }

    public int getNotificationId() {
        return notificationId;
    }

    public String getMessage() {
        return message;
    }

    public String getCreatedAt() {
        return createdAt;
    }
}
