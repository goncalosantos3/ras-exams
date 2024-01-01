package ras.exams.exams.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

public class ExamVersion {
    private final UUID versionId, examID;
    private int versionNumber = 0;
    private List<Question> questions;
    
    // Usado nas rotas do controller
    public ExamVersion(UUID id, UUID examID){
        this.versionId = id;
        this.examID = examID;
        this.versionNumber = this.versionNumber++;
        this.questions = new ArrayList<>();
    }
    
    // Usado na BD
    public ExamVersion(UUID id, UUID examID, int versionNumber){
        this.versionId = id;
        this.examID = examID;
        this.versionNumber = versionNumber;
        this.questions = new ArrayList<>();
    }

    public void addQuestions(List<Question> questions)
    {
        // Sort the questions based on questionNumber
        Collections.sort(questions, new Comparator<Question>(){
            public int compare(Question q1, Question q2){
                return q1.getQuestionNumber() - q2.getQuestionNumber();
            }
        });

        this.questions.addAll(questions);
    }

    // Add a question in the ArrayList in a ordered fashion
    public int addQuestion(Question q){
        q.setVersionID(this.versionId);
        int pos = 0;
        
        for(Question ql: this.questions){
            if(q.getQuestionNumber() < ql.getQuestionNumber()){
                break;
            }else if(q.getQuestionNumber() == ql.getQuestionNumber()){
                // Question number is taken (ERROR!)
                return 404;
            }
            pos += 1;
        }

        this.questions.add(pos, q);
        return 200;
    }

    public Question getQuestion(int questionNumber){
        Question q = null;
        
        for(Question ql: this.questions){
            if(ql.getQuestionNumber() == questionNumber){
                q = ql;
            }
        }

        return q;
    }
    
    public int updateQuestion(Question q){
        int pos = 0;
        for(Question ql: this.questions){
            if(ql.getQuestionNumber() == q.getQuestionNumber()){
                break;
            }
            pos += 1;
        }

        if(pos == this.questions.size()){
            return 404;
        }

        this.questions.remove(pos);
        this.questions.add(pos, q);
        return 200;
    }

    public int deleteQuestion(int questionNumber){
        int pos = 0;
        for(Question q : this.questions){
            if(q.getQuestionNumber() == questionNumber){
                break;
            }
            pos++;
        }
        
        if(pos == this.questions.size()){
            return 404;
        }
        this.questions.remove(pos);
        return 200;
    }

    public UUID getVersionId(){
        return this.versionId;
    }
    
    public UUID getExamID() {
        return examID;
    }
    
    public int getVersionNumber() {
        return versionNumber;
    }
    
    public List<Question> getQuestions(){
        return this.questions;
    }

    public String toString(){
        String str = "Exam version: " + this.versionId.toString() + "\n";
        for(Question q : this.questions){
            str += q.toString();
        } 
        return str;
    }
}
