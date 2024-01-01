package ras.exams.exams.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Exam {
    private final UUID id;
    private List<String> enrolled;
    private ExamHeader header;
    private Map<UUID, ExamVersion> versions;
    private Map<UUID, ExamAnswer> answers;

    // Usado na rota do controller
    public Exam(UUID id, ExamHeader eh){
        this.id = id;
        this.enrolled = new ArrayList<>();
        this.header = eh;
        eh.setExamId(this.id);
        this.versions = new HashMap<>();
        this.answers = new HashMap<>();
    }

    // Usado pela BD
    public Exam(UUID examID, List<String> enrolled, ExamHeader header, 
                Map<UUID, ExamVersion> versions, Map<UUID, ExamAnswer> answers) {
        this.id = examID;
        this.enrolled = new ArrayList<>(enrolled);
        this.header = header;
        this.versions = new HashMap<>(versions);
        this.answers = new HashMap<>(answers);
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

    // Adiciona os estudantes se eles ainda não foram anteriormente adicionados
    public void enrollStudents(List<String> students){
        for(String s: students){
            if(!this.enrolled.contains(s)){
                this.enrolled.add(s);
            }
        }
    }

    /*
     * Para guardar a resposta a uma pergunta:
     *  1. O aluno tem que estar inscrito no exame
     *  2. O aluno tem que ter criada uma resposta a exame
     *  3. A respetiva pergunta tem que já existir
     */
    public int saveCompleteSpacesAnswer(String studentID, int vn, int qn, CompleteSpacesAnswer csa){
        if(this.enrolled.contains(studentID) && this.answers.containsKey(studentID) && this.versions.get(vn).getQuestion(qn) != null){
            ExamAnswer ea = this.answers.get(studentID);
            csa.setExamAnswerID(ea.getExamAnswerId());
            Question q = this.versions.get(vn).getQuestion(qn);
            csa.setQuestion(q);
            csa.setQuestionID(q.getQuestionId());
            ea.addAnswer(csa);
            return 200;
        }   
        return 404;
    }   

    public int saveWritingAnswer(String studentID, int vn, int qn, WritingAnswer wa){
        if(this.enrolled.contains(studentID) && this.answers.containsKey(studentID) && this.versions.get(vn).getQuestion(qn) != null){
            ExamAnswer ea = this.answers.get(studentID);
            wa.setExamAnswerID(ea.getExamAnswerId());
            Question q = this.versions.get(vn).getQuestion(qn);
            wa.setQuestion(q);
            wa.setQuestionID(q.getQuestionId());
            ea.addAnswer(wa);
            return 200;
        }   
        return 404;
    }

    public int saveTrueOrFalseAnswer(String studentID, int vn, int qn, TrueOrFalseAnswer tfa){
        if(this.enrolled.contains(studentID) && this.answers.containsKey(studentID) && this.versions.get(vn).getQuestion(qn) != null){
            ExamAnswer ea = this.answers.get(studentID);
            tfa.setExamAnswerID(ea.getExamAnswerId());
            Question q = this.versions.get(vn).getQuestion(qn);
            tfa.setQuestionID(q.getQuestionId());
            TrueOrFalse tof = (TrueOrFalse) q;
            for(TOFQAnswer o: tfa.getAnswers()){
                System.out.println(o.getOption().getOptionNumber());
                System.out.println(tof.getQuestions());
                o.setOption(tof.getQuestionOnOption(o.getOption().getOptionNumber()));
                System.out.println(o.getOption());
            }
            ea.addAnswer(tfa);
            return 200;
        }   
        return 404;
    }

    public int saveMultipleChoiceAnswer(String studentID, int vn, int qn, MultipleChoiceAnswer mca){
        if(this.enrolled.contains(studentID) && this.answers.containsKey(studentID) && this.versions.get(vn).getQuestion(qn) != null){
            ExamAnswer ea = this.answers.get(studentID);
            mca.setExamAnswerID(ea.getExamAnswerId());
            Question q = this.versions.get(vn).getQuestion(qn);
            mca.setQuestionID(q.getQuestionId());
            MultipleChoice mc = (MultipleChoice) q;
            for(ChoiceAnswer ca: mca.getAnswers()){
                ca.setChoice(mc.getChoiceOnChoiceNumber(ca.getChoice().getChoiceNumber()));
            }
            ea.addAnswer(mca);
            return 200;
        }   
        return 404;
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

    public String toString(){
        String str = "Exam-\nId: " + this.id + "\nName: " + this.header.getExamName();
        str += "\nExamUC: " + this.header.getExamUC() + "\nAdmissionTime: " + this.header.getExamAdmissionTime();
        return str;
    }

    public void setEnrolled(String numberStudent){
        this.enrolled.add(numberStudent);
    }
}
