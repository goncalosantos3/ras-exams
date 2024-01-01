package ras.exams.exams.service;

import ras.exams.exams.data.ExamDAO;
import ras.exams.exams.model.CompleteSpaces;
import ras.exams.exams.model.CompleteSpacesAnswer;
import ras.exams.exams.model.Exam;
import ras.exams.exams.model.ExamAnswer;
import ras.exams.exams.model.ExamHeader;
import ras.exams.exams.model.MultipleChoiceAnswer;
import ras.exams.exams.model.Question;
import ras.exams.exams.model.TrueOrFalse;
import ras.exams.exams.model.TrueOrFalseAnswer;
import ras.exams.exams.model.WritingAnswer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

@Service
public class ExamsService {
    private Map<UUID, Exam> exams;

    public ExamsService(){
        this.exams = ExamDAO.getInstance();
    }
    
    // Creates a new Exam with only it's name
    public UUID createExam(ExamHeader header){
        UUID examId = UUID.randomUUID();
        this.exams.put(examId, new Exam(examId, header));
        return examId;
    }

    public UUID addExamVersion(String examID){
        Exam exam = this.exams.get(UUID.fromString(examID));
        UUID examVersionID = exam.addExamVersion();
        this.exams.put(exam.getID(), exam);
        return examVersionID;
    }

    public int removeExamVersion(String examID, String versionID){
        Exam exam = this.exams.get(UUID.fromString(examID));
        int res = exam.removeExamVersion(versionID);
        this.exams.put(exam.getID(), exam);
        return res;
    }

    // Add a new question to an exam of every type
    public int addQuestion(String examID, Question q){
        UUID examUUID = UUID.fromString(examID);
        Exam e = this.exams.get(examUUID);
        int res = e.addQuestion(q.getVersionID(), q);
        this.exams.put(examUUID, e);
        return res;
    }

    //// Return a question of any type
    public Question getQuestion(String examID, String versionID, String questionNumber){
        Exam e = this.exams.get(UUID.fromString(examID));
        return e.getQuestion(versionID, Integer.parseInt(questionNumber));
    }   

    public int updateQuestion(String examID, Question q){
        UUID examUUID = UUID.fromString(examID);
        Exam e = this.exams.get(examUUID);
        int res = e.updateQuestion(q.getVersionID(), q);
        this.exams.put(examUUID, e);
        return res;
    }   

    public List<Exam> getExams(){
        return this.exams.values().stream().collect(Collectors.toList());
    }

    //public ExamHeader getExamHeader(String examName){
    //    ExamHeader eh = null;
    //    
    //    if(this.exams.containsKey(examName)){
    //        eh = this.exams.get(examName).getHeader();   
    //    }
    //    
    //    return eh;
    //}
    //
    //public int editExamHeader(String examName, ExamHeader examHeader) {
    //    Exam e = null;
//
    //    if (this.exams.containsKey(examName)){
    //        e = this.exams.get(examName);
    //        e.setHeader(examHeader);
    //        this.exams.put(examName, e);
    //        return 200;
    //    }
    //    else {
    //        return 404;
    //    }
    //}
//
    //public List <Exam> getExamsbyStudent(String studentNumber){
    //    List <Exam> res = new ArrayList<>();
//
    //    for (Map.Entry<String, Exam> entry : this.exams.entrySet()) {
    //        Exam e = entry.getValue();
    //        List<String> enrolled = e.getEnrolled();
    //        if (enrolled.contains(studentNumber)){
    //            res.add(e);
    //        }
    //    }
    //    return res;
    //}
//
    //public int enrollStudents(String examName, List<String> students){
    //    Exam e = null;
    //    if (this.exams.containsKey(examName)){
    //        e = this.exams.get(examName);
    //        e.enrollStudents(students);
    //        this.exams.put(examName, e);
    //        return 200;
    //    }
    //    return 404;
    //}
//
    //public Exam getSpecificExamforStudent(String studentNumber,String examName){
    //    Exam e = null;
    //    if (this.exams.containsKey(examName)){
    //        e = this.exams.get(examName);
    //        List<String> enrolled = e.getEnrolled();
    //        if (enrolled.contains(studentNumber)){
    //            return e;
    //        }
    //    }
    //    return e;
    //}
//
    //public Exam getExam(UUID examID){
    //    Exam e = ((ExamDAO)this.exams).getExamByID(examID);
    //    return e;
    //}
//
    //public int createExamAnswer(String examName, ExamAnswer ea){
    //    if(this.exams.containsKey(examName)){
    //        Exam e = this.exams.get(examName);
    //        List <String> enrolled = e.getEnrolled();
    //        if(enrolled.contains(ea.getStudentID().toString())){
    //            ea.setExamID(e.getID());
    //            e.getAnswers().put(ea.getStudentID().toString(), ea);
    //            this.exams.put(examName, e);
    //            return 200;
    //        }
    //        else{
    //            return 404;
    //        }
    //    }
    //    return 404;
    //}
//
    //public int saveCompleteSpacesAnswer(String examName, int vn, int qn, String studentID, CompleteSpacesAnswer csa){
    //    if(this.exams.containsKey(examName)){
    //        Exam e = this.exams.get(examName);
    //        int res = e.saveCompleteSpacesAnswer(studentID, vn, qn, csa);
    //        this.exams.put(examName, e);
    //        return res;
    //    }
    //    return 404;
    //}
//
    //public int saveWritingAnswer(String examName, int vn, int qn, String studentID, WritingAnswer wa){
    //    if(this.exams.containsKey(examName)){
    //        Exam e = this.exams.get(examName);
    //        int res = e.saveWritingAnswer(studentID, vn, qn, wa);
    //        this.exams.put(examName, e);
    //        return res;
    //    }
    //    return 404;
    //}
//
    //public int saveTrueOrFalseAnswer(String examName, int vn, int qn, String studentID, TrueOrFalseAnswer tfa){
    //    if(this.exams.containsKey(examName)){
    //        Exam e = this.exams.get(examName);
    //        int res = e.saveTrueOrFalseAnswer(studentID, vn, qn, tfa);
    //        this.exams.put(examName, e);
    //        return res;
    //    }
    //    return 404;
    //}
//
    //public int saveMultipleChoiceAnswer(String examName, int vn, int qn, String studentID, MultipleChoiceAnswer mca){
    //    if(this.exams.containsKey(examName)){
    //        Exam e = this.exams.get(examName);
    //        int res = e.saveMultipleChoiceAnswer(studentID, vn, qn, mca);
    //        this.exams.put(examName, e);
    //        return res;
    //    }
    //    return 404;
    //}
}
