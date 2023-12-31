package ras.exams.exams.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ExamAnswer {
    private UUID examAnswerId, examID, studentID;
    private int grade;  
    private List<Answer> answers;
    
    // Construtor usado pelas rotas do controller
    // Quando o aluno regista as repostas de um exame ainda não se sabe a classificação que ele teve no mesmo
    public ExamAnswer(String sid, UUID examID){
        this.examAnswerId = UUID.randomUUID();
        this.studentID = UUID.fromString(sid);
        this.examID = examID;
        this.grade = 0;
        this.answers = new ArrayList<>();
    }

    // Construtor usado pela BD
    public ExamAnswer(UUID id, UUID examID, UUID studentID, int grade, List<Answer> answers){
        this.examAnswerId = id; 
        this.examID = examID;
        this.studentID = studentID;
        this.grade = grade;
        this.answers = answers;
    }

    // Replaces an answer with the same questionID (only one answer per question)
    public void addAnswer(Answer answer){
        int pos = 0;
        boolean remove = false;
        
        for(Answer a: this.answers){
            if(a.getQuestionID() == answer.getQuestionID()){
                remove = true;
                break;
            }
            pos++;
        }
        if(remove) this.answers.remove(pos);

        this.answers.add(pos, answer);
    }

    public void setExamID(UUID examID){
        this.examID = examID;
    }

    public UUID getExamAnswerId(){
        return this.examAnswerId;
    }

    public int getGrade(){
        return this.grade;
    }

    public void calculateGrade() {
        this.grade = 0;
        
        for (Answer a : this.answers)
        {
            this.grade += a.autoCorrect();
        }
    }

    public List<Answer> getAnswers(){
        return this.answers;
    }

    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }

    public UUID getStudentID() {
        return studentID;
    }

    public UUID getExamID() {
        return examID;
    }

}
