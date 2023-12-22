package ras.exams.exams.model;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CompleteSpaces extends Question{ 
    private String text;  
    
    public CompleteSpaces(@JsonProperty("id") UUID id,@JsonProperty("question") String question,@JsonProperty("qn") int qn, @JsonProperty("versionID") UUID versionID,@JsonProperty("text") String text){
        super(id, question, qn, 'C', versionID);
        this.text = text;
    }

    public String getText(){
        return this.text;
    }
}
