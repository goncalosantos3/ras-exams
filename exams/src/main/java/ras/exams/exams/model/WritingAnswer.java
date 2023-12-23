package ras.exams.exams.model;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

public class WritingAnswer extends Answer{
    private String text;
    private Question question;

    public WritingAnswer(@JsonProperty("grade") int grade, @JsonProperty("examAnswerID") UUID examAnswerID,@JsonProperty("text") String text,@JsonProperty("q") Question q){
        super(grade, examAnswerID, 'W', q.getQuestionId());
        this.text = text;
        this.question = q;
    }

    public WritingAnswer(UUID answerID, int grade, UUID examAnswerID, String text, Question q){
        super(answerID, grade, examAnswerID, 'W', q.getQuestionId());
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
