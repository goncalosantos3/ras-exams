package ras.exams.exams.data;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import ras.exams.exams.model.ExamVersion;
import ras.exams.exams.model.Question;

public class ExamVersionDAO {

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

    private ExamVersion getExamVersion (ResultSet rs) throws SQLException
    {
        UUID examVersionID = UUID.fromString(rs.getString("examVersionID")),
                examID = UUID.fromString(rs.getString("examID"));
        int versionNumber = rs.getInt("versionNumber");
        ExamVersion v = new ExamVersion(examVersionID, examID, versionNumber);
        v.addQuestions(questionDAO.getQuestionsFromVersion(examVersionID));
        return v;
    }

    
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

    
    public boolean contains(UUID key) {
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

    
    public boolean contains(ExamVersion ev) {
        return this.contains(ev.getVersionId());
    }

    
    public ExamVersion get(UUID examVersionID) {
        ExamVersion a = null;
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery("""
            SELECT BIN_TO_UUID(examVersionID) as examVersionID,
                    BIN_TO_UUID(examID) as examID,
                    versionNumber
            FROM examversion
            WHERE examVersionID=UUID_TO_BIN('"""+examVersionID.toString()+"')"))
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

    public ExamVersion get(UUID examID, int versionNumber) {
        ExamVersion a = null;
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery("""
            SELECT BIN_TO_UUID(examVersionID) as examVersionID,
                    BIN_TO_UUID(examID) as examID,
                    versionNumber
            FROM examversion
            WHERE examID=UUID_TO_BIN('"""+examID.toString()+"') AND versionNumber="+versionNumber))
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
    
    public boolean isEmpty() {
        return this.size() == 0;
    }
    
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

    
    public ExamVersion put(ExamVersion value) {
        ExamVersion rv = this.get(value.getVersionId());
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD); 
            Statement stm = conn.createStatement())
        {
            stm.executeUpdate("INSERT INTO examversion "+
                                "VALUES ("+
                                    "UUID_TO_BIN('"+value.getVersionId().toString()+"'),"+
                                    "UUID_TO_BIN('"+value.getExamID().toString()+"'),"+
                                    value.getVersionNumber()+
                                ") ON DUPLICATE KEY UPDATE "+
                                    "examVersionID=VALUES(examVersionID),"+
                                    "examID=VALUES(examID),"+
                                    "versionNumber=VALUES(versionNumber)");
            if (value.getQuestions() != null)
                for (Question q : value.getQuestions())
                    this.questionDAO.put(q);
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return rv;
    }

    
    public void putAll(Collection<ExamVersion> c) {
        for (ExamVersion ev : c)
        {
            this.put(ev);
        }
    }

    
    public ExamVersion remove(UUID key) {
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
    
    
    public String toString ()
    {
        String r = "{";
        boolean begin = true;
        for (ExamVersion ev : this.values())
        {
            r += (begin) ?"" :", ";
            r += ev.toString();
            begin = false;
        }
        return r + "}";
    }
}
