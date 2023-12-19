package ras.exams.exams.model;

import java.util.List;

public class MultipleChoiceAnswer extends Answer{
    private List<ChoiceAnswer> answers;

    public MultipleChoiceAnswer(int grade, List<ChoiceAnswer> answers){
        super(grade);
        this.answers = answers;
    }

    public List<ChoiceAnswer> getAnswers(){
        return this.answers;
    }
}
