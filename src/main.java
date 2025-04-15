import controller.BookingController;
import view.MainView;

import java.sql.Connection;
import java.sql.DriverManager;

class Main {
    public static void main(String[] args) {
        try {
            Connection conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/booking_db", "root", ""
            );

            BookingController controller = new BookingController(conn);
            new MainView(controller);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}