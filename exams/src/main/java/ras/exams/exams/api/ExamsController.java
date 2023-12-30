package ras.exams.exams.api;

import ras.exams.exams.model.CompleteSpaces;
import ras.exams.exams.model.CompleteSpacesAnswer;
import ras.exams.exams.model.Exam;
import ras.exams.exams.model.ExamAnswer;
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
    public boolean addExamHeader(@RequestBody ExamHeader examHeader) {
        // Tem de devolver um inteiro, indicando se o examHeader foi adicionado ou não
        return examService.addExamHeader(examHeader);
    }

    // Rota - POST /exams/versions?examName=xxxx&versionNumber=xxxx
    @PostMapping("exams/versions")
    public boolean createExamVersion(@RequestParam("examName") String examName, @RequestParam("versionNumber") String versionNumber){
        return examService.addExamVersion(examName, Integer.parseInt(versionNumber));
    }

    // Rota - DELETE /exams/versions?examName=xxxx&versionNumber=xxxx
    @DeleteMapping("exams/versions")
    public boolean deleteExamVersion(@RequestParam("examName") String examName, @RequestParam("versionNumber") String versionNumber){
        return examService.removeExamVersion(examName, Integer.parseInt(versionNumber));
    }  
    
    // Rota - POST /exams/QuestionMultipleChoice?examName=xxxx
    @PostMapping("exams/QuestionMultipleChoice")
    public boolean addMultipleChoiceQuestion(@RequestParam("examName") String examName, @RequestBody MultipleChoice multipleChoice) {
        // Tem de devolver um inteiro, indicando se a questão de escolha múltipla foi adicionada ou não
        return examService.addQuestion(examName, multipleChoice);
    }
    
    // Rota - GET /exams/QuestionMultipleChoice/{examName}/{versionNumber}/{questionNumber}
    @GetMapping("exams/QuestionMultipleChoice/{examName}/{versionNumber}/{questionNumber}")
    public Question getQuestionMultipleChoice(@PathVariable String examName,@PathVariable String versionNumber,@PathVariable String questionNumber) {
        // Tem de devolver uma questão de escolha múltipla - MultipleChoice 
        return examService.getQuestion(examName, versionNumber, questionNumber);
    }
    
    // Rota - PUT /exams/QuestionMultipleChoice/{examName}
    @PutMapping("exams/QuestionMultipleCoice/{examName}")
    public boolean updateQuestionMultipleChoice(@PathVariable String examName, @RequestBody MultipleChoice multipleChoice) {
        // Tem de devolver uma questão de escolha múltipla - MultipleChoice
        return examService.updateQuestion(examName, multipleChoice);
    }

    // Rota - POST /exams/QuestionTrueorFalse?examName=xxxx
    @PostMapping("exams/QuestionTrueorFalse")
    public boolean addTrueorFalseQuestion(@RequestParam("examName") String examName,@RequestBody TrueOrFalse trueOrFalseQuestion) {
        // Tem de devolver um inteiro, indicando se a questão V/F foi adicionada ou não
        return examService.addQuestion(examName, trueOrFalseQuestion);
    }
    
    // Rota - GET /exams/QuestionTrueorFalse/{examName}/{versionNumber}/{questionNumber}
    @GetMapping("exams/QuestionTrueorFalse/{examName}/{versionNumber}/{questionNumber}")
    public Question getQuestionTrueorFalse(@PathVariable String examName, @PathVariable String versionNumber, @PathVariable String questionNumber){
        // Tem de devolver a question True or False - TOFQ
        return examService.getQuestion(examName,versionNumber,questionNumber);
    }

    // Rota - PUT /exams/QuestionTrueorFalse/{examName}
    @PutMapping("exams/QuestionTrueorFalse/{examName}")
    public boolean updateQuestionTrueorFalse(@PathVariable String examName, @RequestBody TrueOrFalse trueOrFalseQuestion){
        // Tem de devolver a question True or False atualizada - TOFQ
        return examService.updateQuestion(examName, trueOrFalseQuestion);
    }

    // Rota - POST /exams/QuestionWriting?examName=xxx
    @PostMapping("exams/QuestionWriting")
    public boolean addWritingQuestion(@RequestParam("examName") String examName, @RequestBody Writing writingQuestion){
        // return examService.createWritingQuestion(examName,writingQuestion)
        // Tem de dar return a um inteiro dizendo se a questão foi criada com sucesso ou não
        return examService.addQuestion(examName, writingQuestion);
    }

    // Rota - GET /exams/QuestionWriting/{examName}/{versionNumber}/{questionNumber}
    @GetMapping("exams/QuestionWriting/{examName}/{versionNumber}/{questionNumber}")
    public Question getWritingQuestion(@PathVariable String examName, @PathVariable String versionNumber, @PathVariable String questionNumber){
        // Tem de dar return a questão de escrita pedida - Writing
        return examService.getQuestion(examName, versionNumber, questionNumber);
    }

    // Rota - PUT /exams/QuestionWriting/{examName}
    @PutMapping("exams/QuestionWriting/{examName}")
    public boolean updateWritingQuestion(@PathVariable String examName, @RequestBody Writing writingQuestion){
        // Tem de dar return a questão de escrita corretamente atualizada - Writing
        return examService.updateQuestion(examName, writingQuestion);
    }

    // Rota - POST /exams/QuestionCompleteSpaces?examName=xxx
    @PostMapping("exams/QuestionCompleteSpaces")
    public boolean addCompleteSpacesQuestion(@RequestParam("examName") String examName,@RequestBody CompleteSpaces completeSpacesQuestion){
        // return examService.createCompleteSpacesQuestion(examName,completeSpacesQuestion)
        return examService.addQuestion(examName, completeSpacesQuestion);
    }

    // Rota - GET /exams/QuestionCompleteSpaces/{examName}/{versionNumber}/{questionNumber}
    @GetMapping("exams/QuestionCompleteSpaces/{examName}/{versionNumber}/{questionNumber}")
    public Question getCompleteSpacesQuestion(@PathVariable String examName, @PathVariable String versionNumber, @PathVariable String questionNumber){
        return this.examService.getQuestion(examName, versionNumber, questionNumber);
    }  

    // Rota - POST /exam/createExamAnswer?examName=xxxx
    @PostMapping("exam/createExamAnswer")
    public int createExamAnswer(@RequestParam("examName") String examName, @RequestBody ExamAnswer examAnswer){
        // Tem de devolver um inteiro, indicando se o exame foi guardado com sucesso ou não
       return this.examService.createExamAnswer(examName, examAnswer);
    }

    // Rota - POST /exam/saveCompleteSpacesAnswer/{examName}/{versionNumber}/{questionNumber}/{studentID}
    @PostMapping("exam/saveCompleteSpacesAnswer/{examName}/{versionNumber}/{questionNumber}/{studentID}")
    public int saveCompleteSpacesAnswer(@PathVariable String examName, @PathVariable String versionNumber, 
        @PathVariable String questionNumber, @PathVariable String studentID, 
        @RequestBody CompleteSpacesAnswer csa){
        return this.examService.saveCompleteSpacesAnswer(examName, Integer.parseInt(versionNumber), Integer.parseInt(questionNumber), studentID, csa);
    }

    // Rota - POST /exam/saveWritingAnswer/{examName}/{versionNumber}/{questionNumber}/{studentID}
    @PostMapping("exam/saveWritingAnswer/{examName}/{versionNumber}/{questionNumber}/{studentID}")
    public int saveWritingAnswer(@PathVariable String examName, @PathVariable String versionNumber, 
        @PathVariable String questionNumber, @PathVariable String studentID, 
        @RequestBody WritingAnswer wa){
        return this.examService.saveWritingAnswer(examName, Integer.parseInt(versionNumber), Integer.parseInt(questionNumber), studentID, wa);
    }

    // Rota - POST /exam/saveTrueOrFalseAnswer/{examName}/{versionNumber}/{questionNumber}/{studentID}
    @PostMapping("exam/saveTrueOrFalseAnswer/{examName}/{versionNumber}/{questionNumber}/{studentID}")
    public int saveTrueOrFalseAnswer(@PathVariable String examName, @PathVariable String versionNumber, 
        @PathVariable String questionNumber, @PathVariable String studentID, 
        @RequestBody TrueOrFalseAnswer tfa){
        return this.examService.saveTrueOrFalseAnswer(examName, Integer.parseInt(versionNumber), Integer.parseInt(questionNumber), studentID, tfa);
    }

    // Rota - POST /exam/saveMultipleChoiceAnswer/{examName}/{versionNumber}/{questionNumber}/{studentID}
    @PostMapping("exam/saveMultipleChoiceAnswer/{examName}/{versionNumber}/{questionNumber}/{studentID}")
    public int saveMultipleChoiceAnswer(@PathVariable String examName, @PathVariable String versionNumber, 
        @PathVariable String questionNumber, @PathVariable String studentID, 
        @RequestBody MultipleChoiceAnswer mca){
        return this.examService.saveMultipleChoiceAnswer(examName, Integer.parseInt(versionNumber), Integer.parseInt(questionNumber), studentID, mca);
    }

    // Rota - PUT /exams/QuestionCompleteSpaces/{examName}
    @PutMapping("exams/QuestionCompleteSpaces/{examName}")
    public boolean updateCompleteSpacesQuestion(@PathVariable String examName, @RequestBody CompleteSpaces completeSpacesQuestion){
        return this.examService.updateQuestion(examName, completeSpacesQuestion);
    }

    // Rota - GET /exams/getCorrection/{examName}/{numberStudent}/{questionNumber}
    @GetMapping("exams/getCorrection/{examName}/{numberStudent}/{questionNumber}")
    public void getQuestionCorrectionStudent(@PathVariable String examName, @PathVariable String studentNumber, @PathVariable String questionNumber){
        // Dá return da cotação em que a pergunta foi avaliada - int 
        // return examService.getQuestionCorrectionStudent(examName,studentNumber,questionNumber)
    }

    // Rota - GET /getExams
    @GetMapping("getExams")
    public List<Exam> getExams(){
        return examService.getExams();
    }

    // Rota - GET /getExamsByTeacher/{teacherNumber}
    @GetMapping("getExamsByTeacher/{teacherNumber}")
    public void getExamsByTeacher(@PathVariable String teacherNumber) {
        // return examService.getExamsByTeacher(teacherNumber);
    }

    // Rota - GET /getExamHeader?examName=xxxx
    @GetMapping("getExamHeader")
    public ExamHeader getExamHeader(@RequestParam("examName") String examName) {
        // Dá return de um header de um exame - ExamHeader
        return examService.getExamHeader(examName);
    }

    // Rota - PUT /exams/editExamHeader?examName=xxxx
    @PutMapping("exams/editExamHeader")
    public int editExamHeader(@RequestParam("examName") String examName, @RequestBody ExamHeader examHeader) {
        // Dá return de um int se o header do exame foi alterado ou não 
        return ((ExamsService) examService).editExamHeader(examName,examHeader);
    }

    // Rota - GET /getExambyStudent?studentNumber=xxxx
    @GetMapping("getExambyStudent")
    public List<Exam> getExambyStudent(@RequestParam("studentNumber") String studentNumber) {
        // Dá return a uma lista de exames na qual o aluno está inscrito - List<Exam>
        return examService.getExamsbyStudent(studentNumber);
    }

    // Rota - PUT /enrollStudents?examName=xxxx
    @PutMapping("enrollStudents")
    public int enrollStudent(@RequestParam("examName") String examName, @RequestBody List<String> students) {
        // Dá return a um inteiro indicando se o aluno foi inscrito no exame ou não
        return examService.enrollStudents(examName, students);
    }

    // Rota - GET /getSpecificExamforStudent?studentNumber=xxxxexamName=xxxx
    @GetMapping("getSpecificExamforStudent")
    public Exam getSpecificExamforStudent(@RequestParam("studentNumber") String studentNumber,@RequestParam("examName") String examName) {
        // Dá return a um exame especifico do aluno - Exam 
        return examService.getSpecificExamforStudent(studentNumber,examName);
    }

    // Rota - POST /replyExam/{numberStudent}/{examName}?questionID=xxxx
    @PostMapping("replyExam/{numberStudent}/{examName}")
    public int replyExam(@PathVariable String numberStudent,@PathVariable String examName,@RequestParam("questionID") String questionID,@RequestBody String answer) {
        //Dá return de um inteiro indicando se a resposta foi devidamente dada
        // return examService.replyExam(numberStudent,examName,questionID,answer)
        return 200;
    }

    // Rota - GET /exam/findByTags?tags=xxxx
    @GetMapping("exam/findByTags")
    public void getExamsbyTags(@RequestParam("tags") List<String> tags) {
        // Dá return da lista de exames que contêm aquelas tags - List<Exam>
        // return examService.getExamsbyTags(tags)
    }
    
    // Rota - GET /exam/{examID}
    @GetMapping("exam/{examID}")
    public Exam getExam(@PathVariable UUID examID) {
        // Dá return de um exame - Exam
        return examService.getExam(examID);
    }

    // Rota - POST /exam/{examID}?examName=xxxxstatus=xxxx
    @PostMapping("updateExam/{examID}")
    public int updateExam(@RequestParam("examName") String examName,@RequestParam("status") String status,@PathVariable UUID examID) {
        //return examService.updateExam(examName,status,examID)
        
        return 200;
    }

    // Rota - DELETE /exam/{examID}
    @DeleteMapping("exam/{examID}")
    public int deleteExam(@PathVariable UUID examID){
        // return examService.deleteExam(examID)
        return 200;
    }

    // Rota - POST /exam/{examID}/uploadImage?additionalMetadata=xxxx
    @PostMapping("exam/{examID}/uploadImage")
        public int uploadImage(@PathVariable UUID examID,@RequestParam("additionalMetadata") String metadata) {
            // Dá return a um inteiro indicando se foi dado o upload de forma correta 
            // return examService.uploadImage(examID,metadata);
            return 200;
        }
        
        
}   
