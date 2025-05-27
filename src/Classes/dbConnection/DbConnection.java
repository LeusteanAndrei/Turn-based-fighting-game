package Classes.dbConnection;

import java.sql.Connection;

public class DbConnection {

    private static final String URL = "jdbc:mysql://localhost:3306/fate";
    private static final String USER = "root";
    private static final String PASSWORD = "7704";



    private static Connection connection;


    public DbConnection() {
        try {
            connection = java.sql.DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static Connection getConnection() {
            return connection;
    }
}
