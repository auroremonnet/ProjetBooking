import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class TestConnection {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/booking_db?useSSL=false";
        String user = "root";
        String password = ""; // Remplacez par votre mot de passe si nécessaire

        try {
            Connection conn = DriverManager.getConnection(url, user, password);
            if (conn != null) {
                System.out.println("Connexion réussie !");
                conn.close();
            }
        } catch (SQLException ex) {
            System.out.println("Erreur de connexion à la base de données !");
            ex.printStackTrace();
        }
    }
}
