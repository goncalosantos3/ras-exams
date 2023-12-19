package ras.exams.exams.model;

public abstract class Answer {
    private int grade;

    public Answer(@JsonProperty("grade") int grade){
        this.grade = grade; 
    }

    public int getGrade(){
        return this.grade;
    }
}
