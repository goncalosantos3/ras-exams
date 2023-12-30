package ras.exams.exams.data;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import ras.exams.exams.model.ExamAnswer;
import ras.exams.exams.model.Answer;


public class ExamAnswerDAO {

    private static ExamAnswerDAO singleton = null;
    private AnswerDAO answerDAO;

    private ExamAnswerDAO()
    {
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD); Statement stm = conn.createStatement())
        {
            String sql = 
            """
            CREATE TABLE IF NOT EXISTS `ras_exams`.`examanswer` (
                `examAnswerID` BINARY(16) NOT NULL,
                `examID` BINARY(16) NOT NULL,
                `studentID` BINARY(16) NOT NULL,
                `grade` INT NULL DEFAULT NULL,
                PRIMARY KEY (`examAnswerID`),
                INDEX `studentAnswer_idx` (`examID` ASC, `studentID` ASC) VISIBLE,
                CONSTRAINT `studentAnswer`
                    FOREIGN KEY (`examID` , `studentID`)
                    REFERENCES `ras_exams`.`enrolledstudents` (`examID` , `studentID`))
            """;
            stm.executeUpdate(sql);
            this.answerDAO = AnswerDAO.getInstance();
        }
        catch (SQLException e)
        {
            // Erro a criar tabela
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }

    }
            
    public static ExamAnswerDAO getInstance()
    {
        if (ExamAnswerDAO.singleton == null)
        {
            ExamAnswerDAO.singleton = new ExamAnswerDAO();
        }
        return ExamAnswerDAO.singleton;
    }

    public List<ExamAnswer> getExamAnswersFromExam (UUID examID)
    {
        List<ExamAnswer> examAnswers = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery("""
            SELECT BIN_TO_UUID(examAnswerID) as examAnswerID,
                    BIN_TO_UUID(studentID) as studentID,
                    grade
            FROM examanswer
            WHERE examID=UUID_TO_BIN('"""+examID.toString()+"')"))
        {
            if (rs.next())
            {
                UUID examAnswerID = UUID.fromString(rs.getString("examAnswerID")),
                        studentID = UUID.fromString(rs.getString("studentID"));
                int grade = rs.getInt("grade");
                examAnswers.add(new ExamAnswer(examAnswerID, 
                                    examID, 
                                    studentID, 
                                    grade, 
                                    this.answerDAO.getAnswersFromExamAnswer(examAnswerID)));
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return examAnswers; 
    }

    public ExamAnswer getStudentExamAnswer (UUID studentID)
    {
        ExamAnswer a = null;
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery("""
            SELECT BIN_TO_UUID(examAnswerID) as examAnswerID,
                    BIN_TO_UUID(examID) as examID,
                    grade
            FROM examanswer
            WHERE studentID=UUID_TO_BIN('"""+studentID.toString()+"')"))
        {
            if (rs.next())
            {
                UUID examAnswerID = UUID.fromString(rs.getString("examAnswerID")),
                        examID = UUID.fromString(rs.getString("examID"));
                int grade = rs.getInt("grade");
                a = new ExamAnswer(examAnswerID, 
                                    examID, 
                                    studentID, 
                                    grade, 
                                    this.answerDAO.getAnswersFromExamAnswer(examAnswerID));
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return a;
    }

    
    public void clear() {
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD); Statement stm = conn.createStatement())
        {
            stm.execute("SET FOREIGN_KEY_CHECKS=0");
            stm.executeUpdate("TRUNCATE examanswer");
            this.answerDAO.clear();
            stm.execute("SET FOREIGN_KEY_CHECKS=1");
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
    }

    
    public boolean contains(UUID key) {
        boolean r;
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery("SELECT examAnswerID FROM examanswer WHERE examAnswerID=UUID_TO_BIN('"+key.toString()+"')"))
        {
            r = rs.next();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return r;
    }

    
    public boolean contains(ExamAnswer ea) {
        return this.contains(ea.getExamAnswerId());
    }

    
    public ExamAnswer get(UUID key) {
        if (!(key instanceof UUID))
            return null;
        ExamAnswer a = null;
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery("""
            SELECT BIN_TO_UUID(examAnswerID) as examAnswerID,
                    BIN_TO_UUID(examID) as examID,
                    BIN_TO_UUID(studentID) as studentID,
                    grade
            FROM examanswer
            WHERE examAnswerID=UUID_TO_BIN('"""+((UUID)key).toString()+"')"))
        {
            if (rs.next())
            {
                UUID examAnswerID = UUID.fromString(rs.getString("examAnswerID")),
                        examID = UUID.fromString(rs.getString("examID")),
                        studentID = UUID.fromString(rs.getString("studentID"));
                int grade = rs.getInt("grade");
                a = new ExamAnswer(examAnswerID, 
                                    examID, 
                                    studentID, 
                                    grade, 
                                    this.answerDAO.getAnswersFromExamAnswer(examAnswerID));
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return a;
    }

        public ExamAnswer get(String studentID) {
        ExamAnswer a = null;
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery("""
            SELECT BIN_TO_UUID(examAnswerID) as examAnswerID,
                    BIN_TO_UUID(examID) as examID,
                    BIN_TO_UUID(studentID) as studentID,
                    grade
            FROM examanswer
            WHERE studentID=UUID_TO_BIN('"""+studentID+"')"))
        {
            if (rs.next())
            {
                UUID examAnswerID = UUID.fromString(rs.getString("examAnswerID")),
                        examID = UUID.fromString(rs.getString("examID"));
                int grade = rs.getInt("grade");
                a = new ExamAnswer(examAnswerID, 
                                    examID, 
                                    UUID.fromString(studentID), 
                                    grade, 
                                    this.answerDAO.getAnswersFromExamAnswer(examAnswerID));
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return a;
    }

    
    public boolean isEmpty() {
        return this.size() == 0;
    }

    
    public Set<UUID> keySet() {
        Set<UUID> r = new HashSet<>();
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery("SELECT BIN_TO_UUID(examAnswerID) FROM examanswer"))
        {
            while (rs.next())
            {
                UUID questionID = UUID.fromString(rs.getString("examAnswerID"));
                r.add(questionID);
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return r;
    }

    
    public ExamAnswer put(ExamAnswer value) {
        ExamAnswer rv = this.get(value.getExamAnswerId());
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD); 
            Statement stm = conn.createStatement())
        {
            if (value.getAnswers() != null)
                value.calculateGrade();
            stm.executeUpdate("INSERT INTO examanswer "+
                                "VALUES ("+
                                    "UUID_TO_BIN('"+value.getExamAnswerId().toString()+"'),"+
                                    "UUID_TO_BIN('"+value.getExamID().toString()+"'),"+
                                    "UUID_TO_BIN('"+value.getStudentID().toString()+"'),"+
                                    value.getGrade()+
                                ") ON DUPLICATE KEY UPDATE "+
                                    "examAnswerID=VALUES(examAnswerID),"+
                                    "examID=VALUES(examID),"+
                                    "studentID=VALUES(studentID),"+
                                    "grade=VALUES(grade)");
            if (value.getAnswers() != null)
                for (Answer a : value.getAnswers())
                    this.answerDAO.put(a);
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return rv;
    }

    
    public void putAll(Collection<ExamAnswer> c) {
        for (ExamAnswer ea : c)
        {
            this.put(ea);
        }
    }

    
    public ExamAnswer remove(UUID key) {
        ExamAnswer rv = this.get(key);
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
            Statement stm = conn.createStatement())
        {
            stm.execute("SET FOREIGN_KEY_CHECKS=0");
            stm.executeUpdate("DELETE FROM examanswer WHERE examAnswerID=UUID_TO_BIN('"+key.toString()+"')");
            if (rv.getAnswers() != null)
                for (Answer a : rv.getAnswers())
                    this.answerDAO.remove(a.getAnswerID());
            stm.execute("SET FOREIGN_KEY_CHECKS=1");
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return rv;
    }

    public ExamAnswer remove(String key) { // key is a student id in String form
        ExamAnswer rv = this.get(key);
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
            Statement stm = conn.createStatement())
        {
            stm.execute("SET FOREIGN_KEY_CHECKS=0");
            stm.executeUpdate("DELETE FROM examanswer WHERE examAnswerID=UUID_TO_BIN('"+key.toString()+"')");
            if (rv.getAnswers() != null)
                for (Answer a : rv.getAnswers())
                    this.answerDAO.remove(a.getAnswerID());
            stm.execute("SET FOREIGN_KEY_CHECKS=1");
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return rv;
    }

    
    public int size() {
        int size = 0;
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery("SELECT count(*) FROM examanswer"))
        {
            if (rs.next())
                size = rs.getInt(1);
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return size;
    }

    
    public Collection<ExamAnswer> values() {
        Set<ExamAnswer> rSet = new HashSet<>();
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery(
            """
            SELECT BIN_TO_UUID(examAnswerID) as examAnswerID,
                    BIN_TO_UUID(examID) as examID,
                    BIN_TO_UUID(studentID) as studentID,
                    grade
            FROM examanswer"""))
        {
            while(rs.next())
            {
                UUID examAnswerID = UUID.fromString(rs.getString("examAnswerID")),
                        examID = UUID.fromString(rs.getString("examID")),
                        studentID = UUID.fromString(rs.getString("studentID"));
                int grade = rs.getInt("grade");
                rSet.add(new ExamAnswer(examAnswerID, 
                                    examID, 
                                    studentID, 
                                    grade, 
                                    this.answerDAO.getAnswersFromExamAnswer(examAnswerID)));
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return rSet;
    }
    
    
    public String toString ()
    {
        String r = "{";
        boolean begin = true;
        for (ExamAnswer examAnswer : this.values())
        {
            r += (begin) ?"" :", ";
            r += examAnswer.toString();
            begin = false;
        }
        return r + "}";
    }
}
