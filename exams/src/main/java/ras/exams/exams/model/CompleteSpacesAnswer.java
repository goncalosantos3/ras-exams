package ras.exams.exams.model;

public class CompleteSpacesAnswer extends Answer{
    private String text;
    private Question question;

    public CompleteSpacesAnswer(int grade, String answer, Question q){
        super(grade);
        this.text = answer;
        this.question = q;
    }
}
