package ras.exams.exams.model;

import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CompleteSpacesAnswer extends Answer{
    private String text;
    private Question question;

    public CompleteSpacesAnswer(@JsonProperty("grade") int grade, @JsonProperty("examAnswerID") UUID examAnswerID,@JsonProperty("answer") String answer,@JsonProperty("q") Question q){
        super(grade, examAnswerID, 'C', q.getQuestionId());
        this.text = answer;
        this.question = q;
    }

    public CompleteSpacesAnswer(UUID answerID, int grade, UUID examAnswerID, String text, Question q){
        super(answerID, grade, examAnswerID, 'C', q.getQuestionId());
        this.text = text;
        this.question = q;
    }

    public String getText(){
        return this.text;
    }

    public Question getQuestion(){
        return this.question;
    }

    @Override
    public int getGrade()
    {
        int r = 0;
        Pattern pattern = Pattern.compile("\\{\\w+,\\s*\\d+}");
        Matcher matcher = pattern.matcher(this.text);

        while(matcher.find())
        {
            String q = matcher.group();
            q = q.substring(q.indexOf(',')+1, q.length()-1).strip();
            r += Integer.parseInt(q);
        }
        this.setGrade(r);
        return r;
    }
}
