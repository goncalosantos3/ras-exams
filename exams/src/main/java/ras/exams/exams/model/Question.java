package ras.exams.exams.model;

import java.util.UUID;

public abstract class Question {
    private final UUID questionId;
    private String question;
    private int questionNumber;

    public Question(UUID id, String question, int qn){
        this.questionId = id;
        this.question = question;
        this.questionNumber = qn;
    }

    public UUID getQuestionId(){
        return this.questionId;
    }

    public String getQuestion(){
        return this.question;
    }

    public int getQuestionNumber(){
        return this.questionNumber;
    }
}
