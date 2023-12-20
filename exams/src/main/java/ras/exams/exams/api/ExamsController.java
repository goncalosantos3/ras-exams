package ras.exams.exams.api;

import ras.exams.exams.model.Exam;
import ras.exams.exams.service.ExamsService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ExamsController {
    private final ExamsService examService;

    public ExamsController(ExamsService examService){
        this.examService = examService;
    }

    @GetMapping("teste")
    public String teste(){
        return "piroca";
    }

    // Creates a new exam - examName
    @PostMapping("exam")
    public void createExam(@RequestBody Exam exam){
        examService.addExam(exam);
    }
}
