package dao;

import model.Client;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClientDAO {
    private final Connection connection;

    public ClientDAO(Connection connection) {
        this.connection = connection;
    }

    // 🔐 Connexion d'un client (login)
    public Client login(String email, String motDePasse) throws SQLException {
        String sql = "SELECT * FROM Client WHERE email = ? AND motDePasse = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, email);
            ps.setString(2, motDePasse);
            ResultSet rs = ps.executeQuery();
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
        return null;
    }

    // ✅ Enregistrement d'un nouveau client
    public boolean register(Client client) throws SQLException {
        String sql = "INSERT INTO Client (nom, prenom, email, motDePasse, typeClient, adresse, telephone) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, client.getNom());
            ps.setString(2, client.getPrenom());
            ps.setString(3, client.getEmail());
            ps.setString(4, client.getMotDePasse());
            ps.setString(5, client.getTypeClient());
            ps.setString(6, client.getAdresse());
            ps.setString(7, client.getTelephone());
            return ps.executeUpdate() == 1;
        }
    }

    // Alias pour respecter l’interface AdminController
    public boolean ajouter(Client client) throws SQLException {
        return register(client);
    }

    // ❌ Suppression d’un client par son ID
    public boolean supprimer(int idClient) throws SQLException {
        String sql = "DELETE FROM Client WHERE idClient = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idClient);
            return stmt.executeUpdate() > 0;
        }
    }

    // 📋 Liste de tous les clients
    public List<Client> getAllClients() throws SQLException {
        List<Client> clients = new ArrayList<>();
        String sql = "SELECT * FROM Client";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Client client = new Client(
                        rs.getInt("idClient"),
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getString("email"),
                        rs.getString("motDePasse"),
                        rs.getString("typeClient"),
                        rs.getString("adresse"),
                        rs.getString("telephone")
                );
                clients.add(client);
            }
        }
        return clients;
    }

    // ✏️ Mise à jour d’un client
    public boolean mettreAJour(Client client) throws SQLException {
        String sql = "UPDATE Client SET nom = ?, prenom = ?, email = ?, motDePasse = ?, typeClient = ?, adresse = ?, telephone = ? WHERE idClient = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, client.getNom());
            stmt.setString(2, client.getPrenom());
            stmt.setString(3, client.getEmail());
            stmt.setString(4, client.getMotDePasse());
            stmt.setString(5, client.getTypeClient());
            stmt.setString(6, client.getAdresse());
            stmt.setString(7, client.getTelephone());
            stmt.setInt(8, client.getIdClient());
            return stmt.executeUpdate() > 0;
        }
    }
}
