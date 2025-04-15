import java.sql.*;

public class ClientDAO {
    private Connection conn;

    public ClientDAO(Connection conn) {
        this.conn = conn;
    }

    // Récupère un client via son email et mot de passe (pour la connexion)
    public Client getClientByEmailAndPassword(String email, String motDePasse) {
        String sql = "SELECT * FROM Client WHERE email = ? AND motDePasse = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
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
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Ajoute un nouveau client dans la base de données
    public boolean addClient(Client client) {
        String sql = "INSERT INTO Client (nom, prenom, email, motDePasse, typeClient, adresse, telephone) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, client.getNom());
            ps.setString(2, client.getPrenom());
            ps.setString(3, client.getEmail());
            ps.setString(4, client.getMotDePasse());
            ps.setString(5, client.getTypeClient());
            ps.setString(6, client.getAdresse());
            ps.setString(7, client.getTelephone());
            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("La création du client a échoué, aucune ligne affectée.");
            }
            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    client.setIdClient(generatedKeys.getInt(1));
                }
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Récupère un client via son identifiant
    public Client getClientById(int idClient) {
        String sql = "SELECT * FROM Client WHERE idClient = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idClient);
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
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Met à jour les informations d'un client
    public boolean updateClient(Client client) {
        String sql = "UPDATE Client SET nom = ?, prenom = ?, email = ?, motDePasse = ?, typeClient = ?, adresse = ?, telephone = ? WHERE idClient = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, client.getNom());
            ps.setString(2, client.getPrenom());
            ps.setString(3, client.getEmail());
            ps.setString(4, client.getMotDePasse());
            ps.setString(5, client.getTypeClient());
            ps.setString(6, client.getAdresse());
            ps.setString(7, client.getTelephone());
            ps.setInt(8, client.getIdClient());
            int affectedRows = ps.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Supprime un client
    public boolean deleteClient(int idClient) {
        String sql = "DELETE FROM Client WHERE idClient = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idClient);
            int affectedRows = ps.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
