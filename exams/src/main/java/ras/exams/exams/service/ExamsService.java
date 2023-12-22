package ras.exams.exams.service;

import ras.exams.exams.model.Exam;
import ras.exams.exams.model.ExamHeader;
import ras.exams.exams.model.MultipleChoice;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Service;


@Service
public class ExamsService {
    private Map<UUID, Exam> exams;
    private Map<String, UUID> namesIds; // Maps exam's names to ids

    public ExamsService(){
        this.exams = new HashMap<>();
        this.namesIds = new HashMap<>();
    }
    
    // Creates a new Exam with only it's name
    public void createExam(String examName){
        UUID examId = UUID.randomUUID();
        this.exams.put(examId, new Exam(examId, examName));
        this.namesIds.put(examName, examId);
        System.out.println("\n" + exams.get(examId));
    }

    public void addExamHeader(ExamHeader header){
        UUID examId = this.namesIds.get(header.getExamName());
        this.exams.get(examId).setHeader(header);
        System.out.println("\n" + exams.get(examId));
        this.exams.put(examId, this.exams.get(examId));
    }

    public void addMultipleChoiceQuestion(String examName, MultipleChoice mc){
        Exam e = this.exams.get(this.namesIds.get(examName));
        e.addMultipleChoiceQuestion(mc.getVersionID(), mc);
        this.exams.put(e.getID(), e);
    }
}
