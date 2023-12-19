package ras.exams.exams.model;

import java.util.List;
import java.util.UUID;

public class TrueOrFalse extends Question{
    List<TOFQ> questions;

    public TrueOrFalse(UUID id, String question, int qn, List<TOFQ> questions){
        super(id, question, qn);
        this.questions = questions;
    }
}
