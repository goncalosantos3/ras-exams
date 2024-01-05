package ras.exams.exams.api;

import java.util.List;

public class NotificationsRequest {
    private List<String> studentIDs;
    private String examID;

    public NotificationsRequest(String examID, List<String> studentIDs){
        this.studentIDs = studentIDs;
        this.examID = examID;
    }
}
