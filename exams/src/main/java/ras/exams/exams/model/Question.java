package ras.exams.exams.model;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

public abstract class Question {
    private final UUID questionId;
    private String question;
    private int questionNumber;
    private char questionType; // 'M', 'T', 'W' or 'C'
    private final UUID versionID; 
    
    public Question(@JsonProperty("id")UUID id,@JsonProperty("question") String question,@JsonProperty("qn") int qn, @JsonProperty("qtype") char qtype, @JsonProperty("versionID") UUID versionID){
        this.questionId = id;
        this.question = question;
        this.questionNumber = qn;
        this.questionType = qtype;
        this.versionID = versionID;
    }

    public UUID getQuestionId(){
        return this.questionId;
    }

    public String getQuestion(){
        return this.question;
    }

    public int getQuestionNumber(){
        return this.questionNumber;
    }

    public char getQuestionType(){
        return Character.toUpperCase(questionType);
    }

    public UUID getVersionID() {
        return versionID;
    }
}
