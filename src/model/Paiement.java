// src/model/Paiement.java
package model;

import java.sql.Timestamp;

/**
 * Représente un paiement pour une réservation.
 */
public class Paiement {
    private int       idPaiement;
    private int       idReservation;
    private double    montant;
    private String    modePaiement;      // toujours "Carte bancaire"
    private Timestamp datePaiement;
    private Integer   idMoyenPaiement;   // null si pas CB
    private String    cvv;               // null si pas CB
    private double    tauxReduction;     // nouveau champ

    public Paiement(int idPaiement,
                    int idReservation,
                    double montant,
                    String modePaiement,
                    Timestamp datePaiement,
                    Integer idMoyenPaiement,
                    String cvv) {
        this.idPaiement      = idPaiement;
        this.idReservation   = idReservation;
        this.montant         = montant;
        this.modePaiement    = modePaiement;
        this.datePaiement    = datePaiement;
        this.idMoyenPaiement = idMoyenPaiement;
        this.cvv             = cvv;
        this.tauxReduction   = 0.0; // valeur par défaut
    }

    // Getters
    public int getIdPaiement()         { return idPaiement; }
    public int getIdReservation()      { return idReservation; }
    public double getMontant()         { return montant; }
    public String getModePaiement()    { return modePaiement; }
    public Timestamp getDatePaiement() { return datePaiement; }
    public Integer getIdMoyenPaiement(){ return idMoyenPaiement; }
    public String getCvv()             { return cvv; }
    public double getTauxReduction()   { return tauxReduction; }

    // Setters
    public void setMontant(double montant) {
        this.montant = montant;
    }

    public void setTauxReduction(double tauxReduction) {
        this.tauxReduction = tauxReduction;
    }
}
