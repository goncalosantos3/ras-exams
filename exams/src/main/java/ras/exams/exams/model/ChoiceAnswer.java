package ras.exams.exams.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ChoiceAnswer {
    private boolean selected;
    private int grade;
    private Question question;

    public ChoiceAnswer(@JsonProperty("selected") boolean selected,@JsonProperty("grade") int grade,@JsonProperty("q") Question q){
        this.selected = selected;
        this.grade = grade;
        this.question = q;
    }

    public boolean getSelected(){
        return this.selected;
    }

    public int getGrade(){
        return this.grade;
    }

    public Question getQuestion(){
        return this.question;
    }
}
