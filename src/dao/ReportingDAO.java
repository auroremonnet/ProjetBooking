// dao/ReportingDAO.java
package dao;

import java.sql.*;
import java.util.LinkedHashMap;
import java.util.Map;

public class ReportingDAO {
    private final Connection connection;

    public ReportingDAO(Connection connection) {
        this.connection = connection;
    }

    /**
     * Répartition des réservations par type d’hébergement.
     * @return Map<catégorie, nombre de réservations>
     */
    public Map<String,Integer> getReservationCountByCategory() throws SQLException {
        String sql = """
            SELECT h.categorie, COUNT(*) AS cnt
              FROM Reservation r
              JOIN Hebergement h ON r.idHebergement = h.idHebergement
             GROUP BY h.categorie
            """;
        Map<String,Integer> result = new LinkedHashMap<>();
        try (Statement st = connection.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                result.put(rs.getString("categorie"), rs.getInt("cnt"));
            }
        }
        return result;
    }

    /**
     * Nombre de réservations par mois pour une année donnée.
     * Initialise d'abord tous les mois à 0.
     * @param year année (ex. 2025)
     * @return Map<"01".."12", nombre>
     */
    public Map<String,Integer> getMonthlyReservationCounts(int year) throws SQLException {
        String sql = """
            SELECT MONTH(r.dateReservation) AS month, COUNT(*) AS cnt
              FROM Reservation r
             WHERE YEAR(r.dateReservation) = ?
             GROUP BY MONTH(r.dateReservation)
             ORDER BY MONTH(r.dateReservation)
            """;

        // on prépare la map avec tous les mois = 0
        Map<String,Integer> result = new LinkedHashMap<>();
        for (int m = 1; m <= 12; m++) {
            result.put(String.format("%02d", m), 0);
        }

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, year);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String mois = String.format("%02d", rs.getInt("month"));
                    result.put(mois, rs.getInt("cnt"));
                }
            }
        }
        return result;
    }

    /**
     * Pour chaque catégorie, nombre de réservations par mois.
     * Sous-map initialisée avec tous les mois 01→12 à 0.
     * @param year année (ex. 2025)
     * @return Map<catégorie, Map<"01".."12", nombre>>
     */
    public Map<String, Map<String,Integer>> getReservationCountByCategoryAndMonth(int year) throws SQLException {
        String sql = """
            SELECT h.categorie,
                   MONTH(r.dateReservation) AS month,
                   COUNT(*)               AS cnt
              FROM Reservation r
              JOIN Hebergement h ON r.idHebergement = h.idHebergement
             WHERE YEAR(r.dateReservation) = ?
             GROUP BY h.categorie, MONTH(r.dateReservation)
             ORDER BY h.categorie, MONTH(r.dateReservation)
            """;

        Map<String,Map<String,Integer>> result = new LinkedHashMap<>();
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, year);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String cat   = rs.getString("categorie");
                    String mois  = String.format("%02d", rs.getInt("month"));
                    int cnt      = rs.getInt("cnt");

                    // crée et initialise la sous-map si nécessaire
                    result.computeIfAbsent(cat, k -> {
                        Map<String,Integer> m = new LinkedHashMap<>();
                        for (int mm = 1; mm <= 12; mm++) {
                            m.put(String.format("%02d", mm), 0);
                        }
                        return m;
                    });

                    // remplace le mois concerné
                    result.get(cat).put(mois, cnt);
                }
            }
        }
        return result;
    }
}
