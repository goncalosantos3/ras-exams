package ras.exams.exams.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TOFQ {
    private String description;
    private boolean correction;
    private int score;
    private int optionNumber;

    public TOFQ(@JsonProperty("desc") String desc,@JsonProperty("correc") boolean correc,
        @JsonProperty("score") int score,@JsonProperty("optionNumber") int optionNumber){
        this.description = desc;
        this.correction = correc;
        this.score = score;
        this.optionNumber = optionNumber;
    }

    // Este construtor é usado como auxiliar ao adicionar respostas de alunos
    public TOFQ(int optionNumber){
        this.optionNumber = optionNumber;
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

    public int getOptionNumber(){
        return this.optionNumber;
    }
}
