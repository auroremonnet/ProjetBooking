import view.AuthDialog;
import view.MainView;
import controller.AuthController;
import controller.BookingController;
import util.DBConnection;

import javax.swing.SwingUtilities;
import java.sql.Connection;

public class Main {
    public static void main(String[] args) {
        try {
            Connection conn = DBConnection.getConnection();
            AuthController authController = new AuthController(conn);

            SwingUtilities.invokeLater(() -> {
                AuthDialog dialog = new AuthDialog(null, authController);
                dialog.setVisible(true);

                // Si login réussi, on ouvre MainView
                if (dialog.isSucceeded()) {
                    BookingController bookingController = new BookingController(conn);
                    MainView mainView = new MainView(bookingController);
                    mainView.setVisible(true);
                } else {
                    System.exit(0);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
