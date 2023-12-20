package ras.exams.exams.model;

import java.util.List;
import java.util.UUID;

public class ExamVersion {
    private final UUID versionId;
    private List<Question> questions;

    public ExamVersion(@JsonProperty("id") UUID id){
        this.versionId = id;
    }

    public UUID getVersionId(){
        return this.versionId;
    }

    public List<Question> getQuestions(){
        return this.questions;
    }
}
