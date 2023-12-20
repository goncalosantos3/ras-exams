package ras.exams.exams.model;

public class TOFQAnswer {
    private int grade;
    private boolean answer;
    private Question question;

    public TOFQAnswer(@JsonProperty("grade")int grade,@JsonProperty("answer") boolean answer,@JsonProperty("q") Question q){   
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
