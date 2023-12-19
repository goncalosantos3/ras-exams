package ras.exams.exams.model;

import java.util.UUID;

public class CompleteSpaces extends Question{ 
    private String text;  
    
    public CompleteSpaces(UUID id, String question, int qn, String text){
        super(id, question, qn);
        this.text = text;
    }
}
