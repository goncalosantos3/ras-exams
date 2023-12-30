package ras.exams.exams.model;

import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TrueOrFalseAnswer extends Answer{
    private List<TOFQAnswer> answers;
    
    // Construtor usado pelas rotas do controller
    public TrueOrFalseAnswer(@JsonProperty("grade") int grade, @JsonProperty("answers") List<TOFQAnswer> answers){
        super(UUID.randomUUID(), grade, 'T');
        this.answers = answers;
    }

    // Construtor usado pela base de dados
    public TrueOrFalseAnswer(UUID answerID, int grade, UUID examAnswerID, List<TOFQAnswer> answers, UUID questionID){
        super(answerID, grade, examAnswerID, 'M', questionID);
        this.answers = answers;
    }

    public List<TOFQAnswer> getAnswers(){
        return this.answers;
    }
}   
