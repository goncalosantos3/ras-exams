package ras.exams.exams.model;

import java.util.List;
import java.util.UUID;

public class MultipleChoice extends Question{
    private List<Choice> choices;

    public MultipleChoice(@JsonProperty("id") UUID id, @JsonProperty("question") String question,@JsonProperty("qn") int qn,@JsonProperty("choices") List<Choice> choices){
        super(id, question, qn);
        this.choices = choices;
    }

    public List<Choice> getChoices(){
        return this.choices;
    }
}
