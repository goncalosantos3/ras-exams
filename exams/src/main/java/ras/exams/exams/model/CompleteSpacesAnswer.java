package ras.exams.exams.model;

import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CompleteSpacesAnswer extends Answer{
    private String text;
    private Question question;

    // Construtor usado pelas rotas do controller
    public CompleteSpacesAnswer(@JsonProperty("answer") String answer){
        super(UUID.randomUUID(), 0, 'C');
        this.text = answer;
    }

    // Construtor usado pela base de dados
    public CompleteSpacesAnswer(UUID answerID, int grade, UUID examAnswerID, String text, Question q){
        super(answerID, grade, examAnswerID, 'C', q.getQuestionId());
        this.text = text;
        this.question = q;
    }

    public void setQuestion(Question q){
        this.question = q;
    }

    public String getText(){
        return this.text;
    }

    public Question getQuestion(){
        return this.question;
    }

    public int autoCorrect(){
        if(this.getGrade() == 0){
            int r = 0;
            Pattern pattern = Pattern.compile("\\{\\w+,\\s*\\d+}");
            Matcher matcher = pattern.matcher(this.text);
            
            while(matcher.find())
            {
                String q = matcher.group();
                q = q.substring(q.indexOf(',')+1, q.length()-1).strip();
                r += Integer.parseInt(q);
            }
            System.out.println("Cotação CSA: " + r);
            this.setGrade(r);
            return r;
        }
        return 0; // 0 para não aumentar no ExamAnswer
    }
}
