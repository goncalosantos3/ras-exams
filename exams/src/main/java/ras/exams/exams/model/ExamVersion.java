package ras.exams.exams.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ExamVersion {
    private final UUID versionId;
    private List<Question> questions;

    public ExamVersion(@JsonProperty("id") UUID id){
        this.versionId = id;
        this.questions = new ArrayList<>();
    }

    public void addMultipleChoiceQuestion(MultipleChoice mc){
        this.questions.add(mc.getQuestionNumber(), mc);
    }

    public UUID getVersionId(){
        return this.versionId;
    }

    public List<Question> getQuestions(){
        return this.questions;
    }
}
