package model;

import java.sql.Timestamp; // si besoin

public class Hebergement {
    private int     idHebergement;
    private String  nom;
    private String  adresse;
    private String  localisation;
    private String  description;
    private String  complementDescription; // ✅ nouveau champ
    private double  prix;
    private String  categorie;
    private String  photos;
    private String  options;
    private int     capaciteMax;
    private int     nombreLits;

    /**
     * Constructeur complet (pour lecture depuis la BDD)
     * Correspond exactement aux colonnes SELECT * FROM Hebergement
     */
    public Hebergement(int idHebergement,
                       String nom,
                       String adresse,
                       String localisation,
                       String description,
                       String complementDescription,
                       double prix,
                       String categorie,
                       String photos,
                       String options,
                       int capaciteMax,
                       int nombreLits) {
        this.idHebergement = idHebergement;
        this.nom           = nom;
        this.adresse       = adresse;
        this.localisation  = localisation;
        this.description   = description;
        this.complementDescription = complementDescription;
        this.prix          = prix;
        this.categorie     = categorie;
        this.photos        = photos;
        this.options       = options;
        this.capaciteMax   = capaciteMax;
        this.nombreLits    = nombreLits;
    }

    /**
     * Constructeur « avant INSERT » (sans id, car auto-increment)
     */
    public Hebergement(String nom,
                       String adresse,
                       String localisation,
                       String description,
                       String complementDescription,
                       double prix,
                       String categorie,
                       String photos,
                       String options,
                       int capaciteMax,
                       int nombreLits) {
        this(0, nom, adresse, localisation,
                description, complementDescription, prix, categorie,
                photos, options, capaciteMax, nombreLits);
    }

    // === Getters ===
    public int    getIdHebergement()        { return idHebergement; }
    public String getNom()                  { return nom; }
    public String getAdresse()              { return adresse; }
    public String getLocalisation()         { return localisation; }
    public String getDescription()          { return description; }
    public String getComplementDescription(){ return complementDescription; } // ✅
    public double getPrix()                 { return prix; }
    public String getCategorie()            { return categorie; }
    public String getPhotos()               { return photos; }
    public String getOptions()              { return options; }
    public int    getCapaciteMax()          { return capaciteMax; }
    public int    getNombreLits()           { return nombreLits; }

    // === Setters ===
    public void setIdHebergement(int id)          { this.idHebergement = id; }
    public void setNom(String nom)                { this.nom = nom; }
    public void setAdresse(String adresse)        { this.adresse = adresse; }
    public void setLocalisation(String loc)       { this.localisation = loc; }
    public void setDescription(String desc)       { this.description = desc; }
    public void setComplementDescription(String cdesc) { this.complementDescription = cdesc; } // ✅
    public void setPrix(double prix)              { this.prix = prix; }
    public void setCategorie(String cat)          { this.categorie = cat; }
    public void setPhotos(String photos)          { this.photos = photos; }
    public void setOptions(String opts)           { this.options = opts; }
    public void setCapaciteMax(int cap)           { this.capaciteMax = cap; }
    public void setNombreLits(int lits)           { this.nombreLits = lits; }
}
