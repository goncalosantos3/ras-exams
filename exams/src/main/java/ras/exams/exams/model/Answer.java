package ras.exams.exams.model;

import java.util.UUID;

public abstract class Answer {
    private UUID answerID;
    private UUID examAnswerID;
    private UUID questionID;
    private int grade;
    private char type;
    
    // Construtor usado pelas rotas do controller
    public Answer(UUID answerID, int grade, char type){
        this.answerID = UUID.randomUUID();
        this.grade = grade; 
        this.type = type;
    }

    // Usado pela base de dados
    public Answer(UUID answerID, int grade, UUID examAnswerID, char type, UUID questionID){
        this.answerID = answerID;
        this.grade = grade; 
        this.examAnswerID = examAnswerID;
        this.type = type;
        this.questionID = questionID;
    }

    public abstract int autoCorrect();
    
    public UUID getAnswerID() {
        return answerID;
    }
    
    public UUID getQuestionID() {
        return questionID;
    }
    
    public void setQuestionID(UUID qid){
        this.questionID = qid;
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

    public void setExamAnswerID(UUID eaid){
        this.examAnswerID = eaid;
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
