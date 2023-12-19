package ras.exams.exams.model;

public class Choice {
    private String description;
    private boolean correction;
    private int score;
    private int choiceNumber;

    public Choice(@JsonProperty("desc") String desc,@JsonProperty("correc") boolean correc,@JsonProperty("score") int score,@JsonProperty("choiceNumber") int choiceNumber){
        this.description = desc;
        this.correction = correc;
        this.score = score;
        this.choiceNumber = choiceNumber;
    }

    public String getDescription(){
        return this.description;
    }

    public boolean getCorrection(){
        return this.correction;
    }

    public int getScore(){
        return this.score;
    }

    public int getChoiceNumber(){
        return this.choiceNumber;
    }
    
}
