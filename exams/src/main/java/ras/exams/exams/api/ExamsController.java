package ras.exams.exams.api;

import ras.exams.exams.model.CompleteSpaces;
import ras.exams.exams.model.CompleteSpacesAnswer;
import ras.exams.exams.model.Exam;
import ras.exams.exams.model.ExamHeader;
import ras.exams.exams.model.MultipleChoice;
import ras.exams.exams.model.MultipleChoiceAnswer;
import ras.exams.exams.model.Question;
import ras.exams.exams.model.TrueOrFalse;
import ras.exams.exams.model.TrueOrFalseAnswer;
import ras.exams.exams.model.Writing;
import ras.exams.exams.model.WritingAnswer;
import ras.exams.exams.service.ExamsService;

import java.util.List;
import java.util.UUID;

import org.springframework.web.bind.annotation.DeleteMapping;
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

    // Rota - POST /exam?teacherID=xxxx
    @PostMapping("exam")
    public UUID createExam(@RequestParam("teacherID") String teacherID, @RequestBody ExamHeader header){
        // Função que cria o exame com base no examName
        return examService.createExam(teacherID, header);
    }

    // Rota - GET /exam?examID=xxxx
    @GetMapping("exam")
    public Exam getExam(@RequestParam("examID") String examID) {
        // Dá return de um exame - Exam
        return examService.getExam(examID);
    }

    // Rota - GET /getExams
    @GetMapping("getExams")
    public List<Exam> getExams(){
        return examService.getExams();
    }

    // Rota - GET /getExamsByTeacher?teacherID=xxxx
    @GetMapping("getExamsByTeacher")
    public List<Exam> getExamsByTeacher(@RequestParam("teacherID") String teacherID) {
        return examService.getExamsByTeacher(teacherID);
    }

    // Rota - GET /getExamsByStudent?studentID=xxxx
    @GetMapping("getExamsByStudent")
    public List<Exam> getExambyStudent(@RequestParam("studentID") String studentID) {
        // Dá return a uma lista de exames na qual o aluno está inscrito - List<Exam>
        return examService.getExamsByStudent(studentID);
    }
    
    // Rota - DELETE /exam?examID=xxxx
    @DeleteMapping("exam")
    public int deleteExam(@RequestParam("examID") String examID){
        return examService.deleteExam(examID);
    }

    // Rota - GET /getExamHeader?examID=xxxx
    @GetMapping("getExamHeader")
    public ExamHeader getExamHeader(@RequestParam("examID") String examID) {
        // Dá return de um header de um exame - ExamHeader
        return examService.getExamHeader(examID);
    }
    
    // Rota - PUT /exam/editExamHeader?examID=xxxx
    @PutMapping("exam/editExamHeader")
    public int editExamHeader(@RequestParam("examID") String examID, @RequestBody ExamHeader examHeader) {
        // Dá return de um int se o header do exame foi alterado ou não 
        return examService.editExamHeader(examID,examHeader);
    }

    // Rota - POST /exam/versions?examID=xxxx
    @PostMapping("exam/versions")
    public UUID createExamVersion(@RequestParam("examID") String examID){
        return examService.addExamVersion(examID);
    }

    // Rota - GET /exam/getExamVersions?examID=xxxx
    @GetMapping("exam/getExamVersions")
    public List<String> getExamVersions(@RequestParam("examID") String examID){
        return examService.getExamVersions(examID);
    }

    // Rota - DELETE /exam/versions?examID=xxxx&versionID=xxxx
    @DeleteMapping("exam/versions")
    public int deleteExamVersion(@RequestParam("examID") String examID, @RequestParam("versionID") String versionID){
        return examService.removeExamVersion(examID, versionID);    
    }  
    
    // Rota - POST /exam/QuestionMultipleChoice?examID=xxxx
    @PostMapping("exam/QuestionMultipleChoice")
    public int addMultipleChoiceQuestion(@RequestParam("examID") String examID, @RequestBody MultipleChoice multipleChoice) {
        // Tem de devolver um inteiro, indicando se a questão de escolha múltipla foi adicionada ou não
        return examService.addQuestion(examID, multipleChoice);
    }

    // // Rota - GET /exam/QuestionMultipleChoice/{examID}/{versionID}/{questionNumber}
    @GetMapping("exam/QuestionMultipleChoice/{examID}/{versionID}/{questionNumber}")
    public Question getQuestionMultipleChoice(@PathVariable String examID,@PathVariable String versionID,@PathVariable String questionNumber) {
        // Tem de devolver uma questão de escolha múltipla - MultipleChoice 
        return examService.getQuestion(examID, versionID, questionNumber);
    }

    // Rota - PUT /exam/QuestionMultipleChoice/{examID}
    @PutMapping("exam/QuestionMultipleChoice/{examID}")
    public int updateQuestionMultipleChoice(@PathVariable String examID, @RequestBody MultipleChoice multipleChoice) {
        // Tem de devolver uma questão de escolha múltipla - MultipleChoice
        return examService.updateQuestion(examID, multipleChoice);
    }

    // Rota - POST /exam/QuestionTrueorFalse?examID=xxxx
    @PostMapping("exam/QuestionTrueorFalse")
    public int addTrueorFalseQuestion(@RequestParam("examID") String examID, @RequestBody TrueOrFalse trueOrFalseQuestion) {
        // Tem de devolver um inteiro, indicando se a questão V/F foi adicionada ou não
        return examService.addQuestion(examID, trueOrFalseQuestion);
    }

    // Rota - GET /exam/QuestionTrueorFalse/{examID}/{versionID}/{questionNumber}
    @GetMapping("exam/QuestionTrueorFalse/{examID}/{versionID}/{questionNumber}")
    public Question getQuestionTrueorFalse(@PathVariable String examID, @PathVariable String versionID, @PathVariable String questionNumber){
        // Tem de devolver a question True or False - TOFQ
        return examService.getQuestion(examID,versionID,questionNumber);
    }

    // Rota - PUT /exam/QuestionTrueorFalse/{examID}
    @PutMapping("exam/QuestionTrueorFalse/{examID}")
    public int updateQuestionTrueorFalse(@PathVariable String examID, @RequestBody TrueOrFalse trueOrFalseQuestion){
        // Tem de devolver a question True or False atualizada - TOFQ
        return examService.updateQuestion(examID, trueOrFalseQuestion);
    }

    // Rota - POST /exam/QuestionWriting?examID=xxxx
    @PostMapping("exam/QuestionWriting")
    public int addWritingQuestion(@RequestParam("examID") String examID, @RequestBody Writing writingQuestion){
        // return examService.createWritingQuestion(examName,writingQuestion)
        // Tem de dar return a um inteiro dizendo se a questão foi criada com sucesso ou não
        return examService.addQuestion(examID, writingQuestion);
    }

    // Rota - GET /exam/QuestionWriting/{examID}/{versionID}/{questionNumber}
    @GetMapping("exam/QuestionWriting/{examID}/{versionID}/{questionNumber}")
    public Question getWritingQuestion(@PathVariable String examID, @PathVariable String versionID, @PathVariable String questionNumber){
        // Tem de dar return a questão de escrita pedida - Writing
        return examService.getQuestion(examID, versionID, questionNumber);
    }

    // Rota - PUT /exam/QuestionWriting/{examID}
    @PutMapping("exam/QuestionWriting/{examID}")
    public int updateWritingQuestion(@PathVariable String examID, @RequestBody Writing writingQuestion){
        // Tem de dar return a questão de escrita corretamente atualizada - Writing
        return examService.updateQuestion(examID, writingQuestion);
    }

    // Rota - POST /exam/QuestionCompleteSpaces?examID=xxxx
    @PostMapping("exam/QuestionCompleteSpaces")
    public int addCompleteSpacesQuestion(@RequestParam("examID") String examID, @RequestBody CompleteSpaces completeSpacesQuestion){
        // return examService.createCompleteSpacesQuestion(examName,completeSpacesQuestion)
        return examService.addQuestion(examID, completeSpacesQuestion);
    }

    // // Rota - GET /exam/QuestionCompleteSpaces/{examID}/{versionID}/{questionNumber}
    @GetMapping("exam/QuestionCompleteSpaces/{examID}/{versionID}/{questionNumber}")
    public Question getCompleteSpacesQuestion(@PathVariable String examID, @PathVariable String versionID, @PathVariable String questionNumber){
        return this.examService.getQuestion(examID, versionID, questionNumber);
    }  

    // Rota - PUT /exam/QuestionCompleteSpaces/{examID}
    @PutMapping("exam/QuestionCompleteSpaces/{examID}")
    public int updateCompleteSpacesQuestion(@PathVariable String examID, @RequestBody CompleteSpaces completeSpacesQuestion){
        return this.examService.updateQuestion(examID, completeSpacesQuestion);
    }

    // Rota - DELETE /exam/Question/{examID}/{versionID}/{questionNumber}
    @DeleteMapping("exam/Question/{examID}/{versionID}/{questionNumber}") 
    public int deleteQuestion(@PathVariable String examID, @PathVariable String versionID, @PathVariable String questionNumber){
        return this.examService.deleteQuestion(examID, versionID, Integer.parseInt(questionNumber));
    }

    // Rota - POST /enrollStudents?examID=xxxx
    @PostMapping("enrollStudents")
    public int enrollStudent(@RequestParam("examID") String examID, @RequestBody List<String> students) {
        // Dá return a um inteiro indicando se o aluno foi inscrito no exame ou não
        return examService.enrollStudents(examID, students);
    }

    // Rota - POST /exam/saveCompleteSpacesAnswer/{examID}/{versionID}/{questionNumber}/{studentID}
    @PostMapping("exam/saveCompleteSpacesAnswer/{examID}/{versionID}/{questionNumber}/{studentID}")
    public int saveCompleteSpacesAnswer(@PathVariable String examID, @PathVariable String versionID, 
        @PathVariable String questionNumber, @PathVariable String studentID, 
        @RequestBody CompleteSpacesAnswer csa){
        return this.examService.saveAnswer(examID, versionID, Integer.parseInt(questionNumber), studentID, csa);
    }
 
    // Rota - POST /exam/saveWritingAnswer/{examID}/{versionID}/{questionNumber}/{studentID}
    @PostMapping("exam/saveWritingAnswer/{examID}/{versionID}/{questionNumber}/{studentID}")
    public int saveWritingAnswer(@PathVariable String examID, @PathVariable String versionID, 
        @PathVariable String questionNumber, @PathVariable String studentID, 
        @RequestBody WritingAnswer wa){
        return this.examService.saveAnswer(examID, versionID, Integer.parseInt(questionNumber), studentID, wa);
    }

    // Rota - POST /exam/saveTrueOrFalseAnswer/{examID}/{versionID}/{questionNumber}/{studentID}
    @PostMapping("exam/saveTrueOrFalseAnswer/{examID}/{versionID}/{questionNumber}/{studentID}")
    public int saveTrueOrFalseAnswer(@PathVariable String examID, @PathVariable String versionID, 
        @PathVariable String questionNumber, @PathVariable String studentID, 
        @RequestBody TrueOrFalseAnswer tfa){
        return this.examService.saveAnswer(examID, versionID, Integer.parseInt(questionNumber), studentID, tfa);
    }

    // Rota - POST /exam/saveMultipleChoiceAnswer/{examID}/{versionID}/{questionNumber}/{studentID}
    @PostMapping("exam/saveMultipleChoiceAnswer/{examID}/{versionID}/{questionNumber}/{studentID}")
    public int saveMultipleChoiceAnswer(@PathVariable String examID, @PathVariable String versionID, 
        @PathVariable String questionNumber, @PathVariable String studentID, 
        @RequestBody MultipleChoiceAnswer mca){
        return this.examService.saveAnswer(examID, versionID, Integer.parseInt(questionNumber), studentID, mca);
    }

    // Rota - POST /exam/autoCorrect?examID=xxxx
    @PostMapping("exam/autoCorrect")
    public int autoCorrectExam(@RequestParam("examID") String examID){
        return this.examService.autoCorrectExam(examID);
    }

    // // Rota - GET /exams/getCorrection/{examName}/{numberStudent}/{questionNumber}
    // @GetMapping("exams/getCorrection/{examName}/{numberStudent}/{questionNumber}")
    // public void getQuestionCorrectionStudent(@PathVariable String examName, @PathVariable String studentNumber, @PathVariable String questionNumber){
    //     // Dá return da cotação em que a pergunta foi avaliada - int 
    //     // return examService.getQuestionCorrectionStudent(examName,studentNumber,questionNumber)
    // }
    // // Rota - POST /exam/{examID}?examName=xxxxstatus=xxxx
    // @PostMapping("updateExam/{examID}")
    // public int updateExam(@RequestParam("examName") String examName,@RequestParam("status") String status,@PathVariable UUID examID) {
    //     //return examService.updateExam(examName,status,examID)
    //     
    //     return 200;
    // }  
    // // Rota - GET /exam/getClassrooms?examName=xxxx
    // @GetMapping("exam/getClassrooms")
    // public int getClassroomsForExam (@RequestParam("examName") String examName) {
    //     // Tem de devolver o List <Booking>, por isso mudar na declaração da função
    //     // Função que nos fornece as salas disponíveis para a realização do exame 
    //     // examService.getClassroomsForExam(examName);
    //     return 200;
    // }
}   
