package dao;

import model.Utilisateur;
import java.sql.*;

public class UtilisateurDAO {
    private Connection connection;

    public UtilisateurDAO(Connection connection) {
        this.connection = connection;
    }

    public Utilisateur login(String email, String password) throws SQLException {
        String sql = "SELECT * FROM utilisateurs WHERE email = ? AND mot_de_passe = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, email);
            ps.setString(2, password); // 🔐 hash si implémenté
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Utilisateur(
                            rs.getInt("id"),
                            rs.getString("nom"),
                            rs.getString("email"),
                            rs.getString("type")
                    );
                }
            }
        }
        return null;
    }

    public boolean register(Utilisateur u) throws SQLException {
        String sql = "INSERT INTO utilisateurs (nom, email, mot_de_passe, type) VALUES (?, ?, ?, ?)";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, u.getNom());
            ps.setString(2, u.getEmail());
            ps.setString(3, u.getMotDePasse()); // 🔐 à sécuriser en hash si souhaité
            ps.setString(4, u.getType());
            int rows = ps.executeUpdate();
            return rows == 1;
        }
    }
}
