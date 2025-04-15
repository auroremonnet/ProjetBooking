package model;

import java.sql.Timestamp;

public class Avis {
    private int idAvis;
    private int idClient;
    private int idHebergement;
    private int note;
    private String commentaire;
    private Timestamp dateAvis;

    public Avis(int idAvis, int idClient, int idHebergement, int note, String commentaire, Timestamp dateAvis) {
        this.idAvis = idAvis;
        this.idClient = idClient;
        this.idHebergement = idHebergement;
        this.note = note;
        this.commentaire = commentaire;
        this.dateAvis = dateAvis;
    }

    public int getIdAvis() { return idAvis; }
    public int getIdClient() { return idClient; }
    public int getIdHebergement() { return idHebergement; }
    public int getNote() { return note; }
    public String getCommentaire() { return commentaire; }
    public Timestamp getDateAvis() { return dateAvis; }
}
