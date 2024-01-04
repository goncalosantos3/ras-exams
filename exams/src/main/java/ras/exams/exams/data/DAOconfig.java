package ras.exams.exams.data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class DAOconfig {

    @Autowired
    private Environment env;

    public String getUSERNAME() {
        return env.getProperty("spring.datasource.username");
    }

    public String getPASSWORD() {
        return env.getProperty("spring.datasource.password");
    }

    public String getURL() {
        return env.getProperty("spring.datasource.url");
    }

    public String getINITIAL_URL() {
        return env.getProperty("default_db_url");
    }
}
