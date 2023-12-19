package ras.exams.exams.model;

import java.util.List;
import java.util.UUID;

public class ExamVersion {
    private final UUID versionId;
    private List<Question> questions;

    public ExamVersion(UUID id){
        this.versionId = id;
    }
}
