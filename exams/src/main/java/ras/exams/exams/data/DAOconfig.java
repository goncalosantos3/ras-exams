package ras.exams.exams.data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class DAOconfig {

    @Value("${spring.datasource.username}")
    private String USERNAME;
    
    public String getUSERNAME() {
        return USERNAME;
    }

    @Value("${spring.datasource.password}") 
    private String PASSWORD;
    
    public String getPASSWORD() {
        return PASSWORD;
    }

    @Value("${spring.datasource.url}") 
    private String URL;
    
    public String getURL() {
        return URL;
    }

    @Value("${default_db_url}")
    private String INITIAL_URL;

    public String getINITIAL_URL() {
        return INITIAL_URL;
    }

    public DAOconfig(){}

}
