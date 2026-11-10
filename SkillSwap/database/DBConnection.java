
package database;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {

    public static Connection getConnection() {
        Connection con = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            con = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/skillswap",
                "root",
                "November02@"
            );

        } catch (Exception e) {
            System.out.println("Database connection failed: " + e.getMessage());
        }

        return con;
    }
}
