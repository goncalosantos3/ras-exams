package ras.exams.exams.model;

import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TrueOrFalseAnswer extends Answer{
    private List<TOFQAnswer> answers;
    
    public TrueOrFalseAnswer(@JsonProperty("grade") int grade, @JsonProperty("examAnswerID") UUID examAnswerID,@JsonProperty("answers") List<TOFQAnswer> answers, @JsonProperty("questionID") UUID questionID){
        super(grade, examAnswerID, 'T', questionID);
        this.answers = answers;
    }

    public TrueOrFalseAnswer(UUID answerID, int grade, UUID examAnswerID, List<TOFQAnswer> answers, UUID questionID){
        super(answerID, grade, examAnswerID, 'M', questionID);
        this.answers = answers;
    }

    public List<TOFQAnswer> getAnswers(){
        return this.answers;
    }
}   
