package controller;

import dao.ClientDAO;
import dao.HebergementDAO;
import dao.ReductionClientDAO;
import model.Client;
import model.Hebergement;
import model.ReductionClient;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * Contrôleur pour les opérations administrateur (clients, hébergements, réductions).
 */
public class AdminController {
    private final HebergementDAO hebergementDAO;
    private final ClientDAO clientDAO;
    private final ReductionClientDAO reductionDAO;

    /**
     * Initialise le contrôleur avec une connexion JDBC.
     */
    public AdminController(Connection conn) {
        this.hebergementDAO = new HebergementDAO(conn);
        this.clientDAO      = new ClientDAO(conn);
        this.reductionDAO   = new ReductionClientDAO(conn);
    }

    // ===================== Hébergements =====================

    public boolean ajouterHebergement(Hebergement h) {
        try {
            return hebergementDAO.ajouter(h);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean supprimerHebergement(int idHebergement) {
        try {
            return hebergementDAO.supprimer(idHebergement);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Hebergement> listerHebergements() {
        try {
            return hebergementDAO.getAllHebergements();
        } catch (SQLException e) {
            e.printStackTrace();
            return List.of();
        }
    }

    public boolean modifierHebergement(Hebergement h) {
        try {
            return hebergementDAO.mettreAJour(h);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // ========================= Clients =========================

    public boolean ajouterClient(Client client) {
        try {
            return clientDAO.ajouter(client);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean supprimerClient(int idClient) {
        try {
            return clientDAO.supprimer(idClient);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Client> listerClients() {
        try {
            return clientDAO.getAllClients();
        } catch (SQLException e) {
            e.printStackTrace();
            return List.of();
        }
    }

    public boolean modifierClient(Client client) {
        try {
            return clientDAO.mettreAJour(client);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // ======================= Réductions =======================

    /**
     * Récupère le taux de réduction pour un type de client ("nouveau" ou "ancien").
     * Si l'entrée est manquante, utilise 5.0% par défaut pour les nouveaux, 0 pour les autres.
     */
    public double getTauxReduction(String typeClient) {
        try {
            Optional<ReductionClient> opt = reductionDAO.findByType(typeClient);
            if (opt.isPresent()) {
                return opt.get().getTauxReduction();
            } else if ("nouveau".equals(typeClient)) {
                return 5.0;
            } else {
                return 0.0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "nouveau".equals(typeClient) ? 5.0 : 0.0;
        }
    }

    /**
     * Shortcut pour obtenir le taux des anciens.
     */
    public double getTauxReductionAncien() {
        return getTauxReduction("ancien");
    }

    /**
     * Met à jour le taux de réduction pour un type de client donné.
     * @return true si la mise à jour a réussi
     */
    public boolean modifierTauxReduction(String typeClient, double nouveauTaux) {
        try {
            reductionDAO.updateTaux(typeClient, nouveauTaux);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Shortcut pour modifier le taux des anciens.
     */
    public boolean modifierTauxReductionAncien(double nouveauTaux) {
        return modifierTauxReduction("ancien", nouveauTaux);
    }
}
