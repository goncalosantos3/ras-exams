package ras.exams.exams.model;

import java.util.UUID;

public class CompleteSpaces extends Question{ 
    private String text;  
    
    public CompleteSpaces(@JsonProperty("id") UUID id,@JsonProperty("question") String question,@JsonProperty("qn") int qn,@JsonProperty("text") String text){
        super(id, question, qn);
        this.text = text;
    }

    public String getText(){
        return this.text;
    }
}
