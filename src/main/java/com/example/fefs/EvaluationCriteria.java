package com.example.fefs;

class EvaluationCriteria {
    private int criteriaId;
    private String criteriaName;
    private String description;

    public EvaluationCriteria(int criteriaId, String criteriaName, String description) {
        this.criteriaId = criteriaId;
        this.criteriaName = criteriaName;
        this.description = description;
    }

    public void modifyCriteria(int criteriaId, String newCriteria){}
}
