package ras.exams.exams.service;

import ras.exams.exams.data.ExamDao;
import ras.exams.exams.model.Exam;
import ras.exams.exams.model.ExamHeader;
import ras.exams.exams.model.Question;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Service;

@Service
public class ExamsService {
    private Map<UUID, Exam> exams;
    private Map<String, UUID> namesIds; // Maps exam's names to ids

    public ExamsService(){
        this.exams = ExamDao.getInstance();
        this.namesIds = new HashMap<>();
        for(Map.Entry<UUID, Exam> e : this.exams.entrySet()){
            this.namesIds.put(e.getValue().getHeader().getExamName(), e.getKey()); 
        }

        // this.exams = new HashMap<>();
        // this.namesIds = new HashMap<>();
    }
    
    // Creates a new Exam with only it's name
    public void createExam(String examName){
        UUID examId = UUID.randomUUID();
        this.exams.put(examId, new Exam(examId, examName));
        this.namesIds.put(examName, examId);
        System.out.println("\n" + exams.get(examId));
    }

    // Add a exam header to an existing exam
    public void addExamHeader(ExamHeader header){
        UUID examId = this.namesIds.get(header.getExamName());
        this.exams.get(examId).setHeader(header);
        System.out.println("\n" + exams.get(examId));
        this.exams.put(examId, this.exams.get(examId));
    }

    public boolean addExamVersion(String examName, int versionNumber){
        UUID examId = this.namesIds.get(examName);
        boolean res = this.exams.get(examId).addExamVersion(versionNumber);
        this.exams.put(examId, this.exams.get(examId));
        return res;
    }

    public boolean removeExamVersion(String examName, int versionNumber){
        UUID examId = this.namesIds.get(examName);
        boolean res = this.exams.get(examId).removeExamVersion(versionNumber);
        this.exams.put(examId, this.exams.get(examId));
        return res;
    }

    // Add a new question to an exam of every type
    public boolean addQuestion(String examName, Question q){
        Exam e = this.exams.get(this.namesIds.get(examName));
        boolean res = e.addQuestion(q.getVersionNumber(), q);
        this.exams.put(e.getID(), e);
        return res;
    }

    // Return a question of any type
    public Question getQuestion(String examName, String versionNumber, String questionNumber){
        Exam e = this.exams.get(this.namesIds.get(examName));
        return e.getQuestion(Integer.parseInt(versionNumber), Integer.parseInt(questionNumber));
    }   

    public boolean updateQuestion(String examName, Question q){
        Exam e = this.exams.get(this.namesIds.get(examName));
        boolean res = e.updateQuestion(q.getVersionNumber(), q);
        this.exams.put(e.getID(), e);
        return res;
    }   
    
    public List<Exam> getExams(){
        List<Exam> res = new ArrayList<>();
        for (Map.Entry<UUID, Exam> entry : this.exams.entrySet()){
            res.add(entry.getValue());
        }
        return res;
    }
    
    public ExamHeader getExamHeader(String examName){
        ExamHeader eh = null;
        
        if(this.namesIds.containsKey(examName)){
            eh = this.exams.get(this.namesIds.get(examName)).getHeader();   
        }
        
        return eh;
    }
    
    public int editExamHeader(String examName, ExamHeader examHeader) {
        Exam e = null;
        if (this.namesIds.containsKey(examName)){
            e = this.exams.get(this.namesIds.get(examName));
            e.setHeader(examHeader);
            this.exams.put(this.namesIds.get(examName), e);
            return 200;
        }
        else {
            return 404;
        }
    }

    public List <Exam> getExambyStudent(String studentNumber){
        List <Exam> res = new ArrayList<>();
        for (Map.Entry<UUID, Exam> entry : this.exams.entrySet()) {
            Exam e = entry.getValue();
            List<String> enrolled = e.getEnrolled();
            if (enrolled.contains(studentNumber)){
                res.add(e);
            }
        }
        return res;
    }

    public int enrollStudent(String examName,String studentNumber){
        Exam e = null;
        if (this.namesIds.containsKey(examName)){
            e = this.exams.get(this.namesIds.get(examName));
            e.setEnrolled(studentNumber);
            return 200;
        }
        return 404;
    }

    public Exam getSpecificExamforStudent(String studentNumber,String examName){
        Exam e = null;
        if (this.namesIds.containsKey(examName)){
            e = this.exams.get(this.namesIds.get(examName));
            List<String> enrolled = e.getEnrolled();
            if (enrolled.contains(studentNumber)){
                return e;
            }
        }
        return e;
    }
}
