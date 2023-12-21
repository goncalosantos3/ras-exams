package ras.exams.exams.data;

import java.sql.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import ras.exams.exams.model.Question;

public class QuestionDAO implements Map<UUID, Question> {

    private static QuestionDAO singleton = null;

    private QuestionDAO()
    {
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD); Statement stm = conn.createStatement())
        {
            String sql = "CREATE TABLE IF NOT EXISTS `question` ("+
                            "`questionID` BINARY(16) NOT NULL," +
                            "`questionType` VARCHAR(45) NULL DEFAULT NULL," +
                            "`question` VARCHAR(200) NULL DEFAULT NULL," +
                            "`versionID` BINARY(16) NULL DEFAULT NULL," +
                            "PRIMARY KEY (`questionID`)," +
                            "INDEX `version_idx` (`versionID` ASC) VISIBLE," +
                            "CONSTRAINT `version`" +
                                "FOREIGN KEY (`versionID`)" +
                                "REFERENCES `ras_exams`.`examversion` (`examVersionID`))" +
                        "ENGINE = InnoDB" +
                        "DEFAULT CHARACTER SET = utf8mb4" +
                        "COLLATE = utf8mb4_0900_ai_ci";
            stm.executeUpdate(sql);

            sql = "CREATE TABLE IF NOT EXISTS `completespacesquestion` (" +
                    "`questionID` BINARY(16) NOT NULL," +
                    "`text` VARCHAR(300) NULL DEFAULT NULL COMMENT" +
                    "PRIMARY KEY (`questionID`)," +
                    "CONSTRAINT `questionIDcs`" +
                    "FOREIGN KEY (`questionID`)" +
                    "REFERENCES `ras_exams`.`question` (`questionID`))" +
                "ENGINE = InnoDB" +
                "DEFAULT CHARACTER SET = utf8mb4" +
                "COLLATE = utf8mb4_0900_ai_ci";

            stm.executeUpdate(sql);

            sql = "CREATE TABLE IF NOT EXISTS `multiplechoicequestion` (" +
                    "`questionID` BINARY(16) NOT NULL," +
                    "`choiceID` BINARY(16) NOT NULL," +
                    "`choiceNumber` INT NOT NULL," +
                    "`text` VARCHAR(256) NULL DEFAULT NULL," +
                    "`correction` TINYINT NULL DEFAULT NULL," +
                    "`score` INT NULL DEFAULT NULL," +
                    "PRIMARY KEY (`questionID`, `choiceID`)," +
                    "CONSTRAINT `questionIDmc`" +
                    "  FOREIGN KEY (`questionID`)" +
                    "  REFERENCES `ras_exams`.`question` (`questionID`))" +
                "ENGINE = InnoDB" +
                "DEFAULT CHARACTER SET = utf8mb4" +
                "COLLATE = utf8mb4_0900_ai_ci";

            stm.executeUpdate(sql);

            sql = "CREATE TABLE IF NOT EXISTS `trueorfalsequestion` (" +
                    "`questionID` BINARY(16) NOT NULL," +
                    "`optionNumber` INT NOT NULL," +
                    "`description` VARCHAR(256) NULL DEFAULT NULL," +
                    "`correction` TINYINT NULL DEFAULT NULL," +
                    "`score` INT NULL DEFAULT NULL," +
                    "PRIMARY KEY (`questionID`, `optionNumber`)," +
                    "CONSTRAINT `questionIDtf`" +
                    "  FOREIGN KEY (`questionID`)" +
                    "  REFERENCES `ras_exams`.`question` (`questionID`))" +
                "ENGINE = InnoDB" +
                "DEFAULT CHARACTER SET = utf8mb4" +
                "COLLATE = utf8mb4_0900_ai_ci";

            stm.executeUpdate(sql);

            sql = "CREATE TABLE IF NOT EXISTS `writingquestion` (" +
                    "`questionID` BINARY(16) NOT NULL," +
                    "`criteria` VARCHAR(200) NULL DEFAULT NULL," +
                    "`minimumLimit` INT NULL DEFAULT NULL," +
                    "`maximumLimit` INT NULL DEFAULT NULL," +
                    "PRIMARY KEY (`questionID`)," +
                    "CONSTRAINT `questionIDw`" +
                    "  FOREIGN KEY (`questionID`)" +
                    "  REFERENCES `ras_exams`.`question` (`questionID`))" +
                "ENGINE = InnoDB" +
                "DEFAULT CHARACTER SET = utf8mb4" +
                "COLLATE = utf8mb4_0900_ai_ci";

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

    @Override
    public void clear() {
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD); Statement stm = conn.createStatement())
        {
            stm.executeUpdate("TRUNCATE question");
            stm.executeUpdate("TRUNCATE completespacesquestion");
            stm.executeUpdate("TRUNCATE multiplechoicequestion");
            stm.executeUpdate("TRUNCATE trueorfalsequestion");
            stm.executeUpdate("TRUNCATE writingquestion");
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
    }

    @Override
    public boolean containsKey(Object arg0) {
        boolean r;
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery("SELECT questionID FROM question WHERE questionID='"+arg0.toString()+"'"))
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
    public boolean containsValue(Object arg0) {
        Question q = (Question) arg0;
        return this.containsKey(q.getQuestionId());
    }

    @Override
    public Set<Entry<UUID, Question>> entrySet() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'entrySet'");
    }

    @Override
    public Question get(Object arg0) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'get'");
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
            ResultSet rs = stm.executeQuery("SELECT questionID FROM question"))
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

    @Override
    public Question put(UUID arg0, Question arg1) {
        Question rv = this.get(arg0);
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD); Statement stm = conn.createStatement())
        {
            // preciso que adicionemos o tipo
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return rv;
    }

    @Override
    public void putAll(Map<? extends UUID, ? extends Question> arg0) {
        for (Entry<? extends UUID, ? extends Question> entry : arg0.entrySet())
        {
            this.put(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public Question remove(Object arg0) {
        Question rv = this.get(arg0);
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
            Statement stm = conn.createStatement())
        {
            stm.executeUpdate("DELETE FROM question WHERE questionID='"+arg0.toString()+"'");
            stm.executeUpdate("DELETE FROM completespacesquestion WHERE questionID='"+arg0.toString()+"'");
            stm.executeUpdate("DELETE FROM multiplechoicequestion WHERE questionID='"+arg0.toString()+"'");
            stm.executeUpdate("DELETE FROM trueorfalsequestion WHERE questionID='"+arg0.toString()+"'");
            stm.executeUpdate("DELETE FROM writingquestion WHERE questionID='"+arg0.toString()+"'");
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

    @Override
    public Collection<Question> values() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'values'");
    }
    
}
