package ras.exams.exams.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ChoiceAnswer {
    private boolean selected;
    private int grade;
    private Choice choice;

    public ChoiceAnswer(@JsonProperty("selected") boolean selected,@JsonProperty("grade") int grade,@JsonProperty("c") Choice c){
        this.selected = selected;
        this.grade = grade;
        this.choice = c;
    }

    public boolean getSelected(){
        return this.selected;
    }

    public int getGrade(){
        return this.grade;
    }

    public Choice getChoice(){
        return this.choice;
    }
}
