package ras.exams.exams.model;

import java.util.UUID;
import com.fasterxml.jackson.annotation.JsonProperty;

public class CompleteSpaces extends Question{ 
    private String text;  
    
    // Chamado pelas rotas do controller
    public CompleteSpaces(@JsonProperty("question") String question, @JsonProperty("qn") int qn,
         @JsonProperty("versionID") String versionID, @JsonProperty("text") String text){
        super(UUID.randomUUID(), question, qn, 0, 'C', UUID.fromString(versionID));
        this.text = text;
        this.setScore(this.getScore());
    }

    // Chamado pela BD
    public CompleteSpaces(UUID id, String question, int qn, int score, UUID versionID, String text){
        super(id, question, qn, score, 'C', versionID);
        this.text = text;
    }

    public int getScore()
    {
        return 0;
    }

    public String getText(){
        return this.text;
    }
}
