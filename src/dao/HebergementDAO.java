import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HebergementDAO {
    private Connection conn;

    public HebergementDAO(Connection conn) {
        this.conn = conn;
    }

    // Ajoute un nouvel hébergement
    public boolean addHebergement(Hebergement hebergement) {
        String sql = "INSERT INTO Hebergement (nom, adresse, localisation, description, prix, categorie, photos, options) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, hebergement.getNom());
            ps.setString(2, hebergement.getAdresse());
            ps.setString(3, hebergement.getLocalisation());
            ps.setString(4, hebergement.getDescription());
            ps.setBigDecimal(5, hebergement.getPrix());
            ps.setString(6, hebergement.getCategorie());
            ps.setString(7, hebergement.getPhotos());
            ps.setString(8, hebergement.getOptions());
            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("La création de l'hébergement a échoué, aucune ligne affectée.");
            }
            try (ResultSet generatedKeys = ps.getGeneratedKeys()){
                if (generatedKeys.next()){
                    hebergement.setIdHebergement(generatedKeys.getInt(1));
                }
            }
            return true;
        } catch(SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    // Récupère un hébergement par son identifiant
    public Hebergement getHebergementById(int idHebergement) {
        String sql = "SELECT * FROM Hebergement WHERE idHebergement = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idHebergement);
            ResultSet rs = ps.executeQuery();
            if (rs.next()){
                return new Hebergement(
                        rs.getInt("idHebergement"),
                        rs.getString("nom"),
                        rs.getString("adresse"),
                        rs.getString("localisation"),
                        rs.getString("description"),
                        rs.getBigDecimal("prix"),
                        rs.getString("categorie"),
                        rs.getString("photos"),
                        rs.getString("options")
                );
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    // Récupère la liste de tous les hébergements
    public List<Hebergement> getAllHebergements() {
        List<Hebergement> list = new ArrayList<>();
        String sql = "SELECT * FROM Hebergement";
        try (Statement stmt = conn.createStatement()){
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()){
                Hebergement hebergement = new Hebergement(
                        rs.getInt("idHebergement"),
                        rs.getString("nom"),
                        rs.getString("adresse"),
                        rs.getString("localisation"),
                        rs.getString("description"),
                        rs.getBigDecimal("prix"),
                        rs.getString("categorie"),
                        rs.getString("photos"),
                        rs.getString("options")
                );
                list.add(hebergement);
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Met à jour un hébergement
    public boolean updateHebergement(Hebergement hebergement) {
        String sql = "UPDATE Hebergement SET nom = ?, adresse = ?, localisation = ?, description = ?, prix = ?, categorie = ?, photos = ?, options = ? WHERE idHebergement = ?";
        try(PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setString(1, hebergement.getNom());
            ps.setString(2, hebergement.getAdresse());
            ps.setString(3, hebergement.getLocalisation());
            ps.setString(4, hebergement.getDescription());
            ps.setBigDecimal(5, hebergement.getPrix());
            ps.setString(6, hebergement.getCategorie());
            ps.setString(7, hebergement.getPhotos());
            ps.setString(8, hebergement.getOptions());
            ps.setInt(9, hebergement.getIdHebergement());
            int affectedRows = ps.executeUpdate();
            return affectedRows > 0;
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Supprime un hébergement
    public boolean deleteHebergement(int idHebergement) {
        String sql = "DELETE FROM Hebergement WHERE idHebergement = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idHebergement);
            int affectedRows = ps.executeUpdate();
            return affectedRows > 0;
        } catch(SQLException e){
            e.printStackTrace();
        }
        return false;
    }
}
