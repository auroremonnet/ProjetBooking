// controller/ReportingController.java
package controller;

import dao.ReportingDAO;
import view.ReportingView;

import javax.swing.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

public class ReportingController {
    private final Connection connection;
    private final ReportingDAO reportingDAO;

    /**
     * Initialise le controller de reporting avec la connexion JDBC.
     */
    public ReportingController(Connection connection) {
        this.connection   = connection;
        this.reportingDAO = new ReportingDAO(connection);
    }

    /**
     * Affiche la fenêtre de reporting (camembert + histogramme).
     * Doit être appelé depuis le thread Swing.
     */
    public void showReporting() {
        SwingUtilities.invokeLater(() -> {
            new ReportingView(connection);
        });
    }

    /**
     * Récupère la répartition des réservations par catégorie
     * @return Map<catégorie, nombre de réservations>
     * @throws SQLException en cas de problème JDBC
     */
    public Map<String,Integer> getReservationCountByCategory() throws SQLException {
        return reportingDAO.getReservationCountByCategory();
    }

    /**
     * Récupère le nombre de réservations par mois pour l'année passée en paramètre.
     * @param year année cible (ex. LocalDate.now().getYear())
     * @return Map<"MM", nombre de réservations>
     * @throws SQLException en cas de problème JDBC
     */
    public Map<String,Integer> getMonthlyReservationCounts(int year) throws SQLException {
        return reportingDAO.getMonthlyReservationCounts(year);
    }
}
