package ras.exams.exams.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ExamVersion {
    private final UUID versionId, examID;
    private int versionNumber;
    private List<Question> questions;
    
    public ExamVersion(@JsonProperty("id") UUID id, @JsonProperty("examID") UUID examID, 
        @JsonProperty("versionNumber") int versionNumber){
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
    public void addQuestion(Question q){
        int pos = 0;
        for(Question ql: this.questions){
            if(q.getQuestionNumber() <= ql.getQuestionNumber()){
                break;
            }
            pos += 1;
        }
        this.questions.add(pos, q);
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
