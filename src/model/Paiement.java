package model;

import java.sql.Timestamp;

public class Paiement {
    private int idPaiement;
    private int idReservation;
    private double montant;
    private String modePaiement;
    private Timestamp datePaiement;

    public Paiement(int idPaiement, int idReservation, double montant, String modePaiement, Timestamp datePaiement) {
        this.idPaiement = idPaiement;
        this.idReservation = idReservation;
        this.montant = montant;
        this.modePaiement = modePaiement;
        this.datePaiement = datePaiement;
    }

    public int getIdPaiement() { return idPaiement; }
    public int getIdReservation() { return idReservation; }
    public double getMontant() { return montant; }
    public String getModePaiement() { return modePaiement; }
    public Timestamp getDatePaiement() { return datePaiement; }
}