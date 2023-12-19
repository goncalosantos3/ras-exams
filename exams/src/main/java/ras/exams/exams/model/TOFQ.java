package ras.exams.exams.model;

public class TOFQ {
    private String description;
    private boolean correction;
    private int score;
    private int optionNumber;

    public TOFQ(String desc, boolean correc, int score, int optionNumber){
        this.description = desc;
        this.correction = correc;
        this.score = score;
        this.optionNumber = optionNumber;
    }
}
