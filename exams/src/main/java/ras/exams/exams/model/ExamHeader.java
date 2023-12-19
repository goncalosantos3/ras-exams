package ras.exams.exams.model;

import java.util.List;

public class ExamHeader {
    private String examName;
    private String examUC;
    private String examAdmissionTime;
    private List<String> examScheduleIDs;
    
    public ExamHeader(String examName, String examUC, String examAdmissionTime){
        this.examName = examName;
        this.examUC = examUC;
        this.examAdmissionTime = examAdmissionTime;
    }

    public ExamHeader(String examName, String examUC, String examAdmissionTime, List<String> schedule){
        this.examName = examName;
        this.examUC = examUC;
        this.examAdmissionTime = examAdmissionTime;
        this.examScheduleIDs = schedule;
    }
}
