// src/model/Paiement.java
package model;

import java.sql.Timestamp;

/**
 * Représente un paiement pour une réservation.
 */
public class Paiement {
    private final int       idPaiement;
    private final int       idReservation;
    private final double    montant;
    private final String    modePaiement;      // toujours "Carte bancaire"
    private final Timestamp datePaiement;
    private final Integer   idMoyenPaiement;   // null si pas CB
    private final String    cvv;               // null si pas CB

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
    }
    // getters…
    public int getIdPaiement()        { return idPaiement; }
    public int getIdReservation()     { return idReservation; }
    public double getMontant()        { return montant; }
    public String getModePaiement()   { return modePaiement; }
    public Timestamp getDatePaiement(){ return datePaiement; }
    public Integer getIdMoyenPaiement(){ return idMoyenPaiement; }
    public String getCvv()            { return cvv; }
}
