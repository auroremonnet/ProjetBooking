package model;

import java.sql.Timestamp;

public class Mail {
    private int idMail;
    private int idClient;
    private String objet;
    private String contenu;
    private Timestamp dateEnvoi;

    public Mail(int idMail, int idClient, String objet, String contenu, Timestamp dateEnvoi) {
        this.idMail = idMail;
        this.idClient = idClient;
        this.objet = objet;
        this.contenu = contenu;
        this.dateEnvoi = dateEnvoi;
    }

    public Mail(int idClient, String objet, String contenu) {
        this.idClient = idClient;
        this.objet = objet;
        this.contenu = contenu;
    }

    public int getIdMail() { return idMail; }
    public int getIdClient() { return idClient; }
    public String getObjet() { return objet; }
    public String getContenu() { return contenu; }
    public Timestamp getDateEnvoi() { return dateEnvoi; }

    // Ajouté pour compatibilité avec les appels .getMessage()
    public String getMessage() {
        return contenu;
    }
}
