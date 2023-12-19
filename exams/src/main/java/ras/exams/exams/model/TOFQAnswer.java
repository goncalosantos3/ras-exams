package ras.exams.exams.model;

public class TOFQAnswer {
    private int grade;
    private boolean answer;
    private Question question;

    public TOFQAnswer(int grade, boolean answer, Question q){   
        this.grade = grade;
        this.answer = answer;
        this.question = q;
    }

    public int getGrade(){
        return this.grade;
    }

    public boolean getAnswer(){
        return this.answer;
    }

    public Question getQuestion(){
        return this.question;
    }
}
