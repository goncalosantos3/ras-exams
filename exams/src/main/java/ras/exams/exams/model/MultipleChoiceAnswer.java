package ras.exams.exams.model;

import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MultipleChoiceAnswer extends Answer{
    private List<ChoiceAnswer> answers;

    public MultipleChoiceAnswer(@JsonProperty("grade")int grade, @JsonProperty("examAnswerID") UUID examAnswerID,@JsonProperty("answers") List<ChoiceAnswer> answers){
        super(grade, examAnswerID);
        this.answers = answers;
    }

    public List<ChoiceAnswer> getAnswers(){
        return this.answers;
    }
}
