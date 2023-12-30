package ras.exams.exams.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Choice {
    private String description;
    private boolean correction;
    private int score;
    private int choiceNumber;

    public Choice(@JsonProperty("desc") String desc,@JsonProperty("correc") boolean correc,
        @JsonProperty("score") int score,@JsonProperty("choiceNumber") int choiceNumber){
        this.description = desc;
        this.correction = correc;
        this.score = score;
        this.choiceNumber = choiceNumber;
    }

    // Este construtor é usado como auxiliar ao adicionar respostas de alunos
    public Choice(int cn){
        this.choiceNumber = cn;
    }

    public String getDescription(){
        return this.description;
    }

    public boolean getCorrection(){
        return this.correction;
    }

    public int getScore(){
        return this.score;
    }

    public int getChoiceNumber(){
        return this.choiceNumber;
    }
    
}
