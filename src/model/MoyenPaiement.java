package model;

public class MoyenPaiement {
    private int idMoyenPaiement;
    private int idClient;
    private String typeCarte;
    private String numeroLast4;
    private int expMois;
    private int expAnnee;
    private String cvv;
    private double solde;

    public MoyenPaiement(int idMoyenPaiement,
                         int idClient,
                         String typeCarte,
                         String numeroLast4,
                         int expMois,
                         int expAnnee,
                         String cvv,
                         double solde) {
        this.idMoyenPaiement = idMoyenPaiement;
        this.idClient        = idClient;
        this.typeCarte       = typeCarte;
        this.numeroLast4     = numeroLast4;
        this.expMois         = expMois;
        this.expAnnee        = expAnnee;
        this.cvv             = cvv;
        this.solde           = solde;
    }

    // Getters
    public int    getIdMoyenPaiement() { return idMoyenPaiement; }
    public int    getIdClient()        { return idClient; }
    public String getTypeCarte()       { return typeCarte; }
    public String getNumeroLast4()     { return numeroLast4; }
    public int    getExpMois()         { return expMois; }
    public int    getExpAnnee()        { return expAnnee; }
    public String getCvv()             { return cvv; }
    public double getSolde()           { return solde; }

    // Setter pour mettre à jour le solde
    public void setSolde(double solde) { this.solde = solde; }
}
