package ras.exams.exams.model;

public class CompleteSpacesAnswer extends Answer{
    private String text;
    private Question question;

    public CompleteSpacesAnswer(@JsonProperty("grade") int grade,@JsonProperty("answer") String answer,@JsonProperty("q") Question q){
        super(grade);
        this.text = answer;
        this.question = q;
    }

    public String getText(){
        return this.text;
    }

    public Question getQuestion(){
        return this.question;
    }
}
