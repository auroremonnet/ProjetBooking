package dao;

import model.Hebergement;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HebergementDAO {
    private final Connection connection;

    public HebergementDAO(Connection connection) {
        this.connection = connection;
    }

    // ✅ Récupère tous les hébergements
    public List<Hebergement> getAllHebergements() throws Exception {
        List<Hebergement> liste = new ArrayList<>();
        String sql = "SELECT * FROM Hebergement";
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                liste.add(new Hebergement(
                        rs.getInt("idHebergement"),
                        rs.getString("nom"),
                        rs.getString("adresse"),
                        rs.getString("localisation"),
                        rs.getString("description"),
                        rs.getDouble("prix"),
                        rs.getString("categorie"),
                        rs.getString("photos"),
                        rs.getString("options")
                ));
            }
        }
        return liste;
    }

    // ✅ Recherche filtrée par lieu, catégorie, prix max
    public List<Hebergement> searchHebergements(String lieu, String categorie, double prixMax) throws Exception {
        List<Hebergement> resultats = new ArrayList<>();
        String sql = "SELECT * FROM Hebergement WHERE localisation LIKE ? AND categorie LIKE ? AND prix <= ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, "%" + lieu + "%");
            ps.setString(2, "%" + categorie + "%");
            ps.setDouble(3, prixMax);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    resultats.add(new Hebergement(
                            rs.getInt("idHebergement"),
                            rs.getString("nom"),
                            rs.getString("adresse"),
                            rs.getString("localisation"),
                            rs.getString("description"),
                            rs.getDouble("prix"),
                            rs.getString("categorie"),
                            rs.getString("photos"),
                            rs.getString("options")
                    ));
                }
            }
        }
        return resultats;
    }

    // ✅ Ajouter un hébergement
    public boolean ajouter(Hebergement h) throws Exception {
        String sql = "INSERT INTO Hebergement (nom, adresse, localisation, description, prix, categorie, photos, options) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, h.getNom());
            ps.setString(2, h.getAdresse());
            ps.setString(3, h.getLocalisation());
            ps.setString(4, h.getDescription());
            ps.setDouble(5, h.getPrix());
            ps.setString(6, h.getCategorie());
            ps.setString(7, h.getPhotos());
            ps.setString(8, h.getOptions());
            return ps.executeUpdate() == 1;
        }
    }

    // ✅ Supprimer un hébergement
    public boolean supprimer(int id) throws Exception {
        String sql = "DELETE FROM Hebergement WHERE idHebergement = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() == 1;
        }
    }

    // ✅ Modifier un hébergement
    public boolean mettreAJour(Hebergement h) throws Exception {
        String sql = "UPDATE Hebergement SET nom = ?, adresse = ?, localisation = ?, description = ?, prix = ?, categorie = ?, photos = ?, options = ? WHERE idHebergement = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, h.getNom());
            ps.setString(2, h.getAdresse());
            ps.setString(3, h.getLocalisation());
            ps.setString(4, h.getDescription());
            ps.setDouble(5, h.getPrix());
            ps.setString(6, h.getCategorie());
            ps.setString(7, h.getPhotos());
            ps.setString(8, h.getOptions());
            ps.setInt(9, h.getIdHebergement());
            return ps.executeUpdate() == 1;
        }
    }
}
