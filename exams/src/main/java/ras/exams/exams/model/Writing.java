package ras.exams.exams.model;

import java.util.UUID;

public class Writing extends Question{
    private String criteria;
    private int minimumLimit;
    private int maximumLimit;

    public Writing(UUID id, String question, int qn, String criteria, int min, int max){
        super(id, question, qn);
        this.criteria = criteria;
        this.minimumLimit = min;
        this.maximumLimit = max;
    }

    public String getCriteria(){
        return this.criteria;
    }

    public int getMinimumLimit(){
        return this.minimumLimit;
    }

    public int getMaximumLimit(){
        return this.maximumLimit;
    }
}
