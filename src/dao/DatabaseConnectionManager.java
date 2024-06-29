// DatabaseConnectionManager.java
package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnectionManager {

    private static DatabaseConnectionManager instance;
    private Connection connection;

    private DatabaseConnectionManager() {
        // Private constructor to prevent instantiation from outside
    }
    // Singleton instance tkhalli instance bark mawjouda toute le cycle mtaa app
    //static maanehe njmm nestaamlou mil barra class (min ay blassa mil application)
    public static synchronized DatabaseConnectionManager getInstance() {
        if (instance == null) {
            instance = new DatabaseConnectionManager();
        }
        return instance;
    }

    public Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            // Establish the database connection
            String url = "jdbc:mysql://localhost:3306/test?useSSL=false";
            String user = "root";
            String password = "admin";
            connection = DriverManager.getConnection(url, user, password);
            //getConnection() tkhalli reusability mtaa lconnection mano93dch naawed nasnaa3 fy connection marra okhra maa lbase fi kol marra naamel query 
        }
        return connection;
    }// haja okhra thread safety fy milieu multi-thread nodhmen safety mtaa connection , instance thread-safe

    public void closeConnection() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }
}
