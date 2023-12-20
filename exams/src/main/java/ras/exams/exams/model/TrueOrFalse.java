package ras.exams.exams.model;

import java.util.List;
import java.util.UUID;

public class TrueOrFalse extends Question{
    List<TOFQ> questions;

    public TrueOrFalse(@JsonProperty("id") UUID id,@JsonProperty("question") String question,@JsonProperty("qn") int qn,@JsonProperty("questions") List<TOFQ> questions){
        super(id, question, qn);
        this.questions = questions;
    }

    public List<TOFQ> getQuestions(){
        return this.questions;
    }
}
