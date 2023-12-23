package ras.exams.exams.model;

import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TrueOrFalse extends Question{
    List<TOFQ> questions;

    public TrueOrFalse(@JsonProperty("id") UUID id,@JsonProperty("question") String question,
        @JsonProperty("qn") int qn, @JsonProperty("versionNumber") int versionNumber,
        @JsonProperty("questions") List<TOFQ> questions){
        super(id, question, qn, 'T', versionNumber);
        this.questions = questions;
    }

    public TrueOrFalse(UUID id, String question, int qn, UUID versionID, List<TOFQ> questions){
        super(id, question, qn, 'T', versionID);
        this.questions = questions;
    }

    public List<TOFQ> getQuestions(){
        return this.questions;
    }
}
