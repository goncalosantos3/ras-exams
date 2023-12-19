package ras.exams.exams.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Exam {
    private final UUID id;
    private List<String> enrolled;
    private ExamHeader header;
    private List<ExamVersion> versions;
    private Map<UUID,ExamAnswer> answers;

    public Exam(UUID id, ExamHeader header){
        this.id = id;
        this.enrolled = new ArrayList<>();
        this.header = header;
        this.versions = new ArrayList<>();
        this.answers = new HashMap<>();
    }
}
