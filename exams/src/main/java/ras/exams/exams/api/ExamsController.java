package ras.exams.exams.api;

import ras.exams.exams.model.CompleteSpaces;
import ras.exams.exams.model.ExamAnswer;
import ras.exams.exams.model.ExamHeader;
import ras.exams.exams.model.MultipleChoice;
import ras.exams.exams.model.TOFQ;
import ras.exams.exams.model.TrueOrFalse;
import ras.exams.exams.model.Writing;
import ras.exams.exams.service.ExamsService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PutMapping;


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
        examService.createExam(examName);
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
    public int addExamHeader(@RequestBody ExamHeader examHeader) {
        // Tem de devolver um inteiro, indicando se o examHeader foi adicionado ou não
        examService.addExamHeader(examHeader);
        return 200;
    }
    
    // Rota - POST /exam/saveExam/{studentNumber}/{examName}
    @PostMapping("exam/saveExam")
    public int saveExam(@PathVariable String studentNumber, @PathVariable String examName, @RequestBody ExamAnswer examAnswer) {
        // Tem de devolver um inteiro, indicando se o exame foi guardado com sucesso ou não
        // examService.saveExam(examName, studentNumber, examAnswer);
        return 200;
    }
    
    // Rota - POST /exams/QuestionMultipleChoice?examName=xxxx
    @PostMapping("exams/QuestionMultipleChoice")
    public int addMultipleChoiceQuestion(@RequestParam("examName") String examName, @RequestBody MultipleChoice multipleChoice) {
        // Tem de devolver um inteiro, indicando se a questão de escolha múltipla foi adicionada ou não
        examService.addQuestion(examName, multipleChoice);
        return 200;
    }
    
    // Rota - GET /exams/QuestionMultipleChoice/{examName}/{versionNumber}/{questionNumber}
    @GetMapping("exams/QuestionMultipleChoice")
    public void getQuestionMultipleChoice(@PathVariable String examName,@PathVariable String versionNumber,@PathVariable String questionNumber) {
        // Tem de devolver uma questão de escolha múltipla - MultipleChoice 
        return examService.getQuestion(examName, versionNumber, questionNumber);
    }
    
    // Rota - PUT /exams/QuestionMultipleChoice/{examName}/{versionNumber}/{questionNumber}
    @PutMapping("exams/QuestionMultipleCoice")
    public void updateQuestionMultipleChoice(@PathVariable String examName,@PathVariable String versionNumber,@PathVariable String questionNumber,@RequestBody MultipleChoice multipleChoice) {
        // Tem de devolver uma questão de escolha múltipla - MultipleChoice
        // return examService.updateQuestionMultipleChoice(examName,versionNumber,questionNumber,multipleChoice)
    }

    // Rota - POST /exams/QuestionTrueorFalse?examName=xxxx
    @PostMapping("exams/QuestionTrueorFalse")
    public int addTrueorFalseQuestion(@RequestParam("examName") String examName,@RequestBody TrueOrFalse trueOrFalseQuestion) {
        // Tem de devolver um inteiro, indicando se a questão V/F foi adicionada ou não
        examService.addQuestion(examName, trueOrFalseQuestion);
        return 200;
    }

    // Rota - GET /exams/QuestionTrueorFalse/{examName}/{versionNumber}/{questionNumber}
    @GetMapping("exams/QuestionTrueorFalse")
    public void getQuestionTrueorFalse(@PathVariable String examName, @PathVariable String versionNumber, @PathVariable String questionNumber){
        // Tem de devolver a question True or False - TOFQ
        // return examService.getQuestionTrueorFalse(examName,versionNumber,questionNumber)
    }

    // Rota - PUT /exams/QuestionTrueorFalse/{examName}/{versionNumber}/{questionNumber}
    @PutMapping("exams/QuestionTrueorFalse")
    public void updateQuestionTrueorFalse(@PathVariable String examName, @PathVariable String versionNumber, @PathVariable String questionNumber, @RequestBody TOFQ trueOrFalseQuestion){
        // Tem de devolver a question True or False atualizada - TOFQ
        // return examService.updateQuestionTrueorFalse(examName,versionNumber,questionNumber,trueOrFalseQuestion)
    }

    // Rota - POST /exams/QuestionWriting?examName=xxx
    @PostMapping("exams/QuestionWriting")
    public int addWritingQuestion(@RequestParam("examName") String examName, @RequestBody Writing writingQuestion){
        // return examService.createWritingQuestion(examName,writingQuestion)
        // Tem de dar return a um inteiro dizendo se a questão foi criada com sucesso ou não
        examService.addQuestion(examName, writingQuestion);
        return 200;
    }

    // Rota - GET /exams/QuestionWriting/{examName}/{versionNumber}/{questionNumber}
    @GetMapping("exams/QuestionWriting")
    public void getWritingQuestion(@PathVariable String examName, @PathVariable String versionNumber, @PathVariable String questionNumber){
        // Tem de dar return a questão de escrita pedida - Writing
        // return examService.getWritingQuestion(examName,versionNumber,questionNumber)
    }

    // Rota - PUT /exams/QuestionWriting/{examName}/{versionNumber}/{questionNumber}
    @PutMapping("exams/QuestionWriting")
    public void updateWritingQuestion(@PathVariable String examName, @PathVariable String versionNumber, @PathVariable String questionNumber, @RequestBody Writing writingQuestion){
        // Tem de dar return a questão de escrita corretamente atualizada - Writing
        // return examService.updateWritingQuestion(examName,versionNumber,questionNumber,writingQuestion)
    }

    // Rota - POST /exams/QuestionCompleteSpaces?examName=xxx
    @PostMapping("exams/QuestionCompleteSpaces")
    public int addCompleteSpacesQuestion(@RequestParam("examName") String examName,@RequestBody CompleteSpaces completeSpacesQuestion){
        // return examService.createCompleteSpacesQuestion(examName,completeSpacesQuestion)
        examService.addQuestion(examName, completeSpacesQuestion);
        return 200;
    }

    // Rota - GET /exams/QuestionCompleteSpaces/{examName}/{versionNumber}/{questionNumber}
    @GetMapping("exams/QuestionCompleteSpaces")
    public void getCompleteSpacesQuestion(@PathVariable String examName, @PathVariable String versionNumber, @PathVariable String questionNumber){

    }

    // Rota - PUT /exams/QuestionCompleteSpaces/{examName}/{versionNumber}/{questionNumber}
    @PutMapping("exams/QuestionCompleteSpaces")
    public void updateCompleteSpacesQuestion(@PathVariable String examName, @PathVariable String versionNumber, @PathVariable String questionNumber, @RequestBody CompleteSpaces completeSpacesQuestion){

    }

    // Rota - GET /exams/getCorrection/{examName}/{numberStudent}/{questionNumber}
    @GetMapping("exams/getCorrection")
    public void getQuestionCorrectionStudent(@PathVariable String examName, @PathVariable String studentNumber, @PathVariable String questionNumber){

    }

    // Rota - GET /getExams
    @GetMapping("getExams")
    public void getExams(){

    }

    // Rota - 
    
    

    
}
