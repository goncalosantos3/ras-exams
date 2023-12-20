package ras.exams.exams.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class WritingAnswer extends Answer{
    private String text;
    private Question question;

    public WritingAnswer(@JsonProperty("grade") int grade,@JsonProperty("text") String text,@JsonProperty("q") Question q){
        super(grade);
        this.text = text;
        this.question = q;
    }

    public String getText(){
        return this.text;
    }

    public Question getQuestion(){
        return this.question;
    }
}
