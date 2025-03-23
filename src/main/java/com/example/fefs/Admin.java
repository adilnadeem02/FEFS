package com.example.fefs;

class Admin extends User {
    public Admin(int userId, String name, String email, String password, String role) {
        super(userId, name, email, password, role);
    }

    public void generateAnalytics(){}
    public void manageUserAccounts(int userId, String action){}
    public void customizeEvaluationCriteria(int criteriaId, String newCriteria){}
    public void flagFacultyPerformance(int facultyId, String flagReason){}
}
