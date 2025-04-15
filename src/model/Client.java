package model;

public class Client {
    private int idClient;
    private String nom;
    private String prenom;
    private String email;
    private String motDePasse;
    private String typeClient;
    private String adresse;
    private String telephone;

    public Client(int idClient, String nom, String prenom, String email, String motDePasse, String typeClient, String adresse, String telephone) {
        this.idClient = idClient;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.motDePasse = motDePasse;
        this.typeClient = typeClient;
        this.adresse = adresse;
        this.telephone = telephone;
    }

    public Client(String nom, String prenom, String email, String motDePasse, String typeClient, String adresse, String telephone) {
        this(0, nom, prenom, email, motDePasse, typeClient, adresse, telephone);
    }

    public int getIdClient() { return idClient; }
    public String getNom() { return nom; }
    public String getPrenom() { return prenom; }
    public String getEmail() { return email; }
    public String getMotDePasse() { return motDePasse; }
    public String getTypeClient() { return typeClient; }
    public String getAdresse() { return adresse; }
    public String getTelephone() { return telephone; }
}
