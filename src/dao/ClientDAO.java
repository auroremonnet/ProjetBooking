package dao;

import model.Client;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ClientDAO {
    private final Connection connection;

    public ClientDAO(Connection connection) {
        this.connection = connection;
    }

    public Client login(String email, String motDePasse) throws Exception {
        String sql = "SELECT * FROM Client WHERE email = ? AND motDePasse = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, email);
            ps.setString(2, motDePasse);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Client(
                            rs.getInt("idClient"),
                            rs.getString("nom"),
                            rs.getString("prenom"),
                            rs.getString("email"),
                            rs.getString("motDePasse"),
                            rs.getString("typeClient"),
                            rs.getString("adresse"),
                            rs.getString("telephone")
                    );
                }
            }
        }
        return null;
    }

    public boolean register(Client client) throws Exception {
        String sql = "INSERT INTO Client (nom, prenom, email, motDePasse, typeClient, adresse, telephone) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, client.getNom());
            ps.setString(2, client.getPrenom());
            ps.setString(3, client.getEmail());
            ps.setString(4, client.getMotDePasse());
            ps.setString(5, client.getTypeClient());  // Par exemple "nouveau"
            ps.setString(6, client.getAdresse());
            ps.setString(7, client.getTelephone());
            return ps.executeUpdate() == 1;
        }
    }
}
