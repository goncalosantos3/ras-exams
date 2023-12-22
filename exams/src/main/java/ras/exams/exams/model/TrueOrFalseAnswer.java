package ras.exams.exams.model;

import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TrueOrFalseAnswer extends Answer{
    private List<TOFQAnswer> answers;
    
    public TrueOrFalseAnswer(@JsonProperty("grade") int grade, @JsonProperty("examAnswerID") UUID examAnswerID,@JsonProperty("answers") List<TOFQAnswer> answers){
        super(grade, examAnswerID);
        this.answers = answers;
    }

    public List<TOFQAnswer> getAnswers(){
        return this.answers;
    }
}   
