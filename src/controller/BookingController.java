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

    // ✅ Affiche tous les hébergements
    public List<Hebergement> listerTous() throws Exception {
        return hebergementDAO.getAllHebergements();
    }

    // ✅ Recherche simple : lieu, catégorie, prix max
    public List<Hebergement> chercher(String lieu, String categorie, double prixMax) throws Exception {
        return hebergementDAO.searchHebergements(lieu, categorie, prixMax);
    }

    // ✅ Recherche avancée : localisation, catégorie, options
    public List<Hebergement> rechercherAvancee(String localisation, String categorie, String options) throws Exception {
        return hebergementDAO.rechercherHebergements(localisation, categorie, options);
    }

    // ✅ Recherche réservation par ID
    public Reservation getReservationParId(int id) throws Exception {
        return reservationDAO.findById(id);
    }
}
