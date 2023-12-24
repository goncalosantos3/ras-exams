package ras.exams.exams.data;

import java.sql.*;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import ras.exams.exams.model.ExamHeader;


public class ExamHeaderDAO implements Map<UUID, ExamHeader> {

    private static ExamHeaderDAO singleton = null;

    private ExamHeaderDAO()
    {
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD); Statement stm = conn.createStatement())
        {
            String sql = 
            """
            CREATE TABLE IF NOT EXISTS `ras_exams`.`examheader` (
                `examHeaderID` BINARY(16) NOT NULL,
                `examID` BINARY(16) NULL DEFAULT NULL,
                `examName` VARCHAR(64) NOT NULL,
                `examUC` VARCHAR(64) NULL,
                `examAdmissionTime` TIME NULL DEFAULT NULL,
                PRIMARY KEY (`examHeaderID`),
                INDEX `examID_idx` (`examID` ASC) VISIBLE,
                CONSTRAINT `examIDheader`
                    FOREIGN KEY (`examID`)
                    REFERENCES `ras_exams`.`exam` (`examID`))
            """;
            stm.executeUpdate(sql);

            sql = 
            """
            CREATE TABLE IF NOT EXISTS `ras_exams`.`examschedules` (
                `scheduleID` BINARY(16) NOT NULL,
                `examHeaderID` BINARY(16) NOT NULL,
                PRIMARY KEY (`scheduleID`, `examHeaderID`),
                INDEX `examSchedule_idx` (`examHeaderID` ASC) VISIBLE,
                CONSTRAINT `examSchedule`
                    FOREIGN KEY (`examHeaderID`)
                    REFERENCES `ras_exams`.`examheader` (`examHeaderID`))
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
            
    public static ExamHeaderDAO getInstance()
    {
        if (ExamHeaderDAO.singleton == null)
        {
            ExamHeaderDAO.singleton = new ExamHeaderDAO();
        }
        return ExamHeaderDAO.singleton;
    }

    private List<String> getHeaderScheduleIDs (UUID examHeaderID)
    {
        List<String> schedule = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery("SELECT BIN_TO_UUID(scheduleID) as id FROM examschedules WHERE examHeaderID=UUID_TO_BIN('"+examHeaderID.toString()+"')"))
        {
            while(rs.next())
            {
                schedule.add(rs.getString("id"));
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return schedule;
    }

    private ExamHeader getExamHeader (ResultSet rs) throws SQLException
    {
        UUID examHeaderID = UUID.fromString(rs.getString("examHeaderID")),
                examID = UUID.fromString(rs.getString("examID"));
        Time examAdmissionTimeSQL = rs.getTime("examAdmissionTime");
        String examAdmissionTime = (examAdmissionTimeSQL==null) ?(null) :examAdmissionTimeSQL.toString();
        String examName = rs.getString("examName"),
                examUC = rs.getString("examUC");
        return new ExamHeader(  examHeaderID, examID, examName, examUC, 
                                examAdmissionTime, 
                                this.getHeaderScheduleIDs(examHeaderID));
    }

    @Override
    public void clear() {
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD); Statement stm = conn.createStatement())
        {
            stm.execute("SET FOREIGN_KEY_CHECKS=0");
            stm.executeUpdate("TRUNCATE examheader");
            stm.executeUpdate("TRUNCATE examschedules");
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
            ResultSet rs = stm.executeQuery("SELECT examHeaderID FROM examheader WHERE examHeaderID=UUID_TO_BIN('"+key.toString()+"')"))
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
        ExamHeader h = (ExamHeader) key;
        return this.containsKey(h.getExamHeaderID());
    }

    @Override
    public Set<Entry<UUID, ExamHeader>> entrySet() {
        Set<Entry<UUID, ExamHeader>> rSet = new HashSet<>();
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery(
            """
            SELECT BIN_TO_UUID(examHeaderID) as examHeaderID,
                    BIN_TO_UUID(examID) as examID,
                    examName,
                    examUC,
                    examAdmissionTime
            FROM examheader"""))
        {
            while(rs.next())
            {
                ExamHeader header = this.getExamHeader(rs);
                rSet.add(Map.entry(header.getExamHeaderID(), header));
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
    public ExamHeader get(Object key) {
        if (!(key instanceof UUID))
            return null;
        ExamHeader a = null;
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery("""
            SELECT BIN_TO_UUID(examHeaderID) as examHeaderID,
                    BIN_TO_UUID(examID) as examID,
                    examName,
                    examUC,
                    examAdmissionTime
            FROM examheader
            WHERE examHeaderID=UUID_TO_BIN('"""+((UUID)key).toString()+"')"))
        {
            if (rs.next())
            {
                a = this.getExamHeader(rs);
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return a;
    }

    public ExamHeader getHeaderFromExamID(UUID examID)
    {
        ExamHeader a = null;
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery("""
            SELECT BIN_TO_UUID(examHeaderID) as examHeaderID,
                    BIN_TO_UUID(examID) as examID,
                    examName,
                    examUC,
                    examAdmissionTime
            FROM examheader
            WHERE examID=UUID_TO_BIN('"""+examID.toString()+"')"))
        {
            if (rs.next())
            {
                a = this.getExamHeader(rs);
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
            ResultSet rs = stm.executeQuery("SELECT BIN_TO_UUID(examHeaderID) FROM examheader"))
        {
            while (rs.next())
            {
                UUID questionID = UUID.fromString(rs.getString("examHeaderID"));
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

    public void putSchedule(UUID examHeaderID, UUID scheduleID)
    {
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
            Statement stm = conn.createStatement())
        {
            stm.executeUpdate( "INSERT INTO examschedules"+
                                    "VALUES( UUID_TO_BIN('"+ scheduleID.toString() +"'),"+ 
                                            "UUID_TO_BIN('"+examHeaderID.toString()+"'))"+
                                    "ON DUPLICATE KEY UPDATE "+
                                        "scheduleID=VALUES(scheduleID),"+
                                        "examHeaderID=VALUES(examHeaderID)");
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
    }

    @Override
    public ExamHeader put(UUID key, ExamHeader value) {
        ExamHeader rv = this.get(key);
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD); 
            Statement stm = conn.createStatement())
        {
            stm.executeUpdate("INSERT INTO examheader "+
                                "VALUES ("+
                                    "UUID_TO_BIN('"+key.toString()+"'),"+
                                    "UUID_TO_BIN('"+value.getExamID().toString()+"'),"+
                                    "'"+value.getExamName()+"',"+
                                    ((value.getExamName() != null) ?("'"+value.getExamName()+"'") :"NULL")+","+
                                    ((value.getExamAdmissionTime() != null) ?("'"+value.getFormatedExamAdmissionTime()+"'") :"NULL")+
                                ") ON DUPLICATE KEY UPDATE "+
                                    "examHeaderID=VALUES(examHeaderID),"+
                                    "examID=VALUES(examID),"+
                                    "examName=VALUES(examName),"+
                                    "examUC=VALUES(examUC),"+
                                    "examAdmissionTime=VALUES(examAdmissionTime)");
            if (value.getExamScheduleIDs() != null)
                for (UUID id : value.getExamScheduleIDs())
                    this.putSchedule(key, id);
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return rv;
    }

    @Override
    public void putAll(Map<? extends UUID, ? extends ExamHeader> m) {
        for (Entry<? extends UUID, ? extends ExamHeader> entry : m.entrySet())
        {
            this.put(entry.getKey(), entry.getValue());
        }
    }

    public void removeSchedule(UUID scheduleID, UUID examHeaderID)
    {
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
            Statement stm = conn.createStatement())
        {
            stm.execute("SET FOREIGN_KEY_CHECKS=0");
            stm.executeUpdate("DELETE FROM examschedules WHERE examHeaderID=UUID_TO_BIN('"+examHeaderID.toString()+"')"+
                                                                "AND scheduleID=UUID_TO_BIN('"+scheduleID.toString()+"')");
            stm.execute("SET FOREIGN_KEY_CHECKS=1");
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
    }

    @Override
    public ExamHeader remove(Object key) {
        ExamHeader rv = this.get(key);
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
            Statement stm = conn.createStatement())
        {
            stm.execute("SET FOREIGN_KEY_CHECKS=0");
            stm.executeUpdate("DELETE FROM examheader WHERE examHeaderID=UUID_TO_BIN('"+key.toString()+"')");
            if (rv.getExamScheduleIDs() != null)
                for (UUID id : rv.getExamScheduleIDs())
                    this.removeSchedule(id, (UUID)key);
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
            ResultSet rs = stm.executeQuery("SELECT count(*) FROM examheader"))
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
    public Collection<ExamHeader> values() {
        Set<ExamHeader> rSet = new HashSet<>();
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery(
            """
            SELECT BIN_TO_UUID(examHeaderID) as examHeaderID,
                    BIN_TO_UUID(examID) as examID,
                    examName,
                    examUC,
                    examAdmissionTime
            FROM examheader"""))
        {
            while(rs.next())
            {
                rSet.add(this.getExamHeader(rs));
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
        for (Map.Entry<UUID, ExamHeader> entry : this.entrySet())
        {
            r += (begin) ?"" :", ";
            r += entry.getKey() + "=" + entry.getValue().getExamHeaderID();
            begin = false;
        }
        return r + "}";
    }
}
