package ras.exams.exams.model;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

public abstract class Question {
    private final UUID questionId;
    private String question;
    private int questionNumber;
    private char questionType; // 'M', 'T', 'W' or 'C'
    private UUID versionID; 
    private int versionNumber;
    
    // Chamado pela BD
    public Question(UUID id, String question, int qn, char qtype, UUID versionID){
        this.questionId = id;
        this.question = question;
        this.questionNumber = qn;
        this.questionType = qtype;
        this.versionID = versionID;
    }
    
    // Chamado pelas sub-classes (CompleteSpaces, Writing, etc.)
    public Question(UUID id, String question, int qn, char qtype, int versionNumber){
        this.questionId = id;
        this.question = question;
        this.questionNumber = qn;
        this.questionType = qtype;
        this.versionNumber = versionNumber;
    }

    public void setVersionID(UUID versionID){
        this.versionID = versionID;
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

    public int getVersionNumber(){
        return versionNumber;
    }

    public String toString(){
        return "Question Number: " + this.questionNumber + "\n" + this.question + "\n";
    }
}
