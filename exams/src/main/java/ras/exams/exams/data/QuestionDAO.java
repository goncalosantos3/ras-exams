package ras.exams.exams.data;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ras.exams.exams.model.Question;
import ras.exams.exams.model.CompleteSpaces;
import ras.exams.exams.model.MultipleChoice;
import ras.exams.exams.model.TrueOrFalse;
import ras.exams.exams.model.Writing;
import ras.exams.exams.model.Choice;
import ras.exams.exams.model.TOFQ;

public class QuestionDAO {

    private static QuestionDAO singleton = null;

    private QuestionDAO()
    {
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD); Statement stm = conn.createStatement())
        {
            String sql = 
            """
            CREATE TABLE IF NOT EXISTS `ras_exams`.`question` (
                `questionID` BINARY(16) NOT NULL,
                `questionNumber` INT NOT NULL,
                `questionType` CHAR(1) NULL DEFAULT NULL,
                `question` VARCHAR(512) NULL DEFAULT NULL,
                `versionID` BINARY(16) NULL DEFAULT NULL,
                PRIMARY KEY (`questionID`),
                INDEX `version_idx` (`versionID` ASC) VISIBLE,
                CONSTRAINT `version`
                    FOREIGN KEY (`versionID`)
                    REFERENCES `ras_exams`.`examversion` (`examVersionID`))
            """;
            stm.executeUpdate(sql);

            sql =
            """
            CREATE TABLE IF NOT EXISTS `ras_exams`.`completespacesquestion` (
                `questionID` BINARY(16) NOT NULL,
                `text` VARCHAR(2048) NULL DEFAULT NULL,
                PRIMARY KEY (`questionID`),
                CONSTRAINT `questionIDcs`
                    FOREIGN KEY (`questionID`)
                    REFERENCES `ras_exams`.`question` (`questionID`))
            """;
                
            stm.executeUpdate(sql);
            
            sql = 
            """
            CREATE TABLE IF NOT EXISTS `ras_exams`.`multiplechoicequestion` (
                `questionID` BINARY(16) NOT NULL,
                `choiceNumber` INT NOT NULL,
                `description` VARCHAR(256) NULL DEFAULT NULL,
                `correction` TINYINT NULL DEFAULT NULL,
                `score` INT NULL DEFAULT NULL,
                PRIMARY KEY (`questionID`, `choiceNumber`),
                CONSTRAINT `questionIDmc`
                    FOREIGN KEY (`questionID`)
                    REFERENCES `ras_exams`.`question` (`questionID`))
            """;

            stm.executeUpdate(sql);

            sql =
            """
            CREATE TABLE IF NOT EXISTS `ras_exams`.`trueorfalsequestion` (
                `questionID` BINARY(16) NOT NULL,
                `optionNumber` INT NOT NULL,
                `description` VARCHAR(256) NULL DEFAULT NULL,
                `correction` TINYINT NULL DEFAULT NULL,
                `score` INT NULL DEFAULT NULL,
                PRIMARY KEY (`questionID`, `optionNumber`),
                CONSTRAINT `questionIDtf`
                    FOREIGN KEY (`questionID`)
                    REFERENCES `ras_exams`.`question` (`questionID`))
            """;
                    
            stm.executeUpdate(sql);
            
            sql = 
            """
            CREATE TABLE IF NOT EXISTS `ras_exams`.`writingquestion` (
                `questionID` BINARY(16) NOT NULL,
                `criteria` VARCHAR(512) NULL DEFAULT NULL,
                `minimumLimit` INT NULL DEFAULT NULL,
                `maximumLimit` INT NULL DEFAULT NULL,
                PRIMARY KEY (`questionID`),
                CONSTRAINT `questionIDw`
                    FOREIGN KEY (`questionID`)
                    REFERENCES `ras_exams`.`question` (`questionID`))
            """;
            
            stm.executeUpdate(sql);
        }
        catch (SQLException e)
        {
            // Erro a criar tabela
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
    }
            
    public static QuestionDAO getInstance()
    {
        if (QuestionDAO.singleton == null)
        {
            QuestionDAO.singleton = new QuestionDAO();
        }
        return QuestionDAO.singleton;
    }

    public Choice getChoice(UUID questionID, int choiceNumber)
    {
        Choice c = null;
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery(
            """
            SELECT choiceNumber, description, correction, score 
            FROM multiplechoicequestion
            WHERE questionID=UUID_TO_BIN('"""+questionID.toString()+"') AND choiceNumber=" + choiceNumber))
        {
            if (rs.next())
            {
                String description = rs.getString("description");
                boolean correction = rs.getBoolean("correction");
                int score = rs.getInt("score");
                c = new Choice(description, correction, score, choiceNumber);
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return c;
    }

    public TOFQ getTOFQ(UUID questionID, int optionNumber)
    {
        TOFQ o = null;
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery(
            """
            SELECT optionNumber, description, correction, score 
            FROM trueorfalsequestion
            WHERE questionID=UUID_TO_BIN('"""+questionID.toString()+"') AND optionNumber=" + optionNumber))
        {
            if (rs.next())
            {
                String description = rs.getString("description");
                boolean correction = rs.getBoolean("correction");
                int score = rs.getInt("score");
                o = new TOFQ(description, correction, score, optionNumber);
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return o;
    }

    private List<Choice> getChoices(UUID questionID)
    {
        List<Choice> choices = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery("SELECT * FROM multiplechoicequestion WHERE questionID=UUID_TO_BIN('"+
                                                                            questionID.toString()+"')"))
        {
            while(rs.next())
            {
                String description = rs.getString("description");
                boolean correction = rs.getBoolean("correction");
                int score = rs.getInt("score");
                int choiceNumber = rs.getInt("choiceNumber");
                choices.add(new Choice(description, correction, score, choiceNumber));
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        choices.sort((c1, c2) -> c1.getChoiceNumber() - c2.getChoiceNumber() );
        return choices;
    }
        
    private List<TOFQ> getTOFQs (UUID questionID)
    {
        List<TOFQ> tofqs = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery("SELECT * FROM trueorfalsequestion WHERE questionID='"+
                                                                            questionID.toString()+"'"))
        {
            while(rs.next())
            {
                String description = rs.getString("description");
                boolean correction = rs.getBoolean("correction");
                int score = rs.getInt("score");
                int optionNumber = rs.getInt("optionNumber");
                tofqs.add(new TOFQ(description, correction, score, optionNumber));
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        tofqs.sort((t1, t2) -> t1.getOptionNumber() - t2.getOptionNumber());
        return tofqs;
    }
        
    private Writing getWriting (UUID questionID, String question, int questionNumber, UUID versionID)
    {
        Writing w = null;
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery("SELECT * FROM writingquestion WHERE questionID=UUID_TO_BIN('"+
                                                                            questionID.toString()+"')"))
        {
            if (rs.next())
            {
                String criteria = rs.getString("criteria");
                int minimumLimit = rs.getInt("minimumLimit");
                int maximumLimit = rs.getInt("maximumLimit");
                w = new Writing(questionID, question, questionNumber, versionID, criteria, minimumLimit, maximumLimit);
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }   
        return w;
    }

    private CompleteSpaces getCompleteSpaces (UUID questionID, String question, int questionNumber, UUID versionID)
    {
        CompleteSpaces c = null;
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery("SELECT * FROM completespacesquestion WHERE questionID=UUID_TO_BIN('"+
                                                                            questionID.toString()+"')"))
        {
            if (rs.next())
            {
                String text = rs.getString("text");
                c = new CompleteSpaces(questionID, question, questionNumber, versionID, text);
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }   
        return c;
    }
        
    private Question getQuestion(ResultSet rs)
    {
        Question q = null;
        try 
        {    
            UUID questionID = UUID.fromString(rs.getString("questionID"));
            String questionType = rs.getString("questionType");
            int questionNumber = rs.getInt("questionNumber");
            String question = rs.getString("question");
            UUID versionID = UUID.fromString(rs.getString("versionID"));

            switch (questionType) {
                case "C":
                    q = this.getCompleteSpaces(questionID, question, questionNumber, versionID);
                    break;
                case "M":
                    q = new MultipleChoice(questionID, 
                                            question, 
                                            questionNumber, 
                                            versionID.toString(),
                                            this.getChoices(questionID));
                    break;
                case "T":
                    q = new TrueOrFalse(questionID, 
                                            question, 
                                            questionNumber,
                                            versionID,
                                            this.getTOFQs(questionID));
                    break;
                case "W":
                    q = this.getWriting(questionID, question, questionNumber, versionID);
                    break;
                default:
                    break;
            }
        } 
        catch (SQLException e) 
        {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return q;
    }

    
    public void clear() {
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD); Statement stm = conn.createStatement())
        {
            stm.execute("SET FOREIGN_KEY_CHECKS=0");
            stm.executeUpdate("TRUNCATE completespacesquestion");
            stm.executeUpdate("TRUNCATE multiplechoicequestion");
            stm.executeUpdate("TRUNCATE trueorfalsequestion");
            stm.executeUpdate("TRUNCATE writingquestion");
            stm.executeUpdate("TRUNCATE question");
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
            ResultSet rs = stm.executeQuery("SELECT questionID FROM question WHERE questionID=UUID_TO_BIN('"+key.toString()+"')"))
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

    
    public boolean contains(Question q) {
        return this.contains(q.getQuestionId());
    }

    public List<Question> getQuestionsFromVersion(UUID versionID)
    {
        List<Question> questions = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery(
            """
            SELECT BIN_TO_UUID(questionID) as questionID,
                    questionNumber,
                    questionType,
                    question,
                    BIN_TO_UUID(versionID) as versionID
            FROM question 
            WHERE versionID=UUID_TO_BIN('"""+versionID.toString()+"')"))
        {
            while (rs.next())
            {
                Question q = this.getQuestion(rs);
                questions.add(q);
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return questions;
    }

    public Question get(UUID key) {
        Question q = null;
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery("""
            SELECT BIN_TO_UUID(questionID) as questionID,
                    questionNumber,
                    questionType,
                    question,
                    BIN_TO_UUID(versionID) as versionID 
            FROM question 
            WHERE questionID=UUID_TO_BIN('"""+key.toString()+"')"))
        {
            if (rs.next())
            {
                q = this.getQuestion(rs);
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return q;
    }
    
    public boolean isEmpty() {
        return this.size() == 0;
    }

    public Set<UUID> keySet() {
        Set<UUID> r = new HashSet<>();
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery("SELECT BIN_TO_UUID(questionID) FROM question"))
        {
            while (rs.next())
            {
                UUID questionID = UUID.fromString(rs.getString("questionID"));
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

    private void putType(UUID questionID, Question q, Statement stm) throws SQLException, InvalidQuestionException
    {
        char type = q.getQuestionType();
        if (type == 'C' && q instanceof CompleteSpaces)
        {
            CompleteSpaces c = (CompleteSpaces)q;
            Pattern pattern = Pattern.compile("^[^{}]*(\\{[^ ,]+,\\s*\\d+\\}[^{}]*)+$", Pattern.MULTILINE);
            Matcher matcher = pattern.matcher(c.getText());
            if (!matcher.matches())
                throw new InvalidQuestionException('C');
            stm.executeUpdate("INSERT INTO completespacesquestion "+
                                "VALUES ("+
                                    "UUID_TO_BIN('"+questionID.toString()+"'),"+
                                    "'"+c.getText()+"'"+
                                ") ON DUPLICATE KEY UPDATE "+
                                    "questionID=VALUES(questionID),"+
                                    "text=VALUES(text)");    
        }
        else if (type == 'M' && q instanceof MultipleChoice)
        {
            for (Choice c : ((MultipleChoice)q).getChoices())
            {
                stm.executeUpdate("INSERT INTO multiplechoicequestion "+
                                    "VALUES ("+
                                        "UUID_TO_BIN('"+questionID.toString()+"'),"+
                                        c.getChoiceNumber()+","+
                                        "'"+c.getDescription()+"',"+
                                        (c.getCorrection() ?1 :0)+","+
                                        c.getScore()+
                                    ") ON DUPLICATE KEY UPDATE "+
                                        "questionID=VALUES(questionID),"+
                                        "choiceNumber=VALUES(choiceNumber),"+
                                        "description=VALUES(description),"+
                                        "correction=VALUES(correction),"+
                                        "score=VALUES(score)");
            }
        }
        else if (type == 'T' && q instanceof TrueOrFalse)
        {
            for (TOFQ o : ((TrueOrFalse)q).getQuestions())
            {
                stm.executeUpdate("INSERT INTO trueorfalsequestion "+
                                    "VALUES ("+
                                        "UUID_TO_BIN('"+questionID.toString()+"'),"+
                                        o.getOptionNumber()+","+
                                        "'"+o.getDescription()+"',"+
                                        (o.getCorrection() ?1 :0)+","+
                                        o.getScore()+
                                    ") ON DUPLICATE KEY UPDATE "+
                                        "questionID=VALUES(questionID),"+
                                        "optionNumber=VALUES(optionNumber),"+
                                        "description=VALUES(description),"+
                                        "correction=VALUES(correction),"+
                                        "score=VALUES(score)");
            }
        }
        else if (type == 'W' && q instanceof Writing)
        {
            Writing w = (Writing)q;
            stm.executeUpdate("INSERT INTO writingquestion "+
                                "VALUES ("+
                                    "UUID_TO_BIN('"+questionID.toString()+"'),"+
                                    "'"+w.getCriteria()+"',"+
                                    w.getMinimumLimit()+","+
                                    w.getMaximumLimit()+
                                ") ON DUPLICATE KEY UPDATE "+
                                    "questionID=VALUES(questionID),"+
                                    "criteria=VALUES(criteria),"+
                                    "minimumLimit=VALUES(minimumLimit),"+
                                    "maximumLimit=VALUES(maximumLimit)");    
        }
    }

    
    public Question put(Question value) {
        Question rv = this.get(value.getQuestionId());
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD); 
            Statement stm = conn.createStatement())
        {
            stm.executeUpdate("INSERT INTO question "+
                                "VALUES ("+
                                    "UUID_TO_BIN('"+value.getQuestionId().toString()+"'),"+
                                    value.getQuestionNumber()+","+
                                    "'"+value.getQuestionType()+"',"+
                                    "'"+value.getQuestion()+"',"+
                                    "UUID_TO_BIN('"+value.getVersionID().toString()+"')"+
                                ") ON DUPLICATE KEY UPDATE "+
                                    "questionID=VALUES(questionID),"+
                                    "questionNumber=VALUES(questionNumber),"+
                                    "questionType=VALUES(questionType),"+
                                    "question=VALUES(question),"+
                                    "versionID=VALUES(versionID)");
            this.putType(value.getQuestionId(), value, stm);
        }
        catch (SQLException | InvalidQuestionException e)
        {
            if (rv == null)
                this.remove(value.getQuestionId());
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return rv;
    }

    
    public void putAll(Collection<Question> c) {
        for (Question q : c)
        {
            this.put(q);
        }
    }

    
    public Question remove(UUID key) {
        Question rv = this.get(key);
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
            Statement stm = conn.createStatement())
        {
            stm.execute("SET FOREIGN_KEY_CHECKS=0");
            stm.executeUpdate("DELETE FROM question WHERE questionID=UUID_TO_BIN('"+key.toString()+"')");
            stm.executeUpdate("DELETE FROM completespacesquestion WHERE questionID=UUID_TO_BIN('"+key.toString()+"')");
            stm.executeUpdate("DELETE FROM multiplechoicequestion WHERE questionID=UUID_TO_BIN('"+key.toString()+"')");
            stm.executeUpdate("DELETE FROM trueorfalsequestion WHERE questionID=UUID_TO_BIN('"+key.toString()+"')");
            stm.executeUpdate("DELETE FROM writingquestion WHERE questionID=UUID_TO_BIN('"+key.toString()+"')");
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
            ResultSet rs = stm.executeQuery("SELECT count(*) FROM question"))
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

    
    public Collection<Question> values() {
        Set<Question> rSet = new HashSet<>();
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery(
            """
            SELECT BIN_TO_UUID(questionID) as questionID,
            questionNumber,
            questionType,
            question,
            BIN_TO_UUID(versionID) as versionID from question"""))
        {
            while(rs.next())
            {
                Question q = this.getQuestion(rs);
                rSet.add(q);
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
        for (Question q : this.values())
        {
            r += (begin) ?"" :", ";
            r += q.toString();
            begin = false;
        }
        return r + "}";
    }
}
