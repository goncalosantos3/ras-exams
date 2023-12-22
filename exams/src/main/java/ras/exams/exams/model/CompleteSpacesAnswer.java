package ras.exams.exams.model;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CompleteSpacesAnswer extends Answer{
    private String text;
    private Question question;

    public CompleteSpacesAnswer(@JsonProperty("grade") int grade, @JsonProperty("examAnswerID") UUID examAnswerID,@JsonProperty("answer") String answer,@JsonProperty("q") Question q){
        super(grade, examAnswerID);
        this.text = answer;
        this.question = q;
    }

    public String getText(){
        return this.text;
    }

    public Question getQuestion(){
        return this.question;
    }
}
