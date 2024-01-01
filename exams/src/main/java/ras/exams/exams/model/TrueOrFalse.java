package ras.exams.exams.model;

import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TrueOrFalse extends Question{
    List<TOFQ> questions;

    // Chamado pelas rotas do controller
    public TrueOrFalse(@JsonProperty("question") String question,
        @JsonProperty("qn") int qn, @JsonProperty("versionID") String versionID,
        @JsonProperty("questions") List<TOFQ> questions){
        super(UUID.randomUUID(), question, qn, 'T', UUID.fromString(versionID));
        this.questions = questions;
    }

    // Chamado pela BD
    public TrueOrFalse(UUID id, String question, int qn, UUID versionID, List<TOFQ> questions){
        super(id, question, qn, 'T', versionID);
        this.questions = questions;
    }

    public List<TOFQ> getQuestions(){
        return this.questions;
    }

    public TOFQ getQuestionOnOption(int on){
        for(TOFQ tofq: this.questions){
            if(tofq.getOptionNumber() == on){
                return tofq;
            }
        }
        return null;
    }
}
