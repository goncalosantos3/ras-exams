package ras.exams.exams.model;

import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ExamAnswer {
    private final UUID examAnswerId, examID, studentID;
    private int grade;  
    private List<Answer> answers;

    public ExamAnswer(@JsonProperty("id") UUID id, @JsonProperty("examID") UUID examID, @JsonProperty("studentID") UUID studentID){
        this.examAnswerId = id;
        this.examID = examID;
        this.studentID = studentID;
    }

    public ExamAnswer(@JsonProperty("id") UUID id, @JsonProperty("examID") UUID examID, @JsonProperty("studentID") UUID studentID, @JsonProperty("grade") int grade,@JsonProperty("answers") List<Answer> answers){
        this.examAnswerId = id; 
        this.examID = examID;
        this.studentID = studentID;
        this.grade = grade;
        this.answers = answers;
    }

    public UUID getExamAnswerId(){
        return this.examAnswerId;
    }

    public int getGrade(){
        return this.grade;
    }

    public void calculateGrade() {
        this.grade = 0;
        for (Answer a : this.answers)
        {
            this.grade += a.getGrade();
        }
    }

    public List<Answer> getAnswers(){
        return this.answers;
    }

    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }

    public UUID getStudentID() {
        return studentID;
    }

    public UUID getExamID() {
        return examID;
    }

}
