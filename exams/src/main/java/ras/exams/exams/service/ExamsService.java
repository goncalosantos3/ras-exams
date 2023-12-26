package ras.exams.exams.service;

import ras.exams.exams.data.ExamDAO;
import ras.exams.exams.model.Exam;
import ras.exams.exams.model.ExamAnswer;
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
    private Map<String, Exam> exams;
    // private Map<String, UUID> namesIds; // Maps exam's names to ids

    public ExamsService(){
        this.exams = ExamDAO.getInstance();
        // this.namesIds = new HashMap<>();
        // for(Map.Entry<UUID, Exam> e : this.exams.entrySet()){
        //     this.namesIds.put(e.getValue().getHeader().getExamName(), e.getKey()); 
        // }

        // this.exams = new HashMap<>();
        // this.namesIds = new HashMap<>();
    }
    
    // Creates a new Exam with only it's name
    public void createExam(String examName){
        UUID examId = UUID.randomUUID();
        this.exams.put(examName, new Exam(examId, examName));
        // this.namesIds.put(examName, examId);
        System.out.println("\n" + exams.get(examId));
    }

    // Add a exam header to an existing exam
    public boolean addExamHeader(ExamHeader header){
        if(!this.exams.containsKey(header.getExamName())){
            return false;
        }

        String examName = header.getExamName();
        Exam exam = this.exams.get(examName);
        exam.setHeader(header);
        this.exams.put(header.getExamName(), exam);
        return true;
    }

    public boolean addExamVersion(String examName, int versionNumber){
        Exam exam = this.exams.get(examName);
        boolean res = exam.addExamVersion(versionNumber);
        this.exams.put(examName, exam);
        return res;
    }

    public boolean removeExamVersion(String examName, int versionNumber){
        Exam exam = this.exams.get(examName);
        boolean res = exam.removeExamVersion(versionNumber);
        this.exams.put(examName, exam);
        return res;
    }

    // Add a new question to an exam of every type
    public boolean addQuestion(String examName, Question q){
        Exam e = this.exams.get(examName);
        boolean res = e.addQuestion(q.getVersionNumber(), q);
        this.exams.put(examName, e);
        return res;
    }

    // Return a question of any type
    public Question getQuestion(String examName, String versionNumber, String questionNumber){
        Exam e = this.exams.get(examName);
        return e.getQuestion(Integer.parseInt(versionNumber), Integer.parseInt(questionNumber));
    }   

    public boolean updateQuestion(String examName, Question q){
        Exam e = this.exams.get(examName);
        boolean res = e.updateQuestion(q.getVersionNumber(), q);
        this.exams.put(examName, e);
        return res;
    }   
    
    public List<Exam> getExams(){
        List<Exam> res = new ArrayList<>();
        for (Map.Entry<String, Exam> entry : this.exams.entrySet()){
            res.add(entry.getValue());
        }
        return res;
    }
    
    public ExamHeader getExamHeader(String examName){
        ExamHeader eh = null;
        
        if(this.exams.containsKey(examName)){
            eh = this.exams.get(examName).getHeader();   
        }
        
        return eh;
    }
    
    public int editExamHeader(String examName, ExamHeader examHeader) {
        Exam e = null;
        if (this.exams.containsKey(examName)){
            e = this.exams.get(examName);
            e.setHeader(examHeader);
            this.exams.put(examName, e);
            return 200;
        }
        else {
            return 404;
        }
    }

    public List <Exam> getExambyStudent(String studentNumber){
        List <Exam> res = new ArrayList<>();
        for (Map.Entry<String, Exam> entry : this.exams.entrySet()) {
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
        if (this.exams.containsKey(examName)){
            e = this.exams.get(examName);
            e.setEnrolled(studentNumber);
            this.exams.put(examName, e);
            return 200;
        }
        return 404;
    }

    public Exam getSpecificExamforStudent(String studentNumber,String examName){
        Exam e = null;
        if (this.exams.containsKey(examName)){
            e = this.exams.get(examName);
            List<String> enrolled = e.getEnrolled();
            if (enrolled.contains(studentNumber)){
                return e;
            }
        }
        return e;
    }

    public Exam getExam(UUID examID){
        Exam e = ((ExamDAO)this.exams).getExamByID(examID);
        return e;
    }

    public int saveExam(String examName,String studentNumber,ExamAnswer examAnswer){
        if (this.exams.containsKey(examName)){
            Exam e = this.exams.get(examName);
            List <String> enrolled = e.getEnrolled();
            if (enrolled.contains(studentNumber)){
                UUID idAnswer = UUID.randomUUID();
                e.getAnswers().put(idAnswer, examAnswer);
                return 200;
            }
            else{
                return 404;
            }
        }
        return 404;
    }
}
