package ras.exams.exams.model;

import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TrueOrFalse extends Question{
    List<TOFQ> questions;

    public TrueOrFalse(@JsonProperty("id") UUID id,@JsonProperty("question") String question,@JsonProperty("qn") int qn, @JsonProperty("versionID") UUID versionID,@JsonProperty("questions") List<TOFQ> questions){
        super(id, question, qn, 'T', versionID);
        this.questions = questions;
    }

    public List<TOFQ> getQuestions(){
        return this.questions;
    }
}
