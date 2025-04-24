package model;

public class Administrateur {
    private int idAdministrateur;
    private String nom;
    private String prenom;
    private String email;
    private String motDePasse;
    private String photo;

    public Administrateur(int idAdministrateur, String nom, String prenom, String email, String motDePasse) {
        this.idAdministrateur = idAdministrateur;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.motDePasse = motDePasse;
        this.photo = photo;

    }

    public Administrateur(String nom, String prenom, String email, String motDePasse) {
        this(0, nom, prenom, email, motDePasse);
    }

    public int getIdAdministrateur() { return idAdministrateur; }
    public String getNom() { return nom; }
    public String getPrenom() { return prenom; }
    public String getEmail() { return email; }
    public String getMotDePasse() { return motDePasse; }
    public String getPhoto() { return photo;}
}
