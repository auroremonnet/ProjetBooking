package dao;

import model.Avis;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AvisDAO {
    private final Connection connection;

    public AvisDAO(Connection connection) {
        this.connection = connection;
    }

    public boolean ajouterAvis(Avis avis) throws Exception {
        if (avis.getNote() < 1 || avis.getNote() > 5) {
            throw new IllegalArgumentException("La note doit être entre 1 et 5.");
        }

        String sql = "INSERT INTO Avis (idClient, idHebergement, note, commentaire, dateAvis) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, avis.getIdClient());
            ps.setInt(2, avis.getIdHebergement());
            ps.setInt(3, avis.getNote());
            ps.setString(4, avis.getCommentaire());
            ps.setTimestamp(5, avis.getDateAvis());
            return ps.executeUpdate() == 1;
        }
    }

    public List<Avis> getAvisPourHebergement(int idHebergement) throws Exception {
        List<Avis> avisList = new ArrayList<>();
        String sql = "SELECT * FROM Avis WHERE idHebergement = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, idHebergement);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    avisList.add(new Avis(
                            rs.getInt("idAvis"),
                            rs.getInt("idClient"),
                            rs.getInt("idHebergement"),
                            rs.getInt("note"),
                            rs.getString("commentaire"),
                            rs.getTimestamp("dateAvis")
                    ));
                }
            }
        }
        return avisList;
    }
}
