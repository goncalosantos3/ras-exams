package ras.exams.exams.data;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import ras.exams.exams.model.ExamVersion;
import ras.exams.exams.model.Exam;
import ras.exams.exams.model.ExamAnswer;
import ras.exams.exams.model.ExamHeader;

@Component
public class ExamDAO implements Map<UUID, Exam> {

    private String USERNAME;
    private String PASSWORD;
    private String URL;
    
    @Autowired
    private ExamHeaderDAO examHeaderDAO;
    @Autowired
    private ExamVersionDAO examVersionDAO;
    @Autowired
    private ExamAnswerDAO examAnswerDAO;

    public ExamDAO(
        @Value("${spring.datasource.username}") String USERNAME,
        @Value("${spring.datasource.password}") String PASSWORD,
        @Value("${spring.datasource.url}") String URL,
        @Value("${default_db_url}") String INITIAL_URL
    )
    {
        this.USERNAME = USERNAME;
        this.PASSWORD = PASSWORD;
        this.URL = URL;

        try (Connection conn = DriverManager.getConnection(INITIAL_URL, USERNAME, PASSWORD); Statement stm = conn.createStatement())
        {
            stm.executeUpdate("CREATE SCHEMA IF NOT EXISTS `ras_exams`");
        }
        catch(SQLException e)
        {
            // Erro a criar BD
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }

        try (Connection conn = DriverManager.getConnection(INITIAL_URL, USERNAME, PASSWORD); Statement stm = conn.createStatement())
        {
            String sql = 
            """
            CREATE TABLE IF NOT EXISTS `ras_exams`.`exam` (
                `examID` BINARY(16) NOT NULL,
                `teacherID` BINARY(16) NOT NULL,
                PRIMARY KEY (`examID`))
            """;
            stm.executeUpdate(sql);

            sql = 
            """
            CREATE TABLE IF NOT EXISTS `ras_exams`.`enrolledstudents` (
                `studentID` BINARY(16) NOT NULL,
                `examID` BINARY(16) NOT NULL,
                PRIMARY KEY (`studentID`, `examID`),
                INDEX `enrolledExam_idx` (`examID` ASC) VISIBLE,
                CONSTRAINT `enrolledExam`
                    FOREIGN KEY (`examID`)
                    REFERENCES `ras_exams`.`exam` (`examID`))
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

    private Exam getExam (ResultSet rs) throws SQLException
    {
        UUID examID = UUID.fromString(rs.getString("examID"));
        String teacherID = rs.getString("teacherID");
        List<String> enrolled = this.getEnrolledStudents(examID);
        ExamHeader header = this.examHeaderDAO.get(examID);
        List<ExamVersion> versionsList = this.examVersionDAO.getExamVersionsFromExam(examID);
        Map<UUID,ExamVersion> versions = new HashMap<>();
        versionsList.forEach(v -> versions.put(v.getVersionId(), v));
        List<ExamAnswer> answersList = this.examAnswerDAO.getExamAnswersFromExam(examID);
        Map<UUID,ExamAnswer> answers = new HashMap<>();
        answersList.forEach(a -> answers.put(a.getStudentID(), a));

        return new Exam(examID, teacherID, enrolled, header, versions, answers);
    }

    @Override
    public void clear() {
        try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD); Statement stm = conn.createStatement())
        {
            stm.execute("SET FOREIGN_KEY_CHECKS=0");
            stm.executeUpdate("TRUNCATE exam");
            stm.executeUpdate("TRUNCATE enrolledstudents");
            this.examHeaderDAO.clear();
            this.examVersionDAO.clear();
            this.examAnswerDAO.clear();
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
        try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery("SELECT examID FROM exam WHERE examID=UUID_TO_BIN('"+key.toString()+"')"))
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
    public boolean containsValue(Object value) {
        Exam v = (Exam) value;
        return this.containsKey(v.getID());
    }

    @Override
    public Set<Entry<UUID, Exam>> entrySet() {
        Set<Entry<UUID, Exam>> rSet = new HashSet<>();
        try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery("SELECT BIN_TO_UUID(examID) as examID, BIN_TO_UUID(teacherID) as teacherID FROM exam"))
        {
            while(rs.next())
            {
                Exam e = this.getExam(rs);
                rSet.add(Map.entry(e.getID(), e));
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return rSet;
    }

    public List<String> getEnrolledStudents(UUID examID)
    {
        List<String> enrolledStudents = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery("SELECT BIN_TO_UUID(studentID) as id FROM enrolledstudents WHERE examID=UUID_TO_BIN('"+examID.toString()+"')"))
        {
            while(rs.next())
            {
                enrolledStudents.add(rs.getString("id"));
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return enrolledStudents;
    }

    @Override
    public Exam get(Object key) {
        if (!(key instanceof UUID))
            return null;
        Exam a = null;
        try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery("SELECT BIN_TO_UUID(examID) as examID, BIN_TO_UUID(teacherID) as teacherID from exam WHERE examID=UUID_TO_BIN('"+key.toString()+"')"))
        {
            if (rs.next())
            {
                a = this.getExam(rs);
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
        try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery("SELECT BIN_TO_UUID(examID) as examID FROM exam"))
        {
            while (rs.next())
            {
                r.add(UUID.fromString(rs.getString("examID")));
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return r;
    }

    private void putEnrolled(UUID examID, List<String> enrolled)
    {
        try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD); Statement stm = conn.createStatement())
        {
            for (String studentID : enrolled)
            {
                String sql = "INSERT INTO enrolledstudents "+
                                "VALUES("+
                                    "UUID_TO_BIN('"+studentID+"'),"+
                                    "UUID_TO_BIN('"+examID.toString()+"')"+
                                ") ON DUPLICATE KEY UPDATE "+
                                    "studentID=VALUES(studentID),"+
                                    "examID=VALUES(examID)";
                stm.executeUpdate(sql);
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
    }

    @Override
    public Exam put(UUID key, Exam value) {
        Exam rv = this.get(key);
        try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD); 
            Statement stm = conn.createStatement())
        {
            stm.executeUpdate("INSERT INTO exam "+
                                "VALUES ("+
                                    "UUID_TO_BIN('"+key.toString()+"'),"+
                                    "UUID_TO_BIN('"+value.getTeacherID()+"')"+
                                    ") ON DUPLICATE KEY UPDATE "+
                                    "examID=VALUES(examID),"+
                                    "teacherID=VALUES(teacherID)");
            ExamHeader header = value.getHeader();
            List<String> enrolled = value.getEnrolled();
            Map<UUID, ExamVersion> versions = value.getVersions();
            Map<UUID, ExamAnswer> answers = value.getAnswers();

            if (rv != null && !rv.getHeader().getExamHeaderID().equals(header.getExamHeaderID()))
                this.examHeaderDAO.remove(rv.getHeader().getExamHeaderID());
            this.examHeaderDAO.put(header);
            this.putEnrolled(header.getExamID(), enrolled);
            this.examVersionDAO.putAll(versions.values());
            this.examAnswerDAO.putAll(answers.values());
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return rv;
    }

    @Override
    public void putAll(Map<? extends UUID, ? extends Exam> m) {
        for (Entry<? extends UUID, ? extends Exam> entry : m.entrySet())
        {
            this.put(entry.getKey(), entry.getValue());
        }
    }

    public void removeEnrolled(UUID examID, List<String> enrolled)
    {
        try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD); Statement stm = conn.createStatement())
        {
            stm.execute("SET FOREIGN_KEY_CHECKS=0");
            for (String studentID : enrolled)
                stm.executeUpdate("DELETE FROM enrolledstudents WHERE examID=UUID_TO_BIN('"+examID.toString()+"') AND studentID=UUID_TO_BIN('"+studentID+"')");
            stm.execute("SET FOREIGN_KEY_CHECKS=1");
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
    }

    @Override
    public Exam remove(Object key) {
        if (!(key instanceof UUID))
            return null;
        Exam rv = this.get(key);
        try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            Statement stm = conn.createStatement())
        {
            stm.execute("SET FOREIGN_KEY_CHECKS=0");
            stm.executeUpdate("DELETE FROM exam WHERE examID=UUID_TO_BIN('"+key.toString()+"')");
            if (rv != null)
            {
                this.removeEnrolled(rv.getID(), rv.getEnrolled());
                this.examHeaderDAO.remove(rv.getHeader().getExamHeaderID());
                for (ExamVersion version : rv.getVersions().values())
                    this.examVersionDAO.remove(version.getVersionId());
                for (ExamAnswer answer : rv.getAnswers().values())
                    this.examAnswerDAO.remove(answer.getExamAnswerId());
            }
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
        try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery("SELECT count(*) FROM exam"))
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
    public Collection<Exam> values() {
        Set<Exam> rSet = new HashSet<>();
        try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery(
            """
            SELECT BIN_TO_UUID(examID) as examID,
                    BIN_TO_UUID(teacherID) as teacherID
            FROM exam"""))
        {
            while(rs.next())
            {
                rSet.add(this.getExam(rs));
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
        for (Map.Entry<UUID, Exam> entry : this.entrySet())
        {
            r += (begin) ?"" :", ";
            r += entry.getKey() + "=" + entry.getValue().getID();
            begin = false;
        }
        return r + "}";
    }
}
