package ras.exams.exams.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MultipleChoiceAnswer extends Answer{
    private List<ChoiceAnswer> answers;

    public MultipleChoiceAnswer(@JsonProperty("grade")int grade,@JsonProperty("answers") List<ChoiceAnswer> answers){
        super(grade);
        this.answers = answers;
    }

    public List<ChoiceAnswer> getAnswers(){
        return this.answers;
    }
}
