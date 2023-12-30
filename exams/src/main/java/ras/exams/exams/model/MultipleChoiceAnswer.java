package ras.exams.exams.model;

import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MultipleChoiceAnswer extends Answer{
    private List<ChoiceAnswer> answers;

    // Construtor usado pelas rotas do controller
    public MultipleChoiceAnswer(@JsonProperty("grade")int grade, @JsonProperty("choices") List<ChoiceAnswer> choices){
        super(UUID.randomUUID(), grade, 'M');
        this.answers = choices;
    }

    // Construtor usado pela base de dados
    public MultipleChoiceAnswer(UUID answerID, int grade, UUID examAnswerID, List<ChoiceAnswer> answers, UUID questionID){
        super(answerID, grade, examAnswerID, 'M', questionID);
        this.answers = answers;
    }

    public List<ChoiceAnswer> getAnswers(){
        return this.answers;
    }
}
