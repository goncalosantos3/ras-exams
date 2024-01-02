package ras.exams.exams.model;

import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MultipleChoice extends Question{
    private List<Choice> choices;

    // Chamado pelas rotas do controller
    public MultipleChoice(@JsonProperty("question") String question, @JsonProperty("qn") int qn, 
        @JsonProperty("versionID") String versionID, @JsonProperty("choices") List<Choice> choices){
        super(UUID.randomUUID(), question, qn, 0, 'M', UUID.fromString(versionID));
        this.choices = choices;
        
        int score = 0;
        for(Choice c: this.choices){
            score += c.getScore();
        }
        this.setScore(score);
    }

    // Chamado pela BD
    public MultipleChoice(UUID id, String question, int qn, int score, String versionID, List<Choice> choices){
        super(id, question, qn, score, 'M', UUID.fromString(versionID));
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
