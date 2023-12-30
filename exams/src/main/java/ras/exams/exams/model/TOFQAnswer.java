package ras.exams.exams.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TOFQAnswer {
    private int grade;
    private boolean answer;
    private TOFQ option;

    // Construtor usado pelas rotas do controller
    public TOFQAnswer(@JsonProperty("grade")int grade, @JsonProperty("answer") boolean answer, @JsonProperty("optionNumber") int on){   
        this.grade = grade;
        this.answer = answer;
        this.option = new TOFQ(on);
    }

    // Construtor usado pela base de dados
    public TOFQAnswer(int grade, boolean answer, TOFQ option){
        this.grade = grade;
        this.answer = answer;
        this.option = option;
    }

    public void setOption(TOFQ o){
        this.option = o;
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
