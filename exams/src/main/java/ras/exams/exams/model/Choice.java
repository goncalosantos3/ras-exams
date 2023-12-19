package ras.exams.exams.model;

public class Choice {
    private String description;
    private boolean correction;
    private int score;
    private int choiceNumber;

    public Choice(String desc, boolean correc, int score, int choiceNumber){
        this.description = desc;
        this.correction = correc;
        this.score = score;
        this.choiceNumber = choiceNumber;
    }
}
