package ras.exams.exams.model;

import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CompleteSpaces extends Question{ 
    private String text;  
    
    // Chamado pelas rotas do controller
    public CompleteSpaces(@JsonProperty("question") String question, @JsonProperty("qn") int qn,
         @JsonProperty("versionID") String versionID, @JsonProperty("text") String text){
        super(UUID.randomUUID(), question, qn, 0, 'C', UUID.fromString(versionID));
        this.text = text;
        this.setScore(this.getScore());
    }

    // Chamado pela BD
    public CompleteSpaces(UUID id, String question, int qn, int score, UUID versionID, String text){
        super(id, question, qn, score, 'C', versionID);
        this.text = text;
    }

    public int getScore()
    {
        if (this.score != -1)
            return this.score;
        int score = 0;
        Pattern pattern = Pattern.compile("\\{\\[[^]]\\],\\s*\\d+}");
        Matcher matcher = pattern.matcher(this.text);

        while(matcher.find())
        {
            String q = matcher.group();
            q = q.substring(q.indexOf(',')+1, q.length()-1).strip();
            score += Integer.parseInt(q);
        }
        this.setScore(score);
        return score;
    }

    public String getText(){
        return this.text;
    }
}
