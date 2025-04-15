package controller;

import dao.ReservationDAO; // Vérifie bien cet import
// autres imports...

public class BookingController {
    private ReservationDAO reservationDAO;

    public BookingController(ReservationDAO reservationDAO) {
        this.reservationDAO = reservationDAO;
    }

    // Exemple de méthode utilisant ReservationDAO
    public void afficherReservation(int id) {
        try {
            System.out.println(reservationDAO.findById(id));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Reste du code du contrôleur...
}
