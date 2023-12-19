package ras.exams.exams.model;

public class WritingAnswer extends Answer{
    private String text;
    private Question question;

    public WritingAnswer(int grade, String text, Question q){
        super(grade);
        this.text = text;
        this.question = q;
    }
}
