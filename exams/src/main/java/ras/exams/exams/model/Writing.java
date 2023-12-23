package ras.exams.exams.model;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Writing extends Question{
    private String criteria;
    private int minimumLimit;
    private int maximumLimit;

    // Chamado pelas rotas do controller
    public Writing(@JsonProperty("question") String question,
        @JsonProperty("qn") int qn, @JsonProperty("versionNumber") int versionNumber,
        @JsonProperty("criteria") String criteria, @JsonProperty("min") int min,
        @JsonProperty("max") int max){
        super(UUID.randomUUID(), question, qn, 'W', versionNumber);
        this.criteria = criteria;
        this.minimumLimit = min;
        this.maximumLimit = max;
    }

    // Chamado pela BD
    public Writing(UUID questionID, String question, int qn, UUID versionID, String criteria, int min, int max){
        super(questionID, question, qn, 'W', versionID);
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
