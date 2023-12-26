package ras.exams.exams.model;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

public abstract class Answer {
    private UUID answerID;
    private UUID examAnswerID;
    private UUID questionID;
    private int grade;
    private char type;
    
    public Answer(@JsonProperty("grade") int grade, @JsonProperty("examAnswerID") UUID examAnswerID, @JsonProperty("type") char type, @JsonProperty("questionID") UUID questionID){
        this.answerID = UUID.randomUUID();
        this.grade = grade; 
        this.examAnswerID = examAnswerID;
        this.type = type;
        this.questionID = questionID;
    }

    public Answer(UUID answerID, int grade, UUID examAnswerID, char type, UUID questionID){
        this.answerID = answerID;
        this.grade = grade; 
        this.examAnswerID = examAnswerID;
        this.type = type;
        this.questionID = questionID;
    }
    
    public UUID getAnswerID() {
        return answerID;
    }
    
    public UUID getQuestionID() {
        return questionID;
    }
    
    public int getGrade(){
        return this.grade;
    }

    public void setGrade(int grade){
        this.grade = grade;
    }

    public UUID getExamAnswerID() {
        return examAnswerID;
    }

    public char getAnswerType() {
        return this.type;
    }

    @Override
    public String toString()
    {
        return "Answer{ ID:"+answerID+" ExamAnswerID:"+examAnswerID+" QuestionID:"+questionID+" Type:"+type+" Grade:"+grade+" }";
    }
}
