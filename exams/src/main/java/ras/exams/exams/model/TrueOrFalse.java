package ras.exams.exams.model;

import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TrueOrFalse extends Question{
    List<TOFQ> questions;

    // Chamado pelas rotas do controller
    public TrueOrFalse(@JsonProperty("question") String question, @JsonProperty("qn") int qn, 
        @JsonProperty("versionID") String versionID, @JsonProperty("questions") List<TOFQ> questions){
        super(UUID.randomUUID(), question, qn, 0, 'T', UUID.fromString(versionID));
        this.questions = questions;

        int score = 0;
        for(TOFQ q: this.questions){
            score += q.getScore();
        }
        this.setScore(score);
    }

    // Chamado pela BD
    public TrueOrFalse(UUID id, String question, int qn, int score, UUID versionID, List<TOFQ> questions){
        super(id, question, qn, score, 'T', versionID);
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
