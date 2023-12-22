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
    private Map<UUID ,ExamVersion> versions;
    private Map<UUID,ExamAnswer> answers;

    public Exam(UUID id, String examName){
        this.id = id;
        this.header = new ExamHeader(examName);
        this.versions = new HashMap<>();
    }

    public Exam(@JsonProperty("id") UUID id,@JsonProperty("header") ExamHeader header){
        this.id = id;
        this.enrolled = new ArrayList<>();
        this.header = header;
        this.versions = new HashMap<>();
        this.answers = new HashMap<>();
    }

    public void addMultipleChoiceQuestion(UUID versionId, MultipleChoice mc){
        if(!this.versions.containsKey(versionId)){
            this.versions.put(versionId, new ExamVersion(versionId));
        }

        this.versions.get(versionId).addMultipleChoiceQuestion(mc);
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
    
    public void setHeader(ExamHeader header){
        this.header = header;
    }

    public Map<UUID, ExamVersion> getVersions(){
        return this.versions;
    }

    public Map<UUID,ExamAnswer> getAnswers(){
        return this.answers;
    }

    public String toString(){
        String str = "Exam-\nId: " + this.id + "\nName: " + this.header.getExamName();
        str += "\nExamUC: " + this.header.getExamUC() + "\nAdmissionTime: " + this.header.getExamAdmissionTime();
        return str;
    }
}
