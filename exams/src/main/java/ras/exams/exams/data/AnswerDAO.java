package ras.exams.exams.data;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import ras.exams.exams.model.Answer;
import ras.exams.exams.model.ChoiceAnswer;
import ras.exams.exams.model.CompleteSpacesAnswer;
import ras.exams.exams.model.MultipleChoiceAnswer;
import ras.exams.exams.model.TOFQAnswer;
import ras.exams.exams.model.TrueOrFalseAnswer;
import ras.exams.exams.model.WritingAnswer;

@Component
public class AnswerDAO {

    private String USERNAME;
    private String PASSWORD;
    private String URL;

    @Autowired
    private QuestionDAO questionDAO;

    public AnswerDAO(
        @Value("${spring.datasource.username}") String USERNAME,
        @Value("${spring.datasource.password}") String PASSWORD,
        @Value("${spring.datasource.url}") String URL
    )
    {
        this.USERNAME = USERNAME;
        this.PASSWORD = PASSWORD;
        this.URL = URL;

        try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD); Statement stm = conn.createStatement())
        {
            String sql = 
            """
            CREATE TABLE IF NOT EXISTS `ras_exams`.`answer` (
                `answerID` BINARY(16) NOT NULL,
                `examAnswerID` BINARY(16) NOT NULL,
                `questionID` BINARY(16) NOT NULL,
                `answerType` CHAR(1) NOT NULL,
                `grade` INT NULL DEFAULT NULL,
                PRIMARY KEY (`answerID`),
                INDEX `answer_idx` (`answerID` ASC) VISIBLE,
                CONSTRAINT `answer`
                    FOREIGN KEY (`questionID`)
                    REFERENCES `ras_exams`.`question` (`questionID`),
                CONSTRAINT `examAnswer`
                    FOREIGN KEY (`examAnswerID`)
                    REFERENCES `ras_exams`.`examanswer` (`examAnswerID`))
            """;
            stm.executeUpdate(sql);

            sql = 
            """
            CREATE TABLE IF NOT EXISTS `ras_exams`.`answermultiplechoice` (
                `answerID` BINARY(16) NOT NULL,
                `questionID` BINARY(16) NOT NULL,
                `choiceNumber` INT NOT NULL,
                `selected` TINYINT NULL DEFAULT NULL,
                `grade` INT NULL DEFAULT NULL,
                PRIMARY KEY (`answerID`, `questionID`, `choiceNumber`),
                INDEX `answerMultipleChoice_idx` (`questionID` ASC, `choiceNumber` ASC) VISIBLE,
                CONSTRAINT `answerMultipleChoice`
                    FOREIGN KEY (`questionID` , `choiceNumber`)
                    REFERENCES `ras_exams`.`multiplechoicequestion` (`questionID` , `choiceNumber`),
                CONSTRAINT `examAnswerMC`
                    FOREIGN KEY (`answerID`)
                    REFERENCES `ras_exams`.`answer` (`answerID`))
            """;
                
            stm.executeUpdate(sql);
            
            sql = 
            """
            CREATE TABLE IF NOT EXISTS `ras_exams`.`answercompletespaces` (
                `answerID` BINARY(16) NOT NULL,
                `questionID` BINARY(16) NOT NULL,
                `text` VARCHAR(2048) NULL DEFAULT NULL,
                `grade` INT NULL DEFAULT NULL,
                PRIMARY KEY (`answerID`, `questionID`),
                INDEX `answerCompleteSpaces_idx` (`questionID` ASC) VISIBLE,
                CONSTRAINT `answerCompleteSpaces`
                    FOREIGN KEY (`questionID`)
                    REFERENCES `ras_exams`.`completespacesquestion` (`questionID`),
                CONSTRAINT `examAnswerCS`
                    FOREIGN KEY (`answerID`)
                    REFERENCES `ras_exams`.`answer` (`answerID`))
            """;

            stm.executeUpdate(sql);

            sql =
            """
            CREATE TABLE IF NOT EXISTS `ras_exams`.`answertrueorfalse` (
                `answerID` BINARY(16) NOT NULL,
                `questionID` BINARY(16) NOT NULL,
                `optionNumber` INT NOT NULL,
                `answer` TINYINT NULL DEFAULT NULL,
                `grade` INT NULL DEFAULT NULL,
                PRIMARY KEY (`answerID`, `questionID`, `optionNumber`),
                INDEX `answerTrueOrFalse_idx` (`questionID` ASC, `optionNumber` ASC) VISIBLE,
                CONSTRAINT `answerTrueOrFalse`
                    FOREIGN KEY (`questionID` , `optionNumber`)
                    REFERENCES `ras_exams`.`trueorfalsequestion` (`questionID` , `optionNumber`),
                CONSTRAINT `examAnswerTF`
                    FOREIGN KEY (`answerID`)
                    REFERENCES `ras_exams`.`answer` (`answerID`))
            """;

            stm.executeUpdate(sql);
            
            sql = 
            """
            CREATE TABLE IF NOT EXISTS `ras_exams`.`answerwriting` (
                `answerID` BINARY(16) NOT NULL,
                `questionID` BINARY(16) NOT NULL,
                `text` VARCHAR(2048) NULL DEFAULT NULL,
                `grade` INT NULL DEFAULT NULL,
                PRIMARY KEY (`answerID`, `questionID`),
                INDEX `answerWriting_idx` (`questionID` ASC) VISIBLE,
                CONSTRAINT `answerWriting`
                    FOREIGN KEY (`questionID`)
                    REFERENCES `ras_exams`.`writingquestion` (`questionID`),
                CONSTRAINT `examAnswerW`
                    FOREIGN KEY (`answerID`)
                    REFERENCES `ras_exams`.`answer` (`answerID`))
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
            
    private List<ChoiceAnswer> getChoiceAnswers(UUID answerID, UUID questionID)
    {
        List<ChoiceAnswer> choices = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery(
            """
            SELECT  BIN_TO_UUID(questionID) as questionID,
                    choiceNumber,
                    selected,
                    grade
            FROM answermultiplechoice 
            WHERE answerID=UUID_TO_BIN('"""+answerID.toString()+"') AND " +
                    "questionID=UUID_TO_BIN('"+questionID.toString()+"')"))
        {
            while(rs.next())
            {
                int choiceNumber = rs.getInt("choiceNumber");
                boolean selected = rs.getBoolean("selected");
                int grade = rs.getInt("grade");
                choices.add(new ChoiceAnswer(selected, grade, this.questionDAO.getChoice(questionID, choiceNumber)));
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        choices.sort((c1, c2) -> c1.getChoice().getChoiceNumber() - c2.getChoice().getChoiceNumber() );
        return choices;
    }
        
    private List<TOFQAnswer> getTOFQAnswers (UUID answerID, UUID questionID)
    {
        List<TOFQAnswer> tofqs = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery(
            """
            SELECT  optionNumber,
                    answer,
                    grade
            FROM answertrueorfalse 
            WHERE answerID=UUID_TO_BIN('"""+answerID.toString()+"') AND " +
                    "questionID=UUID_TO_BIN('"+questionID.toString()+"')"))
        {
            while(rs.next())
            {
                boolean answer = rs.getBoolean("answer");
                int grade = rs.getInt("grade");
                int optionNumber = rs.getInt("optionNumber");
                tofqs.add(new TOFQAnswer(grade, answer, this.questionDAO.getTOFQ(questionID, optionNumber)));
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        tofqs.sort((t1, t2) -> t1.getOption().getOptionNumber() - t2.getOption().getOptionNumber());
        return tofqs;
    }
        
    private WritingAnswer getWritingAnswer (UUID answerID, UUID examAnswerID, UUID questionID)
    {
        WritingAnswer w = null;
        try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        Statement stm = conn.createStatement();
        ResultSet rs = stm.executeQuery(
        """
            SELECT text, grade
            FROM answerwriting
            WHERE answerID=UUID_TO_BIN('"""+answerID.toString()+"')"
            ))
        {
            if (rs.next())
            {
                String text = rs.getString("text");
                int grade = rs.getInt("grade");
                w = new WritingAnswer(answerID, grade, examAnswerID, text, this.questionDAO.get(questionID));
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return w;
    }

    private CompleteSpacesAnswer getCompleteSpacesAnswer (UUID answerID, UUID examAnswerID, UUID questionID)
    {
        CompleteSpacesAnswer c = null;
        try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery("SELECT text, grade FROM answercompletespaces WHERE answerID=UUID_TO_BIN('"+
                                                                            answerID.toString()+"')"))
        {
            if (rs.next())
            {
                String text = rs.getString("text");
                int grade = rs.getInt("grade");
                c = new CompleteSpacesAnswer(answerID, grade, examAnswerID, text, this.questionDAO.get(questionID));
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }   
        return c;
    }
        
    private Answer getAnswer(ResultSet rs)
    {
        Answer a = null;
        try 
        {    
            UUID answerID = UUID.fromString(rs.getString("answerID"));
            UUID examAnserID = UUID.fromString(rs.getString("examAnswerID"));
            UUID questionID = UUID.fromString(rs.getString("questionID"));
            String questionType = rs.getString("answerType");
            int grade = rs.getInt("grade");

            switch (questionType) {
                case "C":
                    a = this.getCompleteSpacesAnswer(answerID, examAnserID, questionID);
                    break;
                case "M":
                    a = new MultipleChoiceAnswer(answerID, 
                                                grade, 
                                                examAnserID, 
                                                this.getChoiceAnswers(answerID, questionID),
                                                questionID);
                    break;
                case "T":
                    a = new TrueOrFalseAnswer(answerID, 
                                                grade, 
                                                examAnserID, 
                                                this.getTOFQAnswers(answerID, questionID),
                                                questionID);
                    break;
                case "W":
                    a = this.getWritingAnswer(answerID, examAnserID, questionID);
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
        return a;
    }

    
    public void clear() {
        try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD); Statement stm = conn.createStatement())
        {
            stm.execute("SET FOREIGN_KEY_CHECKS=0");
            stm.executeUpdate("TRUNCATE answercompletespaces");
            stm.executeUpdate("TRUNCATE answermultiplechoice");
            stm.executeUpdate("TRUNCATE answertrueorfalse");
            stm.executeUpdate("TRUNCATE answerwriting");
            stm.executeUpdate("TRUNCATE answer");
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
        try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery("SELECT answerID FROM answer WHERE answerID=UUID_TO_BIN('"+key.toString()+"')"))
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

    
    public boolean contains(Answer value) {
        return this.contains(value.getAnswerID());
    }

    public List<Answer> getAnswersFromExamAnswer(UUID examAnswerID)
    {
        List<Answer> questions = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery(
            """
            SELECT BIN_TO_UUID(answerID) as answerID,
                    BIN_TO_UUID(examAnswerID) as examAnswerID,
                    BIN_TO_UUID(questionID) as questionID,
                    answerType,
                    grade
            FROM answer
            WHERE examAnswerID=UUID_TO_BIN('"""+examAnswerID.toString()+"')"))
        {
            while (rs.next())
            {
                Answer a = this.getAnswer(rs);
                questions.add(a);
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return questions;
    }

    
    public Answer get(UUID key) {
        Answer a = null;
        try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery("""
            SELECT BIN_TO_UUID(answerID) as answerID,
                    BIN_TO_UUID(examAnswerID) as examAnswerID,
                    BIN_TO_UUID(questionID) as questionID,
                    answerType,
                    grade
            FROM answer
            WHERE answerID=UUID_TO_BIN('"""+key.toString()+"')"))
        {
            if (rs.next())
            {
                a = this.getAnswer(rs);
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

    private void putType(UUID answerID, Answer a, Statement stm) throws SQLException, InvalidAnswerException
    {
        char type = a.getAnswerType();
        if (type == 'C' && a instanceof CompleteSpacesAnswer)
        {
            CompleteSpacesAnswer c = (CompleteSpacesAnswer)a;
            Pattern pattern = Pattern.compile("^[^{}]*(\\{[^,} ]+(,\\s*\\d+)?\\}[^{}]*)+$", Pattern.MULTILINE);
            Matcher matcher = pattern.matcher(c.getText());
            if (!matcher.matches())
                throw new InvalidAnswerException('C');
            stm.executeUpdate("INSERT INTO answercompletespaces "+
                                "VALUES ("+
                                    "UUID_TO_BIN('"+answerID.toString()+"'),"+
                                    "UUID_TO_BIN('"+c.getQuestion().getQuestionId().toString()+"'),"+
                                    "'"+c.getText()+"',"+
                                    c.getGrade()+
                                ") ON DUPLICATE KEY UPDATE "+
                                    "answerID=VALUES(answerID),"+
                                    "questionID=VALUES(questionID),"+
                                    "text=VALUES(text),"+
                                    "grade=VALUES(grade)");    
        }
        else if (type == 'M' && a instanceof MultipleChoiceAnswer)
        {
            for (ChoiceAnswer c : ((MultipleChoiceAnswer)a).getAnswers())
            {
                stm.executeUpdate("INSERT INTO answermultiplechoice "+
                                    "VALUES ("+
                                        "UUID_TO_BIN('"+answerID.toString()+"'),"+
                                        "UUID_TO_BIN('"+a.getQuestionID().toString()+"'),"+
                                        c.getChoice().getChoiceNumber()+","+
                                        (c.getSelected() ?1 :0)+","+
                                        c.getGrade()+
                                    ") ON DUPLICATE KEY UPDATE "+
                                        "answerID=VALUES(answerID),"+
                                        "questionID=VALUES(questionID),"+
                                        "choiceNumber=VALUES(choiceNumber),"+
                                        "selected=VALUES(selected),"+
                                        "grade=VALUES(grade)");
            }
        }
        else if (type == 'T' && a instanceof TrueOrFalseAnswer)
        {
            for (TOFQAnswer o : ((TrueOrFalseAnswer)a).getAnswers())
            {
                stm.executeUpdate("INSERT INTO answertrueorfalse "+
                                    "VALUES ("+
                                        "UUID_TO_BIN('"+answerID.toString()+"'),"+
                                        "UUID_TO_BIN('"+a.getQuestionID().toString()+"'),"+
                                        o.getOption().getOptionNumber() + "," +
                                        (o.getAnswer() ?1 :0)+","+
                                        o.getGrade()+
                                    ") ON DUPLICATE KEY UPDATE "+
                                        "answerID=VALUES(answerID),"+
                                        "questionID=VALUES(questionID),"+
                                        "optionNumber=VALUES(optionNumber),"+
                                        "answer=VALUES(answer),"+
                                        "grade=VALUES(grade)");
            }
        }
        else if (type == 'W' && a instanceof WritingAnswer)
        {
            WritingAnswer w = (WritingAnswer)a;
            stm.executeUpdate("INSERT INTO answerwriting "+
                                "VALUES ("+
                                    "UUID_TO_BIN('"+answerID.toString()+"'),"+
                                    "UUID_TO_BIN('"+a.getQuestionID().toString()+"'),"+
                                    "'"+w.getText()+"',"+
                                    w.getGrade()+
                                ") ON DUPLICATE KEY UPDATE "+
                                    "answerID=VALUES(answerID),"+
                                    "questionID=VALUES(questionID),"+
                                    "text=VALUES(text),"+
                                    "grade=VALUES(grade)");    
        }
    }

    
    public Answer put(Answer value) {
        Answer rv = this.get(value.getAnswerID());
        try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD); 
            Statement stm = conn.createStatement())
        {
            stm.executeUpdate("INSERT INTO answer "+
                                "VALUES ("+
                                    "UUID_TO_BIN('"+value.getAnswerID().toString()+"'),"+
                                    "UUID_TO_BIN('"+value.getExamAnswerID().toString()+"'),"+
                                    "UUID_TO_BIN('"+value.getQuestionID().toString()+"'),"+
                                    "'"+value.getAnswerType()+"',"+
                                    value.getGrade()+
                                ") ON DUPLICATE KEY UPDATE "+
                                    "answerID=VALUES(answerID),"+
                                    "examAnswerID=VALUES(examAnswerID),"+
                                    "questionID=VALUES(questionID),"+
                                    "answerType=VALUES(answerType),"+
                                    "grade=VALUES(grade)");
            this.putType(value.getAnswerID(), value, stm);
        }
        catch (SQLException | InvalidAnswerException e)
        {
            if (rv == null)
                this.remove(value.getAnswerID());
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return rv;
    }

    
    public void putAll(Collection<Answer> l) {
        for (Answer a : l)
        {
            this.put(a);
        }
    }

    
    public Answer remove(UUID key) {
        Answer rv = this.get(key);
        try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            Statement stm = conn.createStatement())
        {
            stm.execute("SET FOREIGN_KEY_CHECKS=0");
            stm.executeUpdate("DELETE FROM answer WHERE answerID=UUID_TO_BIN('"+key.toString()+"')");
            stm.executeUpdate("DELETE FROM answercompletespaces WHERE answerID=UUID_TO_BIN('"+key.toString()+"')");
            stm.executeUpdate("DELETE FROM answermultiplechoice WHERE answerID=UUID_TO_BIN('"+key.toString()+"')");
            stm.executeUpdate("DELETE FROM answertrueorfalse WHERE answerID=UUID_TO_BIN('"+key.toString()+"')");
            stm.executeUpdate("DELETE FROM answerwriting WHERE answerID=UUID_TO_BIN('"+key.toString()+"')");
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
        try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery("SELECT count(*) FROM answer"))
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

    
    public Collection<Answer> values() {
        Set<Answer> rSet = new HashSet<>();
        try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery(
            """
            SELECT BIN_TO_UUID(answerID) as answerID,
                    BIN_TO_UUID(examAnswerID) as examAnswerID,
                    BIN_TO_UUID(questionID) as questionID,
                    answerType,
                    grade
            FROM answer"""))
        {
            while(rs.next())
            {
                Answer a = this.getAnswer(rs);
                rSet.add(a);
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
        for (Answer a : this.values())
        {
            r += (begin) ?"" :", ";
            r += a.toString();
            begin = false;
        }
        return r + "}";
    }
}
