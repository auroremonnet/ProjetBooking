package controller;

import dao.HebergementDAO;
import dao.ReservationDAO;
import model.Hebergement;
import model.Reservation;

import java.sql.Connection;
import java.util.List;

public class BookingController {
    private final HebergementDAO hebergementDAO;
    private final ReservationDAO reservationDAO;

    public BookingController(Connection connection) {
        this.hebergementDAO = new HebergementDAO(connection);
        this.reservationDAO = new ReservationDAO(connection);
    }

    // ✅ Méthode utilisée par MainView pour afficher tous les hébergements
    public List<Hebergement> listerTous() throws Exception {
        return hebergementDAO.getAllHebergements();
    }

    // ✅ Méthode utilisée par MainView pour filtrer la recherche
    public List<Hebergement> chercher(String lieu, String categorie, double prixMax) throws Exception {
        return hebergementDAO.searchHebergements(lieu, categorie, prixMax);
    }

    // (Optionnel pour tests ou autres interfaces)
    public Reservation getReservationParId(int id) throws Exception {
        return reservationDAO.findById(id);
    }
}
