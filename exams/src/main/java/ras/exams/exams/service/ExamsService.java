package ras.exams.exams.service;

import ras.exams.exams.data.ExamDao;
import ras.exams.exams.model.Exam;


import org.springframework.stereotype.Service;

@Service
public class ExamsService {
    // Referência para o DAO de exames
    private ExamDao examDao;

    public void ExamService(ExamDao examDao){
        this.examDao = examDao;
    }

    public int addExam(Exam exam){
        return examDao.addExam(exam);
    }
}
