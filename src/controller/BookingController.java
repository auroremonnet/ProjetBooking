// controller/BookingController.java
package controller;

import dao.HebergementDAO;
import dao.ReservationDAO;
import model.Hebergement;
import model.Reservation;

import java.sql.Connection;
import java.time.LocalDate;
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

    // 🔍 Tous les hébergements
    public List<Hebergement> listerTous() throws Exception {
        return hebergementDAO.getAllHebergements();
    }

    // 🔍 Recherche par lieu, catégorie, prix max
    public List<Hebergement> chercher(String lieu, String categorie, double prixMax) throws Exception {
        return hebergementDAO.searchHebergements(lieu, categorie, prixMax);
    }

    // 🔍 Recherche avancée (localisation, catégorie, options)
    public List<Hebergement> rechercherAvancee(String lieu, String categorie, String options) throws Exception {
        return hebergementDAO.rechercherHebergements(lieu, categorie, options);
    }

    /**
     * Recherche des hébergements disponibles dans une destination à une période donnée,
     * en excluant ceux qui sont déjà réservés sur la période.
     */
    public List<Hebergement> rechercher(String destination,
                                        Date arrivee,
                                        Date depart,
                                        int adultes,
                                        int enfants,
                                        int chambres) throws Exception {
        LocalDate dateArr = new java.sql.Date(arrivee.getTime()).toLocalDate();
        LocalDate dateDep = new java.sql.Date(depart.getTime()).toLocalDate();

        List<Hebergement> tous = hebergementDAO.getAllHebergements();
        List<Hebergement> disponibles = new ArrayList<>();

        for (Hebergement h : tous) {
            // Vérification lieu
            boolean correspondLieu = h.getLocalisation().toLowerCase().contains(destination.toLowerCase()) ||
                    h.getNom().toLowerCase().contains(destination.toLowerCase());

            if (!destination.isEmpty() && !correspondLieu) {
                continue; // Si on a donné un lieu et qu'il ne correspond pas, on ignore
            }

            // Vérification disponibilité (par rapport aux réservations existantes)
            List<Reservation> reservationsExistantes = reservationDAO.getReservationsByHebergement(h.getIdHebergement());
            boolean estLibre = true;

            for (Reservation r : reservationsExistantes) {
                LocalDate rArrivee = r.getDateArrivee().toLocalDate();
                LocalDate rDepart = r.getDateDepart().toLocalDate();
                // Si les dates se chevauchent
                if (!(dateDep.isBefore(rArrivee) || dateArr.isAfter(rDepart))) {
                    estLibre = false;
                    break;
                }
            }

            if (estLibre) {
                disponibles.add(h);
            }
        }

        return disponibles;
    }

    // 🔄 Récupère une réservation par ID
    public Reservation getReservationParId(int id) throws Exception {
        return reservationDAO.findById(id);
    }

    // 🔄 Récupère un hébergement par son ID
    public Hebergement getHebergementParId(int id) throws Exception {
        return hebergementDAO.findById(id);
    }
}
