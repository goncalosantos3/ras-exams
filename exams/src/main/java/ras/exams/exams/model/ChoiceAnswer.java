package ras.exams.exams.model;

public class ChoiceAnswer {
    private boolean selected;
    private int grade;
    private Question question;

    public ChoiceAnswer(boolean selected, int grade, Question q){
        this.selected = selected;
        this.grade = grade;
        this.question = q;
    }
}
