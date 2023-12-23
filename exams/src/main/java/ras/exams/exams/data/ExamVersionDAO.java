package ras.exams.exams.data;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import ras.exams.exams.model.ExamVersion;
import ras.exams.exams.model.Question;

public class ExamVersionDAO implements Map<UUID, ExamVersion> {

    private static ExamVersionDAO singleton = null;
    private QuestionDAO questionDAO;

    private ExamVersionDAO()
    {
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD); Statement stm = conn.createStatement())
        {
            String sql = 
            """
            CREATE TABLE IF NOT EXISTS `ras_exams`.`examversion` (
                `examVersionID` BINARY(16) NOT NULL,
                `examID` BINARY(16) NOT NULL,
                `versionNumber` INT NULL DEFAULT NULL,
                PRIMARY KEY (`examVersionID`),
                INDEX `examID_idx` (`examID` ASC) VISIBLE,
                CONSTRAINT `examIDVersion`
                    FOREIGN KEY (`examID`)
                    REFERENCES `ras_exams`.`exam` (`examID`))
            """;
            stm.executeUpdate(sql);
            this.questionDAO = QuestionDAO.getInstance();
        }
        catch (SQLException e)
        {
            // Erro a criar tabela
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }

    }
            
    public static ExamVersionDAO getInstance()
    {
        if (ExamVersionDAO.singleton == null)
        {
            ExamVersionDAO.singleton = new ExamVersionDAO();
        }
        return ExamVersionDAO.singleton;
    }

    public List<ExamVersion> getExamVersionsFromExam (UUID examID)
    {
        List<ExamVersion> examAnswers = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery("""
            SELECT BIN_TO_UUID(examVersionID) as examVersionID,
                    BIN_TO_UUID(examID) as examID,
                    versionNumber
            FROM examversion
            WHERE examID=UUID_TO_BIN('"""+examID.toString()+"')"))
        {
            while (rs.next())
            {
                examAnswers.add(this.getExamVersion(rs));
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return examAnswers; 
    }

    // public ExamVersion getStudentExamVersion (UUID studentID)
    // {
    //     ExamVersion a = null;
    //     try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
    //         Statement stm = conn.createStatement();
    //         ResultSet rs = stm.executeQuery("""
    //         SELECT BIN_TO_UUID(examAnswerID) as examAnswerID,
    //                 BIN_TO_UUID(examID) as examID,
    //                 grade
    //         FROM examversion
    //         WHERE studentID=UUID_TO_BIN('"""+studentID.toString()+"')"))
    //     {
    //         if (rs.next())
    //         {
    //             UUID examAnswerID = UUID.fromString(rs.getString("examAnswerID")),
    //                     examID = UUID.fromString(rs.getString("examID"));
    //             int grade = rs.getInt("grade");
    //             a = new ExamVersion(examAnswerID, 
    //                                 examID, 
    //                                 studentID, 
    //                                 grade, 
    //                                 this.answerDAO.getAnswersFromExamVersion(examAnswerID));
    //         }
    //     }
    //     catch (SQLException e)
    //     {
    //         e.printStackTrace();
    //         throw new NullPointerException(e.getMessage());
    //     }
    //     return a;
    // }

    private ExamVersion getExamVersion (ResultSet rs) throws SQLException
    {
        UUID examVersionID = UUID.fromString(rs.getString("examVersionID")),
                examID = UUID.fromString(rs.getString("examID"));
        int versionNumber = rs.getInt("versionNumber");
        ExamVersion v = new ExamVersion(examVersionID, examID, versionNumber);
        v.addQuestions(questionDAO.getQuestionsFromVersion(examVersionID));
        return v;
    }

    @Override
    public void clear() {
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD); Statement stm = conn.createStatement())
        {
            stm.execute("SET FOREIGN_KEY_CHECKS=0");
            stm.executeUpdate("TRUNCATE examversion");
            this.questionDAO.clear();
            stm.execute("SET FOREIGN_KEY_CHECKS=1");
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
    }

    @Override
    public boolean containsKey(Object key) {
        boolean r;
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery("SELECT examVersionID FROM examversion WHERE examVersionID=UUID_TO_BIN('"+key.toString()+"')"))
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

    @Override
    public boolean containsValue(Object key) {
        ExamVersion v = (ExamVersion) key;
        return this.containsKey(v.getVersionId());
    }

    @Override
    public Set<Entry<UUID, ExamVersion>> entrySet() {
        Set<Entry<UUID, ExamVersion>> rSet = new HashSet<>();
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery(
            """
            SELECT BIN_TO_UUID(examVersionID) as examVersionID,
                    BIN_TO_UUID(examID) as examID,
                    versionNumber
            FROM examversion"""))
        {
            while(rs.next())
            {
                ExamVersion v = this.getExamVersion(rs);
                rSet.add(Map.entry(v.getVersionId(), v));
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return rSet;
    }

    @Override
    public ExamVersion get(Object key) {
        if (!(key instanceof UUID))
            return null;
        ExamVersion a = null;
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery("""
            SELECT BIN_TO_UUID(examVersionID) as examVersionID,
                    BIN_TO_UUID(examID) as examID,
                    versionNumber
            FROM examversion
            WHERE examVersionID=UUID_TO_BIN('"""+((UUID)key).toString()+"')"))
        {
            if (rs.next())
            {
                a = this.getExamVersion(rs);
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return a;
    }

    @Override
    public boolean isEmpty() {
        return this.size() == 0;
    }

    @Override
    public Set<UUID> keySet() {
        Set<UUID> r = new HashSet<>();
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery("SELECT BIN_TO_UUID(examVersionID) FROM examversion"))
        {
            while (rs.next())
            {
                UUID questionID = UUID.fromString(rs.getString("examVersionID"));
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

    @Override
    public ExamVersion put(UUID key, ExamVersion value) {
        ExamVersion rv = this.get(key);
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD); 
            Statement stm = conn.createStatement())
        {
            stm.executeUpdate("INSERT INTO examversion "+
                                "VALUES ("+
                                    "UUID_TO_BIN('"+key.toString()+"'),"+
                                    "UUID_TO_BIN('"+value.getExamID().toString()+"'),"+
                                    value.getVersionNumber()+
                                ") ON DUPLICATE KEY UPDATE "+
                                    "examVersionID=VALUES(examVersionID),"+
                                    "examID=VALUES(examID),"+
                                    "versionNumber=VALUES(versionNumber)");
            if (value.getQuestions() != null)
                for (Question q : value.getQuestions())
                    this.questionDAO.put(q.getQuestionId(), q);
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return rv;
    }

    @Override
    public void putAll(Map<? extends UUID, ? extends ExamVersion> m) {
        for (Entry<? extends UUID, ? extends ExamVersion> entry : m.entrySet())
        {
            this.put(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public ExamVersion remove(Object key) {
        ExamVersion rv = this.get(key);
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
            Statement stm = conn.createStatement())
        {
            stm.execute("SET FOREIGN_KEY_CHECKS=0");
            stm.executeUpdate("DELETE FROM examversion WHERE examVersionID=UUID_TO_BIN('"+key.toString()+"')");
            if (rv.getQuestions() != null)
                for (Question q : rv.getQuestions())
                    this.questionDAO.remove(q.getQuestionId());
            stm.execute("SET FOREIGN_KEY_CHECKS=1");
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return rv;
    }

    @Override
    public int size() {
        int size = 0;
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery("SELECT count(*) FROM examversion"))
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

    @Override
    public Collection<ExamVersion> values() {
        Set<ExamVersion> rSet = new HashSet<>();
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery(
            """
            SELECT BIN_TO_UUID(examVersionID) as examVersionID,
                    BIN_TO_UUID(examID) as examID,
                    versionNumber
            FROM examversion"""))
        {
            while(rs.next())
            {
                rSet.add(this.getExamVersion(rs));
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return rSet;
    }
    
    @Override
    public String toString ()
    {
        String r = "{";
        boolean begin = true;
        for (Map.Entry<UUID, ExamVersion> entry : this.entrySet())
        {
            r += (begin) ?"" :", ";
            r += entry.getKey() + "=" + entry.getValue().getVersionId();
            begin = false;
        }
        return r + "}";
    }
}
