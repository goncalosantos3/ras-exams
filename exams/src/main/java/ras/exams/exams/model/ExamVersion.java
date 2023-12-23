package ras.exams.exams.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ExamVersion {
    private final UUID versionId, examID;
    private int versionNumber;
    private List<Question> questions;
    
    public ExamVersion(@JsonProperty("id") UUID id, @JsonProperty("examID") UUID examID, @JsonProperty("versionNumber") int versionNumber){
        this.versionId = id;
        this.examID = examID;
        this.versionNumber = versionNumber;
        this.questions = new ArrayList<>();
    }
    
    public void addQuestions(List<Question> questions)
    {
        this.questions.addAll(questions);
    }

    public void addMultipleChoiceQuestion(MultipleChoice mc){
        this.questions.add(mc.getQuestionNumber(), mc);
    }
    
    public UUID getVersionId(){
        return this.versionId;
    }
    
    public UUID getExamID() {
        return examID;
    }
    
    public int getVersionNumber() {
        return versionNumber;
    }
    
    public List<Question> getQuestions(){
        return this.questions;
    }
}
