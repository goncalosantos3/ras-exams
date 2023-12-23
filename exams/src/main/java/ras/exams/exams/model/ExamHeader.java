package ras.exams.exams.model;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ExamHeader {
    private UUID examHeaderID, examID;
    private String examName;
    private String examUC;
    private LocalTime examAdmissionTime;
    private List<UUID> examScheduleIDs;   
    
    public ExamHeader(String examName){
        this.examName = examName;
    }

    public ExamHeader(String examName, String examUC, String examAdmissionTime){
        this.examName = examName;
        this.examUC = examUC;
        this.examAdmissionTime = LocalTime.parse(examAdmissionTime, DateTimeFormatter.ofPattern("HH:mm"));
    }

    public ExamHeader(  @JsonProperty("examHeaderID") UUID examHeaderID, @JsonProperty("examID") UUID examID, 
                        @JsonProperty("examName")String examName, @JsonProperty("examUC") String examUC, 
                        @JsonProperty("examAdmissionTime") String examAdmissionTime, @JsonProperty("schedule") List<String> schedule){
        this.examHeaderID = examHeaderID;
        this.examID = examID;
        this.examName = examName;
        this.examUC = examUC;
        this.examAdmissionTime = LocalTime.parse(examAdmissionTime, DateTimeFormatter.ofPattern("HH:mm"));
        this.examScheduleIDs = schedule.stream().map(id -> UUID.fromString(id)).toList();
    }
    
    public UUID getExamHeaderID() {
        return examHeaderID;
    }
    
    public UUID getExamID() {
        return examID;
    }
    
    public String getExamName(){
        return this.examName;
    }

    public String getExamUC(){
        return this.examUC;
    }

    public LocalTime getExamAdmissionTime(){
        return this.examAdmissionTime;
    }

    public String getFormatedExamAdmissionTime(){
        return this.examAdmissionTime.format(DateTimeFormatter.ofPattern("HH:mm"));
    }

    public List<UUID> getExamScheduleIDs(){
        return this.examScheduleIDs;
    }
}
