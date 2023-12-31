package ras.exams.exams.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Exam {
    private final UUID id;
    private ExamHeader header;
    private String teacherID;
    private List<String> enrolled;
    private Map<UUID, ExamVersion> versions;
    private Map<UUID, ExamAnswer> answers;

    // Usado na rota do controller
    public Exam(UUID id, String teacherID, ExamHeader eh){
        this.id = id;
        this.header = eh;
        this.teacherID = teacherID;
        this.enrolled = new ArrayList<>();
        eh.setExamId(this.id);
        this.versions = new HashMap<>();
        this.answers = new HashMap<>();
    }

    // Usado pela BD
    public Exam(UUID examID, String teacherID, List<String> enrolled, ExamHeader header, 
                Map<UUID, ExamVersion> versions, Map<UUID, ExamAnswer> answers) {
        this.id = examID;
        this.teacherID = teacherID;
        this.enrolled = new ArrayList<>(enrolled);
        this.header = header;
        this.versions = new HashMap<>(versions);
        this.answers = new HashMap<>(answers);
    }

    public List<String> getExamVersions(){
        List<String> res = new ArrayList<>();
        for(UUID id: this.versions.keySet()){
            res.add(id.toString());
        }
        return res;
    }

    public UUID addExamVersion(){
        UUID examVersionID = UUID.randomUUID();
        this.versions.put(examVersionID, new ExamVersion(examVersionID, this.id));
        return examVersionID;
    }

    public int removeExamVersion(String examVersionID){
        UUID evid = UUID.fromString(examVersionID);
        if(!this.versions.containsKey(evid)){
            return 404;
        }

        this.versions.remove(evid);
        return 200;
    }

    // Returns false if versionNumber doesn't exist
    public int addQuestion(UUID examVersionID, Question q){
        if(!this.versions.containsKey(examVersionID)){
            return 404;
        }
        return this.versions.get(examVersionID).addQuestion(q);
    }

    // Returns a specific question from a specific exam version
    public Question getQuestion(String versionID, int questionNumber){
        return this.versions.get(UUID.fromString(versionID)).getQuestion(questionNumber);
    }

    public int updateQuestion(UUID versionID, Question q){
        if(!this.versions.containsKey(versionID)){
            return 404;
        }

        return this.versions.get(versionID).updateQuestion(q);
    }

    public int deleteQuestion(UUID versionID, int questionNumber){
        if(!this.versions.containsKey(versionID)){
            return 404;
        }
        int res = this.versions.get(versionID).deleteQuestion(questionNumber);
        return res;
    }

    // Adiciona os estudantes se eles ainda não foram anteriormente adicionados
    // Para além disto, cria automáticamente uma resposta de exame para cada um deles
    public void enrollStudents(List<String> students){
        for(String s: students){
            if(!this.enrolled.contains(s)){
                this.enrolled.add(s);
                this.answers.put(UUID.fromString(s), new ExamAnswer(s, this.id));
            }
        }
    }

    /*
     * Para guardar a resposta a uma pergunta:
     *  1. O aluno tem que estar inscrito no exame
     *  2. O aluno tem que ter criada uma resposta a exame
     *  3. A respetiva pergunta tem que já existir
     */ // /exam/saveWritingAnswer/79670828-8c1c-4c97-991b-c61725972e4f/ECF4F428-2C08-4734-BD1F-58CA270EAF54/1/44444444-4444-4444-4444-444444444444
    public int saveAnswer(String versionID, int qn, String studentID, Answer a){
        UUID studentUUID = UUID.fromString(studentID);
        UUID versionUUID = UUID.fromString(versionID);  

        if(this.enrolled.contains(studentID) && this.answers.containsKey(studentUUID) && this.versions.get(versionUUID).getQuestion(qn) != null){
            ExamAnswer ea = this.answers.get(studentUUID);
            a.setExamAnswerID(ea.getExamAnswerId());
            Question q = this.versions.get(versionUUID).getQuestion(qn);
            a.setQuestionID(q.getQuestionId());
            switch (a.getClass().getSimpleName()) {
                case "CompleteSpacesAnswer":
                    CompleteSpacesAnswer csa = (CompleteSpacesAnswer) a;
                    csa.setQuestion(q);
                    break;
                case "WritingAnswer":
                    WritingAnswer wa = (WritingAnswer) a;
                    wa.setQuestion(q);
                    break;
                case "TrueOrFalseAnswer":
                    TrueOrFalseAnswer tfa = (TrueOrFalseAnswer) a;
                    TrueOrFalse tof = (TrueOrFalse) q; 
                    for(TOFQAnswer o: tfa.getAnswers()){
                        o.setOption(tof.getQuestionOnOption(o.getOption().getOptionNumber()));
                    }
                    break;
                case "MultipleChoiceAnswer":
                    MultipleChoiceAnswer mca = (MultipleChoiceAnswer) a;
                    MultipleChoice mc = (MultipleChoice) q;
                    for(ChoiceAnswer c: mca.getAnswers()){
                        c.setChoice(mc.getChoiceOnChoiceNumber(c.getChoice().getChoiceNumber()));
                    }
                    break;
                default:
                    System.out.println("Classe de resposta inválida");
                    return 404;
            }
            ea.addAnswer(a);
            return 200;
        }   
        return 404;
    }

    public void autoCorrect(){
        for(ExamAnswer ea: this.answers.values()){
            ea.calculateGrade();
        }
    }

    public ExamAnswer getExamAnswer(String studentID){
        UUID studentUUID = UUID.fromString(studentID);
        if(this.answers.containsKey(studentUUID)){
            return this.answers.get(studentUUID);
        }
        return null;
    }

    public UUID getID(){
        return this.id;
    }

    public List<String> getEnrolled(){
        return this.enrolled;
    }

    public ExamHeader getHeader(){
        return this.header;
    }
    
    public void setHeader(ExamHeader header){
        this.header = header;
        this.header.setExamId(this.id);
    }

    public Map<UUID, ExamVersion> getVersions(){
        return this.versions;
    }

    public Map<UUID,ExamAnswer> getAnswers(){
        return this.answers;
    }

    public String getTeacherID(){
        return this.teacherID;
    }

    public String toString(){
        String str = "Exam-\nId: " + this.id + "\nName: " + this.header.getExamName();
        str += "\nExamUC: " + this.header.getExamUC() + "\nAdmissionTime: " + this.header.getExamAdmissionTime();
        return str;
    }

    public void setEnrolled(String numberStudent){
        this.enrolled.add(numberStudent);
    }
}
