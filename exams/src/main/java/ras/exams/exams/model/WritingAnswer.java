package ras.exams.exams.model;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

public class WritingAnswer extends Answer{
    private String text;
    private Question question;

    // Construtor usado pelas rotas do controller
    public WritingAnswer(@JsonProperty("text") String text){
        super(UUID.randomUUID(), 0, 'W');
        this.text = text;
    }

    public void setQuestion(Question q){
        this.question = q;
    }

    // Construtor usado pela base de dados
    public WritingAnswer(UUID answerID, int grade, UUID examAnswerID, String text, Question q){
        super(answerID, grade, examAnswerID, 'W', q.getQuestionId());
        this.text = text;
        this.question = q;
    }

    // This type of answer can't be auto-corrected
    public int autoCorrect(){
        return 0;
    }

    public String getText(){
        return this.text;
    }

    public Question getQuestion(){
        return this.question;
    }
}
