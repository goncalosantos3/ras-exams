package ras.exams.exams.model;

import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ExamAnswer {
    private final UUID examAnswerId;
    private int grade;  
    private List<Answer> answers;

    public ExamAnswer(@JsonProperty("id") UUID id){
        this.examAnswerId = id;
    }

    public ExamAnswer(@JsonProperty("id") UUID id,@JsonProperty("grade") int grade,@JsonProperty("answers") List<Answer> answers){
        this.examAnswerId = id; 
        this.grade = grade;
        this.answers = answers;
    }

    public UUID getExamAnswerId(){
        return this.examAnswerId;
    }

    public int getGrade(){
        return this.grade;
    }

    public List<Answer> getAnswers(){
        return this.answers;
    }
}
