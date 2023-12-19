package ras.exams.exams.model;

import java.util.List;
import java.util.UUID;

public class MultipleChoice extends Question{
    private List<Choice> choices;

    public MultipleChoice(UUID id, String question, int qn, List<Choice> choices){
        super(id, question, qn);
        this.choices = choices;
    }
}
