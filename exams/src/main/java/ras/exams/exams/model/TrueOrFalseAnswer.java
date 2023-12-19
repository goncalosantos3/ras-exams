package ras.exams.exams.model;

import java.util.List;

public class TrueOrFalseAnswer extends Answer{
    private List<TOFQAnswer> answers;
    
    public TrueOrFalseAnswer(int grade, List<TOFQAnswer> answers){
        super(grade);
        this.answers = answers;
    }
}   
