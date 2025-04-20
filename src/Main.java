import controller.AuthController;
import model.Client;
import util.DBConnection;
import view.AuthView;

import javax.swing.*;
import java.sql.Connection;

public class Main {
    public static void main(String[] args) {
        try {
            Connection conn = DBConnection.getConnection();
            AuthController authController = new AuthController(conn);
            SwingUtilities.invokeLater(() -> {
                AuthView authView = new AuthView(authController, conn);  // ajout du paramètre conn
                authView.setVisible(true);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
