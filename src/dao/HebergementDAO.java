package dao;

import model.Hebergement;
import java.sql.*;
import java.util.*;

public class HebergementDAO {
    private Connection connection;

    public HebergementDAO(Connection connection) {
        this.connection = connection;
    }

    public List<Hebergement> getAllHebergements() throws SQLException {
        List<Hebergement> liste = new ArrayList<>();
        String sql = "SELECT * FROM hebergements";

        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Hebergement h = new Hebergement(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getString("adresse"),
                        rs.getString("categorie"),
                        rs.getDouble("prix_par_nuit")
                );
                liste.add(h);
            }
        }
        return liste;
    }

    public List<Hebergement> searchHebergements(String lieu, String categorie, double prixMax) throws SQLException {
        List<Hebergement> result = new ArrayList<>();
        String sql = "SELECT * FROM hebergements WHERE adresse LIKE ? AND categorie LIKE ? AND prix_par_nuit <= ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, "%" + lieu + "%");
            ps.setString(2, "%" + categorie + "%");
            ps.setDouble(3, prixMax);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Hebergement h = new Hebergement(
                            rs.getInt("id"),
                            rs.getString("nom"),
                            rs.getString("adresse"),
                            rs.getString("categorie"),
                            rs.getDouble("prix_par_nuit")
                    );
                    result.add(h);
                }
            }
        }
        return result;
    }
}
