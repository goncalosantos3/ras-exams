package ras.exams.exams.dao;
import ras.exams.exams.model.Exam;
import java.util.*;

public class ExamDataAccessService implements ExamDao {

    private static List <Exam> DB = new ArrayList <>();

    @Override
    public int insertExam(UUID id, Exam exam){
        DB.add(new Exam(id,exam.getHeader()));
        return 1;
    }
}