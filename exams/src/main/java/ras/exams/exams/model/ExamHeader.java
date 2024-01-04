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
    private char status;    
    /* Status:
     *  - 'S': exame ainda não começou (skecth)
     *  - 'P': exame em progresso (progress)
     *  - 'F': exame terminado (finished)
     */
    
    
    // Construtor para as rotas do controller
    public ExamHeader(@JsonProperty("examName") String examName, @JsonProperty("examUC") String examUC, 
        @JsonProperty("examAdmissionTime") String examAT, @JsonProperty("examScheduleIDs") List<UUID> examSIds,
        @JsonProperty("status") char status){
        this.examHeaderID = UUID.randomUUID();
        this.examName = examName;
        this.examUC = examUC;
        this.examAdmissionTime = LocalTime.parse(examAT, DateTimeFormatter.ofPattern("HH:mm"));
        this.examScheduleIDs = examSIds;
        this.status = status;
    }

    // Construtor para a BD
    public ExamHeader(UUID examHeaderID, UUID examID, String examName, String examUC, 
            String examAdmissionTime, List<String> schedule){
        this.examHeaderID = examHeaderID;
        this.examID = examID;
        this.examName = examName;
        this.examUC = examUC;
        this.examAdmissionTime = (examAdmissionTime==null) ?null :LocalTime.parse(examAdmissionTime, DateTimeFormatter.ofPattern("HH:mm"));
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
        return (this.examAdmissionTime==null) ?null :this.examAdmissionTime.format(DateTimeFormatter.ofPattern("HH:mm"));
    }

    public List<UUID> getExamScheduleIDs(){
        return this.examScheduleIDs;
    }

    public char getStatus(){
        return this.status;
    }

    public void setExamId(UUID examId){
        this.examID = examId;
    }
}
