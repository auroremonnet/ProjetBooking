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
    private int capaciteMax;
    private int nombreLits;

    // === Constructeur complet avec ID (utilisé à la lecture depuis la BDD) ===
    public Hebergement(int idHebergement, String nom, String adresse, String localisation,
                       String description, double prix, String categorie, String photos,
                       String options, int capaciteMax, int nombreLits) {
        this.idHebergement = idHebergement;
        this.nom = nom;
        this.adresse = adresse;
        this.localisation = localisation;
        this.description = description;
        this.prix = prix;
        this.categorie = categorie;
        this.photos = photos;
        this.options = options;
        this.capaciteMax = capaciteMax;
        this.nombreLits = nombreLits;
    }

    // === Constructeur sans ID (utilisé pour créer un hébergement avant insertion) ===
    public Hebergement(String nom, String adresse, String localisation,
                       String description, double prix, String categorie, String photos,
                       String options, int capaciteMax, int nombreLits) {
        this(0, nom, adresse, localisation, description, prix, categorie, photos, options, capaciteMax, nombreLits);
    }

    // === Getters ===
    public int getIdHebergement() { return idHebergement; }
    public String getNom() { return nom; }
    public String getAdresse() { return adresse; }
    public String getLocalisation() { return localisation; }
    public String getDescription() { return description; }
    public double getPrix() { return prix; }
    public String getCategorie() { return categorie; }
    public String getPhotos() { return photos; }
    public String getOptions() { return options; }
    public int getCapaciteMax() { return capaciteMax; }
    public int getNombreLits() { return nombreLits; }

    // === Setters (si vous voulez modifier après création) ===
    public void setIdHebergement(int idHebergement) { this.idHebergement = idHebergement; }
    public void setNom(String nom) { this.nom = nom; }
    public void setAdresse(String adresse) { this.adresse = adresse; }
    public void setLocalisation(String localisation) { this.localisation = localisation; }
    public void setDescription(String description) { this.description = description; }
    public void setPrix(double prix) { this.prix = prix; }
    public void setCategorie(String categorie) { this.categorie = categorie; }
    public void setPhotos(String photos) { this.photos = photos; }
    public void setOptions(String options) { this.options = options; }
    public void setCapaciteMax(int capaciteMax) { this.capaciteMax = capaciteMax; }
    public void setNombreLits(int nombreLits) { this.nombreLits = nombreLits; }
}