package controller;

import dao.HebergementDAO;
import dao.ReservationDAO;
import model.Hebergement;
import model.Reservation;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BookingController {
    private final HebergementDAO hebergementDAO;
    private final ReservationDAO reservationDAO;

    public BookingController(Connection connection) {
        this.hebergementDAO = new HebergementDAO(connection);
        this.reservationDAO = new ReservationDAO(connection);
    }

    // ✅ Affiche tous les hébergements (utilisé dans MainView)
    public List<Hebergement> listerTous() throws Exception {
        return hebergementDAO.getAllHebergements();
    }

    // ✅ Recherche simple (prix, lieu, catégorie)
    public List<Hebergement> chercher(String lieu, String categorie, double prixMax) throws Exception {
        return hebergementDAO.searchHebergements(lieu, categorie, prixMax);
    }

    // ✅ Recherche avancée avec options (wifi, jardin, etc.)
    public List<Hebergement> rechercherAvancee(String lieu, String categorie, String options) throws Exception {
        return hebergementDAO.rechercherHebergements(lieu, categorie, options);
    }

    // ✅ Recherche depuis l'écran d'accueil
    public List<Hebergement> rechercher(String destination, Date arrivee, Date depart, int adultes, int enfants, int chambres) throws Exception {
        List<Hebergement> tous = hebergementDAO.getAllHebergements();

        List<Hebergement> filtres = new ArrayList<>();
        for (Hebergement h : tous) {
            boolean correspond = h.getNom().toLowerCase().contains(destination.toLowerCase()) ||
                    h.getLocalisation().toLowerCase().contains(destination.toLowerCase());

            // Vous pouvez ajouter ici d'autres critères comme :
            // - h.getCapacite() >= adultes + enfants
            // - h.getNbChambres() >= chambres
            // - disponibilité entre arrivee et depart (si vous avez cette logique)

            if (correspond) {
                filtres.add(h);
            }
        }

        return filtres;
    }

    // (Optionnel) Recherche de réservation par ID
    public Reservation getReservationParId(int id) throws Exception {
        return reservationDAO.findById(id);
    }
}
