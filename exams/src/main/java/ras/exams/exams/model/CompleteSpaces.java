package ras.exams.exams.model;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CompleteSpaces extends Question{ 
    private String text;  
    
    // Chamado pelas rotas do controller
    public CompleteSpaces(@JsonProperty("question") String question, @JsonProperty("qn") int qn, 
        @JsonProperty("versionID") String versionID, @JsonProperty("text") String text){
        super(UUID.randomUUID(), question, qn, 'C', UUID.fromString(versionID));
        this.text = text;
    }

    // Chamado pela BD
    public CompleteSpaces(UUID id, String question, int qn, UUID versionID, String text){
        super(id, question, qn, 'C', versionID);
        this.text = text;
    }

    public String getText(){
        return this.text;
    }
}
