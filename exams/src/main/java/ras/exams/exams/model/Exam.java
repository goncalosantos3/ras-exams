package ras.exams.exams.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Exam {
    private final UUID id;
    private List<String> enrolled;
    private ExamHeader header;
    private List<ExamVersion> versions;
    private Map<UUID,ExamAnswer> answers;

    public Exam(@JsonProperty("id") UUID id,@JsonProperty("header") ExamHeader header){
        this.id = id;
        this.enrolled = new ArrayList<>();
        this.header = header;
        this.versions = new ArrayList<>();
        this.answers = new HashMap<>();
    }

    public UUID getID(){
        return this.id;
    }

    public List<String> getEnrolled(){
        return this.enrolled;
    }

    public ExamHeader getHeader(){
        return this.header;
    }

    public List<ExamVersion> getVersions(){
        return this.versions;
    }

    public Map<UUID,ExamAnswer> getAnswers(){
        return this.answers;
    }


}
