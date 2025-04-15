package model;

public class Hebergement {
    private int idHebergement;
    private String nom;
    private String adresse;
    private String localisation;
    private String description;
    private double prix;
    private String categorie;
    private String photos;
    private String options;

    public Hebergement(int idHebergement, String nom, String adresse, String localisation,
                       String description, double prix, String categorie, String photos, String options) {
        this.idHebergement = idHebergement;
        this.nom = nom;
        this.adresse = adresse;
        this.localisation = localisation;
        this.description = description;
        this.prix = prix;
        this.categorie = categorie;
        this.photos = photos;
        this.options = options;
    }

    public Hebergement(String nom, String adresse, String localisation,
                       String description, double prix, String categorie, String photos, String options) {
        this(0, nom, adresse, localisation, description, prix, categorie, photos, options);
    }

    public int getIdHebergement() { return idHebergement; }
    public String getNom() { return nom; }
    public String getAdresse() { return adresse; }
    public String getLocalisation() { return localisation; }
    public String getDescription() { return description; }
    public double getPrix() { return prix; }
    public String getCategorie() { return categorie; }
    public String getPhotos() { return photos; }
    public String getOptions() { return options; }
}
