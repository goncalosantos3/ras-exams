package ras.exams.exams.api;

import ras.exams.exams.model.Exam;
import ras.exams.exams.model.ExamHeader;
import ras.exams.exams.service.ExamsService;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ExamsController {
    private final ExamsService examService;

    public ExamsController(ExamsService examService){
        this.examService = examService;
    }

    // Rota - POST /exam?examName=xxxx
    @PostMapping("exam")
    public int createExam(@RequestParam("examName") String examName){
        // Função que cria o exame com base no examName
        //examService.createExam(examName);
        return 200;
    }

    // Rota - GET /exam/getClassrooms?examName=xxxx
    @GetMapping("exam/getClassrooms")
    public int getClassroomsForExam (@RequestParam("examName") String examName) {
        // Tem de devolver o List <Booking>, por isso mudar na declaração da função
        // Função que nos fornece as salas disponíveis para a realização do exame 
        // examService.getClassroomsForExam(examName);
        return 200;
    }

    // Rota - POST /exams/header
    @PostMapping("exams/header")
    public int createExamHeader(@RequestBody ExamHeader examHeader) {
        // Tem de devolver um inteiro, indicando se o examHeader foi adicionado ou não
        // examService.createExamHeader(examHeader);
        return 200;
    }
    
    // Rota - POST /exam/saveExam/{numberStudent}/{examName}
    @PostMapping("exam/saveExam")
    public int saveExam(@PathVariable String examName,@PathVariable String numberStudent,@RequestBody Exam exam) {
        // Tem de devolver um inteiro, indicando se o exame foi guardado com sucesso ou não
        // examService.saveExam(examName,numberStudent,exam);
        return 200;
    }
    
    // Rota - POST /exams/QuestionMultipleChoice?examName=xxxx

    
}
