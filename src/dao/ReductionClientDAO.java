package dao;


import java.sql.*;
import java.util.Optional;
import model.ReductionClient;

public class ReductionClientDAO {
    private final Connection conn;

    public ReductionClientDAO(Connection conn) {
        this.conn = conn;
    }

    /** Récupère le taux de réduction pour un type de client donné. */
    public Optional<ReductionClient> findByType(String typeClient) throws SQLException {
        String sql = "SELECT type_client, taux_reduction FROM reduction_client WHERE type_client = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, typeClient);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(new ReductionClient(
                            rs.getString("type_client"),
                            rs.getDouble("taux_reduction")
                    ));
                }
                return Optional.empty();
            }
        }
    }

    /** Met à jour le taux de réduction pour un type de client (ici 'ancien'). */
    public void updateTaux(String typeClient, double nouveauTaux) throws SQLException {
        String sql = "UPDATE reduction_client SET taux_reduction = ? WHERE type_client = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDouble(1, nouveauTaux);
            ps.setString(2, typeClient);
            ps.executeUpdate();
        }
    }
}
