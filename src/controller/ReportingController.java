// controller/ReportingController.java
package controller;

import dao.ReportingDAO;
import model.Administrateur;
import view.ReportingView;

import javax.swing.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

public class ReportingController {
    private final Connection connection;
    private final ReportingDAO reportingDAO;
    private final Administrateur administrateur;

    /**
     * Initialise le controller de reporting avec la connexion JDBC et l'admin connecté.
     */
    public ReportingController(Administrateur administrateur, Connection connection) {
        this.administrateur = administrateur;
        this.connection     = connection;
        this.reportingDAO   = new ReportingDAO(connection);
    }

    /**
     * Affiche la fenêtre de reporting (graph empilé) sur le thread Swing.
     */
    public void showReporting() {
        SwingUtilities.invokeLater(() -> {
            // On lui passe l'admin afin que la vue puisse proposer un bouton "Retour"
            new ReportingView(administrateur, connection);
        });
    }

    /**
     * Récupère la répartition des réservations par catégorie.
     * @return Map<catégorie, nombre de réservations>
     */
    public Map<String,Integer> getReservationCountByCategory() throws SQLException {
        return reportingDAO.getReservationCountByCategory();
    }

    /**
     * Récupère le nombre de réservations par mois pour l'année donnée.
     * @param year année cible (ex. LocalDate.now().getYear())
     * @return Map<"MM", nombre de réservations>
     */
    public Map<String,Integer> getMonthlyReservationCounts(int year) throws SQLException {
        return reportingDAO.getMonthlyReservationCounts(year);
    }

    /**
     * Récupère la répartition empilée : pour chaque catégorie,
     * la répartition mois par mois.
     * @param year année cible
     * @return Map<catégorie, Map<"MM", count>>
     */
    public Map<String, Map<String,Integer>> getReservationCountByCategoryAndMonth(int year)
            throws SQLException {
        return reportingDAO.getReservationCountByCategoryAndMonth(year);
    }
}
