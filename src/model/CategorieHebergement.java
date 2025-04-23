// model/CategorieHebergement.java
package model;

/**
 * Représente une catégorie d'hébergement (ex. “Chalet”, “Villa”, “Hôtel”, etc.).
 */
public class CategorieHebergement {
    private int idCategorie;
    private String nomCategorie;

    /** Constructeur vide (nécessaire pour certains frameworks ou sérialiseurs). */
    public CategorieHebergement() {}

    /** Constructeur complet. */
    public CategorieHebergement(int idCategorie, String nomCategorie) {
        this.idCategorie  = idCategorie;
        this.nomCategorie = nomCategorie;
    }

    // === Getters ===

    /** @return l'identifiant de la catégorie (clé primaire). */
    public int getIdCategorie() {
        return idCategorie;
    }

    /** @return le nom de la catégorie (ex. “Chalet”). */
    public String getNomCategorie() {
        return nomCategorie;
    }

    // === Setters ===

    /** @param idCategorie nouvelle valeur de l'ID. */
    public void setIdCategorie(int idCategorie) {
        this.idCategorie = idCategorie;
    }

    /** @param nomCategorie nouveau libellé de la catégorie. */
    public void setNomCategorie(String nomCategorie) {
        this.nomCategorie = nomCategorie;
    }

    @Override
    public String toString() {
        return "CategorieHebergement{" +
                "idCategorie=" + idCategorie +
                ", nomCategorie='" + nomCategorie + '\'' +
                '}';
    }
}
