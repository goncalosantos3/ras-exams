package ras.exams.exams.data;

public class DAOconfig {
    static final String USERNAME = "ras";                       // Actualizar
    static final String PASSWORD = "rasexams";                       // Actualizar
    private static final String DATABASE = "ras_exams";          // Actualizar
	//private static final String DRIVER = "jdbc:mariadb";        // Usar para MariaDB
	private static final String DRIVER = "jdbc:mysql";        // Usar para MySQL
    static final String URL = DRIVER+"://localhost:3306/"+DATABASE;
}