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
    private Map<Integer, ExamVersion> versions;
    private Map<UUID, ExamAnswer> answers;

    public Exam(UUID id, String examName){
        this.id = id;
        this.enrolled = new ArrayList<>();
        this.header = new ExamHeader(examName);
        this.versions = new HashMap<>();
        this.answers = new HashMap<>();
    }

    public Exam(@JsonProperty("id") UUID id, @JsonProperty("header") ExamHeader header){
        this.id = id;
        this.enrolled = new ArrayList<>();
        this.header = header;
        this.versions = new HashMap<>();
        this.answers = new HashMap<>();
    }

    public Exam(UUID examID, List<String> enrolled, ExamHeader header, 
                Map<Integer, ExamVersion> versions, Map<UUID, ExamAnswer> answers) {
        this.id = examID;
        this.enrolled = new ArrayList<>(enrolled);
        this.header = header;
        this.versions = new HashMap<>(versions);
        this.answers = new HashMap<>(answers);
    }

    public boolean addExamVersion(int versionNumber){
        if(this.versions.containsKey(versionNumber)){
            return false;
        }

        UUID examVersionID = UUID.randomUUID();
        this.versions.put(versionNumber, new ExamVersion(examVersionID, this.id, versionNumber));
        return true;
    }

    public boolean removeExamVersion(int versionNumber){
        if(!this.versions.containsKey(versionNumber)){
            return false;
        }

        this.versions.remove(versionNumber);
        return true;
    }

    // Returns false if versionNumber doesn't exist
    public boolean addQuestion(int versionNumber, Question q){
        if(!this.versions.containsKey(versionNumber)){
            return false;
        }

        return this.versions.get(versionNumber).addQuestion(q);
    }

    // Returns a specific question from a specific exam version
    public Question getQuestion(int versionNumber, int questionNumber){
        return this.versions.get(versionNumber).getQuestion(questionNumber);
    }

    public boolean updateQuestion(int versionNumber, Question q){
        if(!this.versions.containsKey(versionNumber)){
            return false;
        }

        return this.versions.get(versionNumber).updateQuestion(q);
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
        this.header.setExamId(this.id);
    }

    public Map<Integer, ExamVersion> getVersions(){
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
