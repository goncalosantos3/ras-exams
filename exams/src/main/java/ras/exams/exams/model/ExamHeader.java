package ras.exams.exams.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ExamHeader {
    private String examName;
    private String examUC;
    private String examAdmissionTime;
    private List<String> examScheduleIDs;
    
    public ExamHeader(@JsonProperty("examName")String examName,@JsonProperty("examUC") String examUC,@JsonProperty("examAdmissionTime") String examAdmissionTime){
        this.examName = examName;
        this.examUC = examUC;
        this.examAdmissionTime = examAdmissionTime;
    }

    public ExamHeader(@JsonProperty("examName")String examName,@JsonProperty("examUC") String examUC,@JsonProperty("examAdmissionTime") String examAdmissionTime,@JsonProperty("schedule") List<String> schedule){
        this.examName = examName;
        this.examUC = examUC;
        this.examAdmissionTime = examAdmissionTime;
        this.examScheduleIDs = schedule;
    }

    public String getExamName(){
        return this.examName;
    }

    public String getExamUC(){
        return this.examUC;
    }

    public String getExamAdmissionTime(){
        return this.examAdmissionTime;
    }

    public List<String> getExamScheduleIDs(){
        return this.examScheduleIDs;
    }
}
