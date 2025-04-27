package com.example.fefs;

import java.sql.ResultSet;
import java.util.List;

class Faculty extends User {
    public Faculty(int userId, String name, String email, String password, String role) {
        super(userId, name, email, password, role);
    }

    public List<String[]> viewFeedbackReports()
    {
        return Database.getFeedback(this.userId);
    }
}
