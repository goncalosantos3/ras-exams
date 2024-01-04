package ras.exams.exams.model;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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

            List<AbstractMap.SimpleEntry<List<String>,Integer>> listQ = new ArrayList<>();
            List<String> listA = new ArrayList<>();

            String q = ((CompleteSpaces)this.question).getText();
            String a = this.text;

            int from = 0,openIndex, closeIndex;
            while (true)
            {
                openIndex = q.indexOf('{', from);
                if (openIndex == -1)
                    break;

                closeIndex = q.indexOf('}', openIndex);
                
                if (closeIndex == -1)
                    break;

                from = closeIndex+1;
                String sub = q.substring(openIndex+1, closeIndex).trim();

                String[] parts = sub.split("]\\s*,\\s*");
                int val = Integer.parseInt(parts[1].trim());

                List<String> vals = new ArrayList<>();
                for (String s : parts[0].substring(1).split(","))
                {
                    vals.add(s.trim());
                }
                listQ.add(new AbstractMap.SimpleEntry<>(vals, val));
            } 

            from=0;
            while (true)
            {
                openIndex = a.indexOf('{', from);
                if (openIndex == -1)
                    break;

                closeIndex = a.indexOf('}', openIndex);
                
                if (closeIndex == -1)
                    break;

                from = closeIndex+1;
                String sub = a.substring(openIndex+1, closeIndex).trim();
                listA.add(sub);
            } 

            if (listA.size() != listQ.size())
                return 0;

            int grade = 0;
            for (int i=0 ; i<listA.size() ; i++)
            {
                String ans = listA.get(i);
                AbstractMap.SimpleEntry<List<String>,Integer> ques = listQ.get(i);
                if (ques.getKey().contains(ans))
                    grade += ques.getValue();
            }

            this.setGrade(grade);
            return grade;
        }
        return 0; // 0 para não aumentar no ExamAnswer
    }
}
