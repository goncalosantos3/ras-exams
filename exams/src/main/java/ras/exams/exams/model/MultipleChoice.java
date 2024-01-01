package ras.exams.exams.model;

import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MultipleChoice extends Question{
    private List<Choice> choices;

    // Chamado pelas rotas do controller
    public MultipleChoice(@JsonProperty("question") String question, @JsonProperty("qn") int qn, 
        @JsonProperty("versionID") String versionID, @JsonProperty("choices") List<Choice> choices){
        super(UUID.randomUUID(), question, qn, 'M', UUID.fromString(versionID));
        this.choices = choices;
    }

    // Chamado pela BD
    public MultipleChoice(UUID id, String question, int qn, String versionID, List<Choice> choices){
        super(id, question, qn, 'M', UUID.fromString(versionID));
        this.choices = choices;
    }

    public List<Choice> getChoices(){
        return this.choices;
    }

    public Choice getChoiceOnChoiceNumber(int cn){
        for(Choice c: this.choices){
            if(c.getChoiceNumber() == cn){
                return c;
            }
        }
        return null;
    }
}
