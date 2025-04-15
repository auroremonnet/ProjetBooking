package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class DBConnection {
    public static Connection getConnection() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/booking_db"; // Adaptez selon votre configuration
        String user = "root";     // Remplacez par votre nom d'utilisateur
        String password = "";    // Remplacez par votre mot de passe
        Connection conn = DriverManager.getConnection(url, user, password);
        conn.setAutoCommit(true); // Activez le commit automatique ou gérez les transactions manuellement
        return conn;
    }
}
