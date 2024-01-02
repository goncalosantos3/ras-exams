package ras.exams.exams.model;

import java.util.UUID;

public abstract class Question {
    private final UUID questionId;
    private UUID versionID;
    private String question;
    private int questionNumber;
    private int score;
    private char questionType; // 'M', 'T', 'W' or 'C'
    
    public Question(UUID id, String question, int qn, int score, char qtype, UUID versionID){
        this.questionId = id;
        this.question = question;
        this.questionNumber = qn;
        this.questionType = qtype;
        this.versionID = versionID;
    }

    public void setVersionID(UUID versionID){
        this.versionID = versionID;
    }

    public void setScore(int score){
        this.score = score;
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

    public char getQuestionType(){
        return Character.toUpperCase(questionType);
    }

    public UUID getVersionID() {
        return versionID;
    }

    public String toString(){
        return "Question Number: " + this.questionNumber + "\n" + this.question + "\n";
    }
}
