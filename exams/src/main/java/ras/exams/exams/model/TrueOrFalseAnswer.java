package ras.exams.exams.model;

import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TrueOrFalseAnswer extends Answer{
    private List<TOFQAnswer> answers;
    
    // Construtor usado pelas rotas do controller
    public TrueOrFalseAnswer(@JsonProperty("answers") List<TOFQAnswer> answers){
        super(UUID.randomUUID(), 0, 'T');
        this.answers = answers;
    }

    // Construtor usado pela base de dados
    public TrueOrFalseAnswer(UUID answerID, int grade, UUID examAnswerID, List<TOFQAnswer> answers, UUID questionID){
        super(answerID, grade, examAnswerID, 'T', questionID);
        this.answers = answers;
    }

    public int autoCorrect(){
        if(this.getGrade() == 0){
            int r = 0;
            for(TOFQAnswer ta: this.answers){
                r += ta.autoCorrect();
            }
            System.out.println("Cotação TOFA: " + r);
            this.setGrade(r);
            return r;
        }
        return 0; // 0 para não aumentar no ExamAnswer
    }

    public List<TOFQAnswer> getAnswers(){
        return this.answers;
    }
}   
