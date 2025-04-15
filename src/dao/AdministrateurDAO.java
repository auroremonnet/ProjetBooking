import java.sql.*;

public class AdministrateurDAO {
    private Connection conn;

    public AdministrateurDAO(Connection conn) {
        this.conn = conn;
    }

    // Récupère un administrateur par email et mot de passe (pour la connexion)
    public Administrateur getAdministrateurByEmailAndPassword(String email, String motDePasse) {
        String sql = "SELECT * FROM Administrateur WHERE email = ? AND motDePasse = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            ps.setString(2, motDePasse);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Administrateur(
                        rs.getInt("idAdministrateur"),
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getString("email"),
                        rs.getString("motDePasse")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Ajoute un nouvel administrateur
    public boolean addAdministrateur(Administrateur admin) {
        String sql = "INSERT INTO Administrateur (nom, prenom, email, motDePasse) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, admin.getNom());
            ps.setString(2, admin.getPrenom());
            ps.setString(3, admin.getEmail());
            ps.setString(4, admin.getMotDePasse());
            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("La création de l'administrateur a échoué, aucune ligne affectée.");
            }
            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    admin.setIdAdministrateur(generatedKeys.getInt(1));
                }
            }
            return true;
        } catch(SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    // D'autres méthodes (update, delete, getById, etc.) peuvent être ajoutées selon les besoins
}
