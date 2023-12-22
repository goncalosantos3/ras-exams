package ras.exams.exams.model;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

public abstract class Answer {
    private int grade;
    private UUID examAnswerID;
    
    public Answer(@JsonProperty("grade") int grade, @JsonProperty("examAnswerID") UUID examAnswerID){
        this.grade = grade; 
        this.examAnswerID = examAnswerID;
    }
    
    public int getGrade(){
        return this.grade;
    }

    public UUID getExamAnswerID() {
        return examAnswerID;
    }
}
