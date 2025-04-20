import controller.BookingController;
import view.AccueilView;

import java.sql.Connection;
import java.sql.DriverManager;
public class Main {
    public static void main(String[] args) {
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/booking_db", "root", "");
            new AccueilView(conn);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
