package ras.exams.exams.model;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Writing extends Question{
    private String criteria;
    private int minimumLimit;
    private int maximumLimit;

    public Writing(@JsonProperty("id") UUID id,@JsonProperty("question") String question,@JsonProperty("qn") int qn, @JsonProperty("versionID") UUID versionID,@JsonProperty("criteria") String criteria,@JsonProperty("min") int min,@JsonProperty("max") int max){
        super(id, question, qn, 'W', versionID);
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
