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
}
