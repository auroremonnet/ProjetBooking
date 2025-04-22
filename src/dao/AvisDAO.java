package dao;

import model.Avis;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AvisDAO {
    private final Connection conn;

    public AvisDAO(Connection conn) {
        this.conn = conn;
    }

    public boolean aReserve(int idClient, int idHebergement) throws SQLException {
        String sql = "SELECT COUNT(*) FROM reservation WHERE idClient = ? AND idHebergement = ? AND statut IN ('Confirmée', 'Payée')";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idClient);
            stmt.setInt(2, idHebergement);
            ResultSet rs = stmt.executeQuery();
            rs.next();
            return rs.getInt(1) > 0;
        }
    }

    public boolean ajouterAvis(Avis avis) throws SQLException {
        String sql = "INSERT INTO avis (idClient, idHebergement, note, commentaire) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, avis.getIdClient());
            stmt.setInt(2, avis.getIdHebergement());
            stmt.setInt(3, avis.getNote());
            stmt.setString(4, avis.getCommentaire());
            return stmt.executeUpdate() > 0;
        }
    }

    public List<Avis> getAvisPourHebergement(int idHebergement) throws SQLException {
        List<Avis> avisList = new ArrayList<>();
        String sql = "SELECT * FROM avis WHERE idHebergement = ? ORDER BY dateAvis DESC";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idHebergement);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                avisList.add(new Avis(
                        rs.getInt("idAvis"),
                        rs.getInt("idClient"),
                        rs.getInt("idHebergement"),
                        rs.getInt("note"),
                        rs.getString("commentaire"),
                        rs.getTimestamp("dateAvis")
                ));
            }
        }
        return avisList;
    }

    public double getMoyenneAvis(int idHebergement) throws SQLException {
        String sql = "SELECT AVG(note) FROM avis WHERE idHebergement = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idHebergement);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble(1);
            }
        }
        return 0.0;
    }

    // ✅ Ajouté pour éviter les doublons d'avis
    public boolean dejaLaisse(int idClient, int idHebergement) throws SQLException {
        String sql = "SELECT COUNT(*) FROM avis WHERE idClient = ? AND idHebergement = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idClient);
            stmt.setInt(2, idHebergement);
            ResultSet rs = stmt.executeQuery();
            rs.next();
            return rs.getInt(1) > 0;
        }
    }
    public Avis getAvisByClientEtHebergement(int idClient, int idHebergement) throws Exception {
        String sql = "SELECT * FROM avis WHERE idClient = ? AND idHebergement = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idClient);
            ps.setInt(2, idHebergement);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Avis(
                        rs.getInt("idAvis"),
                        rs.getInt("idClient"),
                        rs.getInt("idHebergement"),
                        rs.getInt("note"),
                        rs.getString("commentaire"),
                        rs.getTimestamp("dateAvis")
                );
            }
        }
        return null;
    }

}
