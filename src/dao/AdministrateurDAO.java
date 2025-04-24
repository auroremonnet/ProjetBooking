package dao;

import model.Administrateur;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class AdministrateurDAO {
    private final Connection connection;

    public AdministrateurDAO(Connection connection) {
        this.connection = connection;
    }

    public Administrateur login(String email, String motDePasse) throws Exception {
        String sql = "SELECT * FROM Administrateur WHERE email = ? AND motDePasse = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, email);
            ps.setString(2, motDePasse);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Administrateur(
                            rs.getInt("idAdministrateur"),
                            rs.getString("nom"),
                            rs.getString("prenom"),
                            rs.getString("email"),
                            rs.getString("motDePasse")
                    );
                }
            }
        }
        return null;
    }

    public boolean register(Administrateur admin) throws Exception {
        String sql = "INSERT INTO Administrateur (nom, prenom, email, motDePasse, photo) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, admin.getNom());
            ps.setString(2, admin.getPrenom());
            ps.setString(3, admin.getEmail());
            ps.setString(4, admin.getMotDePasse());
            return ps.executeUpdate() == 1;
        }
    }
}
