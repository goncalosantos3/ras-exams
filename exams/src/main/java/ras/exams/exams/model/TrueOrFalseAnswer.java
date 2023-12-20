package ras.exams.exams.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TrueOrFalseAnswer extends Answer{
    private List<TOFQAnswer> answers;
    
    public TrueOrFalseAnswer(@JsonProperty("grade") int grade,@JsonProperty("answers") List<TOFQAnswer> answers){
        super(grade);
        this.answers = answers;
    }

    public List<TOFQAnswer> getAnswers(){
        return this.answers;
    }
}   
