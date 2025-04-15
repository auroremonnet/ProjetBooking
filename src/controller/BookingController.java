package controller;

import dao.HebergementDAO;
import dao.UtilisateurDAO;
import dao.ReservationDAO;
import dao.ReductionDAO;

import model.Hebergement;
import model.Reservation;
import model.Utilisateur;
import model.Reduction;

import java.sql.Connection;
import java.util.List;

public class BookingController {
    private HebergementDAO hebergementDAO;
    private UtilisateurDAO utilisateurDAO;
    private ReservationDAO reservationDAO;
    private ReductionDAO reductionDAO;

    public BookingController(Connection connection) {
        this.hebergementDAO = new HebergementDAO(connection);
        this.utilisateurDAO = new UtilisateurDAO(connection);
        this.reservationDAO = new ReservationDAO(connection);
        this.reductionDAO = new ReductionDAO(connection);
    }

    // === Hebergements ===
    public List<Hebergement> getTousLesHebergements() throws Exception {
        return hebergementDAO.getAllHebergements();
    }

    public List<Hebergement> chercherHebergements(String lieu, String categorie, double prixMax) throws Exception {
        return hebergementDAO.searchHebergements(lieu, categorie, prixMax);
    }

    // === Utilisateurs ===
    public Utilisateur connexion(String email, String motDePasse) throws Exception {
        return utilisateurDAO.login(email, motDePasse);
    }

    public boolean inscription(Utilisateur u) throws Exception {
        return utilisateurDAO.register(u);
    }

    // === Réservations ===
    public boolean reserver(Reservation r) throws Exception {
        return reservationDAO.createReservation(r);
    }

    public List<Reservation> getReservationsUtilisateur(int idUtilisateur) throws Exception {
        return reservationDAO.getReservationsByUser(idUtilisateur);
    }

    // === Réductions ===
    public double calculerReduction(int idUtilisateur) throws Exception {
        return reductionDAO.getReductionPourClient(idUtilisateur);
    }
}
