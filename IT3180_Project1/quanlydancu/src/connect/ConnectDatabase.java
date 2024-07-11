package connect;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectDatabase implements AutoCloseable {

    static {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public String getStringURL() {
        String URL = "jdbc:postgresql://localhost:5432/nncnpm";
        return URL;
    }

    public Connection getConnectDatabase() throws SQLException {
        String URL = getStringURL();
        Connection connection = DriverManager.getConnection(URL, "postgres", "truong2??3@");
        System.out.println("Connected to the database");
        return connection;
    }

    @Override
    public void close() throws SQLException {
       
        System.out.println("Closing the database connection");
    }
}
