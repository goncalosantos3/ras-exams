package ras.exams.exams.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public abstract class Answer {
    private int grade;

    public Answer(@JsonProperty("grade") int grade){
        this.grade = grade; 
    }

    public int getGrade(){
        return this.grade;
    }
}
