package ras.exams.exams.model;

import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MultipleChoice extends Question{
    private List<Choice> choices;

    public MultipleChoice(@JsonProperty("id") UUID id, @JsonProperty("question") String question,
        @JsonProperty("qn") int qn, @JsonProperty("versionID") String versionID,
        @JsonProperty("choices") List<Choice> choices){
        super(id, question, qn, 'M', UUID.fromString(versionID));
        this.choices = choices;
    }
    
    public MultipleChoice(UUID questionID, String question, int questionNumber,
                            UUID versionID, List<Choice> choices){
        super(questionID, question, questionNumber, 'M', versionID);
        this.choices = choices;
    }

    public List<Choice> getChoices(){
        return this.choices;
    }
}
