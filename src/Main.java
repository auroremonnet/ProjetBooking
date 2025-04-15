import controller.BookingController;
import view.MainView;
import view.AdminDashboardView;

import java.sql.Connection;
import java.sql.DriverManager;

public class Main {
    public static void main(String[] args) {
        try {
            Connection conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/booking_db", "root", ""
            );
            new AdminDashboardView(conn);

            BookingController controller = new BookingController(conn);
            new MainView(controller);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
