import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AvisDAO {
    private Connection conn;

    public AvisDAO(Connection conn) {
        this.conn = conn;
    }

    // Ajoute un nouvel avis
    public boolean addAvis(Avis avis) {
        String sql = "INSERT INTO Avis (idClient, idHebergement, note, commentaire) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, avis.getIdClient());
            ps.setInt(2, avis.getIdHebergement());
            ps.setInt(3, avis.getNote());
            ps.setString(4, avis.getCommentaire());
            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("La création de l'avis a échoué, aucune ligne affectée.");
            }
            try (ResultSet generatedKeys = ps.getGeneratedKeys()){
                if (generatedKeys.next()){
                    avis.setIdAvis(generatedKeys.getInt(1));
                }
            }
            return true;
        } catch(SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    // Récupère la liste des avis pour un hébergement
    public List<Avis> getAvisByHebergementId(int idHebergement) {
        List<Avis> avisList = new ArrayList<>();
        String sql = "SELECT * FROM Avis WHERE idHebergement = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idHebergement);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                Avis avis = new Avis(
                        rs.getInt("idAvis"),
                        rs.getInt("idClient"),
                        rs.getInt("idHebergement"),
                        rs.getInt("note"),
                        rs.getString("commentaire"),
                        rs.getTimestamp("dateAvis").toLocalDateTime()
                );
                avisList.add(avis);
            }
        } catch(SQLException e){
            e.printStackTrace();
        }
        return avisList;
    }
}
