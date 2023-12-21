package ras.exams.exams.data;
import ras.exams.exams.model.Exam;
import java.util.UUID;

import java.sql.*;

public interface ExamDao {
    int insertExam (UUID id, Exam exam);

    default int addExam(Exam exam){
        UUID id = UUID.randomUUID();
        return insertExam(id,exam);
    }
}