package model;

public class ReductionClient {
    private String typeClient;      // "nouveau" ou "ancien"
    private double tauxReduction;   // en pourcent

    public ReductionClient() {}

    public ReductionClient(String typeClient, double tauxReduction) {
        this.typeClient    = typeClient;
        this.tauxReduction = tauxReduction;
    }

    public String getTypeClient() {
        return typeClient;
    }

    public void setTypeClient(String typeClient) {
        this.typeClient = typeClient;
    }

    public double getTauxReduction() {
        return tauxReduction;
    }

    public void setTauxReduction(double tauxReduction) {
        this.tauxReduction = tauxReduction;
    }
}
