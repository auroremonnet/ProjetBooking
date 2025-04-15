package model;

public class Reduction {
    // Attribut représentant le pourcentage de réduction
    private double pourcentage;

    // Constructeur
    public Reduction(double pourcentage) {
        this.pourcentage = pourcentage;
    }

    // Getter pour le pourcentage
    public double getPourcentage() {
        return pourcentage;
    }

    // Setter pour le pourcentage
    public void setPourcentage(double pourcentage) {
        this.pourcentage = pourcentage;
    }

    // Méthode toString pour faciliter l'affichage de l'objet Reduction
    @Override
    public String toString() {
        return "Reduction{" +
                "pourcentage=" + pourcentage +
                '}';
    }
}
