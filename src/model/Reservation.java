package model;

import java.sql.Date;
import java.sql.Timestamp;

public class Reservation {
    private int idReservation;
    private Date dateArrivee;
    private Date dateDepart;
    private int nombreAdultes;
    private int nombreEnfants;
    private int nombreChambres;
    private int idClient;
    private int idHebergement;
    private String statut;
    private Timestamp dateReservation;

    public Reservation(int idReservation, Date dateArrivee, Date dateDepart, int nombreAdultes, int nombreEnfants,
                       int nombreChambres, int idClient, int idHebergement, String statut, Timestamp dateReservation) {
        this.idReservation = idReservation;
        this.dateArrivee = dateArrivee;
        this.dateDepart = dateDepart;
        this.nombreAdultes = nombreAdultes;
        this.nombreEnfants = nombreEnfants;
        this.nombreChambres = nombreChambres;
        this.idClient = idClient;
        this.idHebergement = idHebergement;
        this.statut = statut;
        this.dateReservation = dateReservation;
    }

    public int getIdReservation() { return idReservation; }
    public Date getDateArrivee() { return dateArrivee; }
    public Date getDateDepart() { return dateDepart; }
    public int getNombreAdultes() { return nombreAdultes; }
    public int getNombreEnfants() { return nombreEnfants; }
    public int getNombreChambres() { return nombreChambres; }
    public int getIdClient() { return idClient; }
    public int getIdHebergement() { return idHebergement; }
    public String getStatut() { return statut; }
    public Timestamp getDateReservation() { return dateReservation; }
}
