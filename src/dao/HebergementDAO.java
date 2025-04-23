// dao/HebergementDAO.java
package dao;

import model.Hebergement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class HebergementDAO {
    private final Connection connection;

    public HebergementDAO(Connection connection) {
        this.connection = connection;
    }

    /** Récupère tous les hébergements */
    public List<Hebergement> getAllHebergements() throws SQLException {
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
                        rs.getString("options"),
                        rs.getInt("capacite_max"),
                        rs.getInt("nombre_lits")
                ));
            }
        }
        return liste;
    }

    /** Recherche filtrée par lieu, catégorie et prix max */
    public List<Hebergement> searchHebergements(String lieu, String categorie, double prixMax) throws SQLException {
        List<Hebergement> resultats = new ArrayList<>();
        String sql = "SELECT * FROM Hebergement "
                + "WHERE localisation LIKE ? AND categorie LIKE ? AND prix <= ?";
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
                            rs.getString("options"),
                            rs.getInt("capacite_max"),
                            rs.getInt("nombre_lits")
                    ));
                }
            }
        }
        return resultats;
    }

    /** Ajoute un nouvel hébergement */
    public boolean ajouter(Hebergement h) throws SQLException {
        String sql = "INSERT INTO Hebergement "
                + "(nom, adresse, localisation, description, prix, categorie, photos, options, capacite_max, nombre_lits) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, h.getNom());
            ps.setString(2, h.getAdresse());
            ps.setString(3, h.getLocalisation());
            ps.setString(4, h.getDescription());
            ps.setDouble(5, h.getPrix());
            ps.setString(6, h.getCategorie());
            ps.setString(7, h.getPhotos());
            ps.setString(8, h.getOptions());
            ps.setInt(9, h.getCapaciteMax());
            ps.setInt(10, h.getNombreLits());
            return ps.executeUpdate() == 1;
        }
    }

    /** Supprime un hébergement par son ID */
    public boolean supprimer(int id) throws SQLException {
        String sql = "DELETE FROM Hebergement WHERE idHebergement = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() == 1;
        }
    }

    /** Met à jour un hébergement existant */
    public boolean mettreAJour(Hebergement h) throws SQLException {
        String sql = "UPDATE Hebergement SET "
                + "nom = ?, adresse = ?, localisation = ?, description = ?, "
                + "prix = ?, categorie = ?, photos = ?, options = ?, "
                + "capacite_max = ?, nombre_lits = ? "
                + "WHERE idHebergement = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, h.getNom());
            ps.setString(2, h.getAdresse());
            ps.setString(3, h.getLocalisation());
            ps.setString(4, h.getDescription());
            ps.setDouble(5, h.getPrix());
            ps.setString(6, h.getCategorie());
            ps.setString(7, h.getPhotos());
            ps.setString(8, h.getOptions());
            ps.setInt(9, h.getCapaciteMax());
            ps.setInt(10, h.getNombreLits());
            ps.setInt(11, h.getIdHebergement());
            return ps.executeUpdate() == 1;
        }
    }

    /** Recherche avancée (localisation, catégorie, options) */
    public List<Hebergement> rechercherHebergements(String localisation, String categorie, String options) throws SQLException {
        List<Hebergement> results = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM Hebergement WHERE 1=1");
        if (!localisation.isEmpty()) sql.append(" AND localisation LIKE ?");
        if (!categorie.isEmpty())    sql.append(" AND categorie    LIKE ?");
        if (!options.isEmpty())      sql.append(" AND options      LIKE ?");

        try (PreparedStatement ps = connection.prepareStatement(sql.toString())) {
            int idx = 1;
            if (!localisation.isEmpty()) ps.setString(idx++, "%" + localisation + "%");
            if (!categorie.isEmpty())    ps.setString(idx++, "%" + categorie    + "%");
            if (!options.isEmpty())      ps.setString(idx++, "%" + options      + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    results.add(new Hebergement(
                            rs.getInt("idHebergement"),
                            rs.getString("nom"),
                            rs.getString("adresse"),
                            rs.getString("localisation"),
                            rs.getString("description"),
                            rs.getDouble("prix"),
                            rs.getString("categorie"),
                            rs.getString("photos"),
                            rs.getString("options"),
                            rs.getInt("capacite_max"),
                            rs.getInt("nombre_lits")
                    ));
                }
            }
        }
        return results;
    }

    /** Recherche un hébergement par son ID */
    public Hebergement findById(int id) throws SQLException {
        String sql = "SELECT * FROM Hebergement WHERE idHebergement = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Hebergement(
                            rs.getInt("idHebergement"),
                            rs.getString("nom"),
                            rs.getString("adresse"),
                            rs.getString("localisation"),
                            rs.getString("description"),
                            rs.getDouble("prix"),
                            rs.getString("categorie"),
                            rs.getString("photos"),
                            rs.getString("options"),
                            rs.getInt("capacite_max"),
                            rs.getInt("nombre_lits")
                    );
                }
            }
        }
        return null;
    }
}
