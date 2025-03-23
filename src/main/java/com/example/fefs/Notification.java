package com.example.fefs;

class Notification {
    private int notificationId;
    private int userId;
    private String message;
    private boolean isRead;

    public Notification(int notificationId, int userId, String message, boolean isRead) {
        this.notificationId = notificationId;
        this.userId = userId;
        this.message = message;
        this.isRead = isRead;
    }

    public void sendNotification(int userId, String message){}
    public void markAsRead(){}
}
