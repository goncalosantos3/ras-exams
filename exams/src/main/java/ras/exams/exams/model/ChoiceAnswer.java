package ras.exams.exams.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ChoiceAnswer {
    private boolean selected;
    private int grade;
    private Choice choice; // question

    // Construtor usado pelas rotas do controller
    public ChoiceAnswer(@JsonProperty("selected") boolean selected, 
        @JsonProperty("choiceNumber") int cn){
        this.selected = selected;
        this.grade = 0;
        this.choice = new Choice(cn);
    }

    // Construtor usado pela base de dados
    public ChoiceAnswer(boolean selected, int grade, Choice c){
        this.selected = selected;
        this.grade = grade;
        this.choice = c;
    }

    public void setChoice(Choice c){
        this.choice = c;
    }

    public boolean getSelected(){
        return this.selected;
    }

    public int getGrade(){
        return this.grade;
    }

    public void setGrade(int grade){
        this.grade = grade;
    }

    public Choice getChoice(){
        return this.choice;
    }
}
