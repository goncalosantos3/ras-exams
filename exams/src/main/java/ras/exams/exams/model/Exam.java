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
    private Map<String, ExamAnswer> answers;

    public Exam(UUID id, String examName){
        this.id = id;
        this.enrolled = new ArrayList<>();
        this.header = new ExamHeader(examName, id);
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
                Map<Integer, ExamVersion> versions, Map<String, ExamAnswer> answers) {
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
        q.setVersionID(this.versions.get(versionNumber).getVersionId());
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

    // Adiciona os estudantes se eles ainda não foram anteriormente adicionados
    public void enrollStudents(List<String> students){
        for(String s: students){
            if(!this.enrolled.contains(s)){
                this.enrolled.add(s);
            }
        }
    }

    public int saveCompleteSpacesAnswer(String studentID, int vn, int qn, CompleteSpacesAnswer csa){
        System.out.println(this.answers.keySet());
        if(this.enrolled.contains(studentID) && this.answers.containsKey(UUID.fromString(studentID))){
            ExamAnswer ea = this.answers.get(UUID.fromString(studentID));
            csa.setExamAnswerID(ea.getExamAnswerId());
            Question q = this.versions.get(vn).getQuestion(qn);
            csa.setQuestion(q);
            csa.setQuestionID(q.getQuestionId());
            ea.addAnswer(csa);
            return 200;
        }   
        return 404;
    }   

    public int saveWritingAnswer(String studentID, int vn, int qn, WritingAnswer wa){
        if(this.enrolled.contains(studentID) && this.answers.containsKey(UUID.fromString(studentID))){
            ExamAnswer ea = this.answers.get(UUID.fromString(studentID));
            wa.setExamAnswerID(ea.getExamAnswerId());
            Question q = this.versions.get(vn).getQuestion(qn);
            wa.setQuestion(q);
            wa.setQuestionID(q.getQuestionId());
            ea.addAnswer(wa);
            return 200;
        }   
        return 404;
    }

    public int saveTrueOrFalseAnswer(String studentID, int vn, int qn, TrueOrFalseAnswer tfa){
        if(this.enrolled.contains(studentID) && this.answers.containsKey(UUID.fromString(studentID))){
            ExamAnswer ea = this.answers.get(UUID.fromString(studentID));
            tfa.setExamAnswerID(ea.getExamAnswerId());
            Question q = this.versions.get(vn).getQuestion(qn);
            tfa.setQuestionID(q.getQuestionId());
            TrueOrFalse tof = (TrueOrFalse) q;
            for(TOFQAnswer tofqanswer: tfa.getAnswers()){
                tofqanswer.setOption(tof.getQuestionOnOption(tofqanswer.getOption().getOptionNumber()));
            }
            ea.addAnswer(tfa);
        }   
        return 404;
    }

    public int saveMultipleChoiceAnswer(String studentID, int vn, int qn, MultipleChoiceAnswer mca){
        if(this.enrolled.contains(studentID) && this.answers.containsKey(UUID.fromString(studentID))){
            ExamAnswer ea = this.answers.get(UUID.fromString(studentID));
            mca.setExamAnswerID(ea.getExamAnswerId());
            Question q = this.versions.get(vn).getQuestion(qn);
            mca.setQuestionID(q.getQuestionId());
            MultipleChoice mc = (MultipleChoice) q;
            for(ChoiceAnswer ca: mca.getAnswers()){
                ca.setChoice(mc.getChoiceOnChoiceNumber(ca.getChoice().getChoiceNumber()));
            }
            ea.addAnswer(mca);
        }   
        return 404;
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

    public Map<String,ExamAnswer> getAnswers(){
        return this.answers;
    }

    public String toString(){
        String str = "Exam-\nId: " + this.id + "\nName: " + this.header.getExamName();
        str += "\nExamUC: " + this.header.getExamUC() + "\nAdmissionTime: " + this.header.getExamAdmissionTime();
        return str;
    }

    public void setEnrolled(String numberStudent){
        this.enrolled.add(numberStudent);
    }
}
