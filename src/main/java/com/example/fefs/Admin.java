package com.example.fefs;

class Administrator extends User {
    public Administrator(int userId, String name, String email, String password, String role) {
        super(userId, name, email, password, role);
    }

    public void generateAnalytics(){}
    public void manageUserAccounts(int userId, String action){}
    public void customizeEvaluationCriteria(int criteriaId, String newCriteria){}
    public void flagFacultyPerformance(int facultyId, String flagReason){}
}
