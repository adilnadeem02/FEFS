package com.example.fefs;

import java.sql.ResultSet;

class Faculty extends User {
    public Faculty(int userId, String name, String email, String password, String role) {
        super(userId, name, email, password, role);
    }

    public ResultSet viewFeedbackReports()
    {
        return Database.getFeedbackForFaculty(this.userId);
    }
}
