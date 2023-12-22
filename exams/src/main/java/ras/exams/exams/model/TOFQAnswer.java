package ras.exams.exams.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TOFQAnswer {
    private int grade;
    private boolean answer;
    private TOFQ option;

    public TOFQAnswer(@JsonProperty("grade")int grade,@JsonProperty("answer") boolean answer,@JsonProperty("o") TOFQ option){   
        this.grade = grade;
        this.answer = answer;
        this.option = option;
    }

    public int getGrade(){
        return this.grade;
    }

    public boolean getAnswer(){
        return this.answer;
    }

    public TOFQ getOption(){
        return this.option;
    }
}
