package ras.exams.exams.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ExamVersion {
    private final UUID versionId;
    private List<Question> questions;

    public ExamVersion(@JsonProperty("id") UUID id){
        this.versionId = id;
        this.questions = new ArrayList<>();
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

    public UUID getVersionId(){
        return this.versionId;
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
